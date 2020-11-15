-- 新部署postgis, 创建 bw_data2, 不检查 <extid, sampletime> unique
/*
create table bw_data2 (like bw_data including all);
alter table bw_data2 alter column dataid drop default ;
drop index bw_data2_extid_sampletime_idx ;
create index bw_data2_extid_sampletime_idx on bw_data2 (extid, sampletime);
*/

select count(distinct extid)
from bw_data2;

-- 从 szv_data (in oracle) 拷贝到 bw_data2 (in pgsql).
create or replace procedure pcopyOraTest(dev1 int8, dev2 int8)
    language 'plpgsql'
as
$$
declare
    szidOra int8;
    szidPg  int8;
    vcnt    int8;
begin
    select max(dataid) into szidOra from szv_data;
    if szidOra is null then
        szidOra := -1;
    end if;

    select max(dataid) into szidPg from bw_data2;
    if szidPg is null then
        szidPg := 0;
    end if;

    raise notice 'prepare copy data from % to %', szidOra, szidPg;

    while szidPg < szidOra
        loop
            begin
                INSERT INTO bw_data2(dataid, extId, sampleTime, forwarddigits, literpulse, firmId, szid)

                SELECT zd.dataid
                     , zd.deviceCode
                     , zd.postDateToDate
                     , zd.meterNum
                     , 1000
                     , '27'
                     , zd.dataid
                FROM szv_data zd
                WHERE dataId BETWEEN szidPg + 1 AND szidPg + 1000000;
                get diagnostics vcnt = row_count;
                raise notice 'copy from oracle-test %s rows', vcnt;

                szidPg := szidPg + 1000000;
            end;
        end loop;

    raise notice '% - 从oracle_fdw 迁移历史数据成功: %', current_timestamp, szidPg;
    raise log '% - 从oracle_fdw 迁移历史数据成功: %', current_timestamp, szidPg;

exception
    when others then
        raise notice '% - 从oracle_fdw 迁移历史数据错误: %', current_timestamp, SQLERRM;
        raise log '% - 从oracle_fdw 迁移历史数据错误: %', current_timestamp, SQLERRM;
        rollback;
end
$$;

/*
call pcopyOraTest(1, 100);
*/

-- copy data from bw_data2 (synchronized with oracle-test) to bw_data
create or replace procedure pcopyHist(dev1 int8, dev2 int8)
    language 'plpgsql'
as
$$
declare
    curOut    refcursor;
    curLocal  refcursor;
    extidOut  varchar;
    dcntOut   int8;
    mt1Out    timestamp with time zone;
    mt2Out    timestamp with time zone;
    vcnt      int8;
    vidx      int8;
    dcntLocal int8;
    mt1Local  timestamp with time zone;
    mt2Local  timestamp with time zone;
begin
    vidx := 0;
    open curOut for
        select extid, count(1) dcnt, min(sampletime) mt1, max(sampletime) mt2
        from bw_data2
        group by extid
        order by extid
        limit dev2;

    loop
        begin
            fetch curOut into extidOut, dcntOut, mt1Out, mt2Out;
            exit when not FOUND;

            open curLocal for
                select count(1) dcnt, min(sampletime) mt1, max(sampletime) mt2
                from bw_data
                where extid = extidOut
                group by extid;
            fetch curLocal into dcntLocal, mt1Local, mt2Local;

            vidx := vidx + 1;
            raise notice '%: copy history for %', vidx, extidOut;

            if not FOUND then
                -- insert all data
                insert into bw_data(extid, sampleTime, forwarddigits, literpulse, firmId, szid)
                select extid, sampletime, forwarddigits, 1000, '27', szid
                from bw_data2 d2
                         join (
                    select max(dataid) dataid
                    from bw_data2
                    where extid = extidOut
                    group by extid, sampletime
                ) b2 on d2.dataid = b2.dataid;
                get diagnostics vcnt = ROW_COUNT;
                raise notice 'first time copy history % rows of %', vcnt, extidOut;
            else
                -- left
                insert into bw_data(extid, sampleTime, forwarddigits, literpulse, firmId, szid)
                select extid, sampletime, forwarddigits, 1000, '27', szid
                from bw_data2 d2
                         join (
                    select max(dataid) dataid
                    from bw_data2
                    where extid = extidOut
                      and sampletime < mt1Local
                    group by extid, sampletime
                ) b2 on d2.dataid = b2.dataid;
                get diagnostics vcnt = ROW_COUNT;
                raise notice 'copy history left % rows for [%, %](local), [%, %](out)', vcnt, mt1Local, mt2Local, mt1Out, mt2Out;

                -- right
                insert into bw_data(extid, sampleTime, forwarddigits, literpulse, firmId, szid)
                select extid, sampletime, forwarddigits, 1000, '27', szid
                from bw_data2 d2
                         join (
                    select max(dataid) dataid
                    from bw_data2
                    where extid = extidOut
                      and sampletime > mt2Local
                    group by extid, sampletime
                ) b2 on d2.dataid = b2.dataid;
                get diagnostics vcnt = ROW_COUNT;
                raise notice 'copy history right % rows for [%, %](local), [%, %](out)', vcnt, mt1Local, mt2Local, mt1Out, mt2Out;
            end if;

            close curLocal;
        end;
    end loop;

    raise log '% - 清洗历史数据成功: %', current_timestamp, vidx;

