-- 新部署postgis, 创建 bw_data2, 不检查 <extid, sampletime> unique
/*
create table bw_data2 (like bw_data including all);
alter table bw_data2 alter column dataid drop default ;
drop index bw_data2_extid_sampletime_idx ;
create index bw_data2_extid_sampletime_idx on bw_data2 (extid, sampletime);
*/

select count(distinct extid) from bw_data2;

create or replace function pcopyOraTest(dev1 int8, dev2 int8)
returns int8
as $$
    declare
        szidOra int8;
        szidPg int8;
        vcnt int8;
    begin
        select max(dataid) into szidOra from szv_data;
        if szidOra is null then
            szidOra = -1;
        end if;

        select max(dataid) into szidPg from bw_data2;
        if szidPg is null then
            szidPg = 0;
        end if;

        raise notice 'prepare copy data from % to %', szidOra, szidPg;

        while szidPg < szidOra
        loop
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
            get diagnostics vcnt = row_count ;
            raise notice 'copy from oracle-test %s rows', vcnt;

            szidPg = szidPg + 1000000;
        end loop;

        raise notice '从oracle_fdw 迁移历史数据成功: %', szidPg;
        raise log '从oracle_fdw 迁移历史数据成功: %', szidPg;
        return szidPg;

        exception when others then
        raise notice '从oracle_fdw 迁移历史数据错误: %', SQLERRM;
        raise log '从oracle_fdw 迁移历史数据错误: %', SQLERRM;
        return 0;
    end;
$$ language 'plpgsql';

/*
select pcopyOraTest(1, 100);
*/

-- copy data from bw_data2 (synchronized with oracle-test) to bw_data
create or replace function pcopyHist(dev1 int8, dev2 int8)
returns int8
as $$
    declare
        curOut cursor for select extid, count(1) dcnt, min(sampletime) mt1, max(sampletime) mt2
        from bw_data2
        group by extid;
        curLocal cursor for select extid, count(1) dcnt, min(sampletime) mt1, max(sampletime) mt2
        from bw_data
        group by extid;
        extidOut varchar;
        dcntOut int8;
        mt1Out timestamp with time zone;
        mt2Out timestamp with time zone;

        vcnt int8;
        vidx int8;

        extidLocal varchar;
        dcntLocal int8;
        mt1Local timestamp with time zone;
        mt2Local timestamp with time zone;
    begin
        vidx = 0;
        open curOut;
        open curLocal;

        loop
            begin
                fetch curOut into extidOut, dcntOut, mt1Out, mt2Out;
                exit when not FOUND;

                fetch curLocal into extidLocal, dcntLocal, mt1Local, mt2Local;

                vidx = vidx + 1;
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
                    get diagnostics vcnt = ROW_COUNT ;
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
                    get diagnostics vcnt = ROW_COUNT ;
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
                    get diagnostics vcnt = ROW_COUNT ;
                    raise notice 'copy history right % rows for [%, %](local), [%, %](out)', vcnt, mt1Local, mt2Local, mt1Out, mt2Out;
                end if;
            end ;
        end loop;

        raise log '清洗历史数据成功: %', vidx;
        return vidx;

        exception
        when others then
            raise log '清洗历史数据错误: %', SQLERRM;
        return 0;
    end;
$$ language 'plpgsql';

/*
select pcopyHist(1, 100);
*/

create or replace function copyMeterRead(dev1 int8, dev2 int8)
as $$
    begin
        declare
    end;
    $$ language 'plpgsql';