exception
    when others then
        raise notice '% - 清洗历史数据错误: %', current_timestamp, SQLERRM;
        raise log '% - 清洗历史数据错误: %', current_timestamp, SQLERRM;
        rollback;
end
$$;

/*
call pcopyHist(1, 100);
*/

create or replace function pcopySingleMeterRead(mcode varchar(100))
    returns int4
    language 'plpgsql'
as
$$
declare
    localMax timestamptz;
    localYm  numeric(6);
    rcnt     int4;
begin
    -- 截至到1990年
    select max(sampletime) into localMax from bw_data where extid = mcode;
    if localMax is null then
        localMax := '1990-1-1'::timestamptz;
        localYm := 199001;
    else
        localYm := to_number(to_char(localMax, 'YYYYMM'), '999999');
    end if;
    raise notice 'meter code % @ %', mcode, localYm;

    insert into bw_data ( extid
                        , sampletime
                        , endtime
                        , durationsecond
                        , forwardsum
                        , forwarddigits
                        , literpulse
                        , firmid
                        , szid)
    select meterCode                                          AS extid
         , thisReadingTime                                    AS sampletime
         , lastRead                                           AS endtime
         , extract(epoch from age(thisReadingTime, lastRead)) AS durationsecond
         , readWater                                          AS forwardsum
         , thisFwd                                            AS forwarddigits
         , 1000.0                                             AS literpulse
         , '27'                                               AS firmid
         , v.readid                                           AS szid
    from szv_meter_read v
             join (select max(readid) as readid
                   from szv_meter_read
                   where metercode = mcode
                     AND businessYearMonth > localYm
                     AND thisReadingTime > localMax
                   group by businessYearMonth
    ) v2 on v.readid = v2.readid
    where v.metercode = mcode
      AND v.businessYearMonth > localYm;
    get diagnostics rcnt = ROW_COUNT;
    raise notice '% - copy %: % rows', current_timestamp, mcode, rcnt;
    return rcnt;
exception
    when others then
        raise notice '% - fail to commit caused by %', current_timestamp, SQLERRM;
        rollback;
        return -1;
end
$$;

select pcopySingleMeterRead('110111100903');

-- call pcopySingleMeterRead loop
create or replace procedure pcopyMeterRead(fidFilter varchar(100), mcodeFilter varchar(100))
    language plpgsql
as
$$
declare
    curFirm  refcursor;
    curMeter refcursor;
    fid      varchar(100);
    fname    varchar(100);
    mcnt     int4;
    mcode    varchar(100);
    rcnt     int4;
    totalCnt int8;
begin
    totalCnt := 0;
    open curFirm for select firmId, firmName
                     from bw_firm
                     where firmId like fidFilter
                     order by firmid;
    loop
        fetch curFirm into fid, fname;
        exit when not FOUND;

        select count(1)
        into mcnt
        from bw_meter
        where firmid = fid
          and metercode like mcodeFilter
          and powertype = 'MANUAL';
        raise notice '% - 循环拷贝抄表数据: % / %, 水表数量: %', current_timestamp, fid, fname, mcnt;
        raise log '% - 循环拷贝抄表数据: % / %, 水表数量: %', current_timestamp, fid, fname, mcnt;

        open curMeter for select metercode
                          from bw_meter
                          where firmid = fid
                            and metercode like mcodeFilter
                            and powertype = 'MANUAL'
                          order by metercode;
        loop
            -- one transaction can be commit in procedure; but not function.
            begin
                fetch curMeter into mcode;
                exit when not FOUND;

                select pcopySingleMeterRead(mcode) into rcnt;
                totalCnt := totalCnt + rcnt;
            exception
                when others then
                    raise notice '% - fail to commit caused by %', current_timestamp, SQLERRM;
            end;
        end loop; -- single meter

        raise notice '% - copied meter-read for firm: % / %', current_timestamp, fid, fname;
        raise log '% - copied meter-read for firm: % / %', current_timestamp, fid, fname;
        close curMeter;
    end loop; -- single firm.

    raise notice '% - 累计拷贝数据 %, %: %', current_timestamp, fidFilter, mcodeFilter, totalCnt;
    raise log '% - 累计拷贝数据 %, %: %', current_timestamp, fidFilter, mcodeFilter, totalCnt;
    close curFirm;
exception
    when others then
        raise notice '% - 拷贝历史抄表错误: %', current_timestamp, SQLERRM;
        raise log '% - 拷贝历史抄表错误: %', current_timestamp, SQLERRM;
end
$$;

call pcopyMeterRead('270101001', '110111200103');

/*
call pcopyMeterRead('270101001', '%');

call pcopyMeterRead('27__%', '%');
*/