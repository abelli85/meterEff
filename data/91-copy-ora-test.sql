-- set serveroutput on
-- transfer data from szcc-oracle to jdzx-oracle

/*
drop index uidx_data_device_data;
/

create unique index uidx_data_device_data on szv_data (devicecode, pipe, postdatetodate);
/

 */

desc szcc_jk.v_meterinfo@szcclnk;

-- 远传大表, 19000+
select count(distinct devicecode) from szv_data_device;

desc szcc_jk.v_data@szcclnk;

select count(1), max(postdatetodate) from szcc_jk.v_data@szcclnk
where devicecode = '01120170700702';

select count(1), max(postdatetodate) from szcc_jk.v_data@szcclnk
where devicecode = 'SS_SK_20200065';


set serveroutput on;
grant execute on utl_file to public;

-- 拷贝单只远传表的数据
create or replace procedure copyOraSingleSzcc(devId integer, dcode varchar, lgr utl_file.file_type)
as
    pcnt   integer;
    pdate1 DATE;
    pdate2 DATE;
    vdate1 DATE;
    vdate2 DATE;
    begin
        dbms_output.put_line(current_timestamp || ', deviceId:' || devId || ' @ ' || dcode);
        if utl_file.is_open(lgr) then
            utl_file.put_line(lgr, current_timestamp || ', deviceId:' || devId || ' @ ' || dcode,
                              true);
        end if;

        pcnt := 0;
        SELECT COUNT(1)
        INTO pcnt
        FROM szv_data
        WHERE deviceCode = dcode;

        if pcnt = 0 THEN
            BEGIN
                utl_file.put_line(lgr, 'first time copy data for ' || dcode, true);

                -- insert all data from szcc-oracle
                /** + ignore_row_on_dupkey_index (szv_data(devicecode, pipe, postdatetodate)) */
                insert into szv_data(devicecode, metercode, pipe, postdate, postdatetodate, meternum, diametername)
                select devicecode, metercode, pipe, postdate, postdatetodate, meternum, diametername
                from szcc_jk.v_data@szcclnk
                where devicecode = dcode;
            END;
        ELSE
            BEGIN
                -- data period in oracle-jdzx
                SELECT MIN(postDateToDate), MAX(postDateToDate)
                INTO pdate1, pdate2
                FROM szv_data
                WHERE deviceCode = dcode;

                -- data period in oracle-szcc
                SELECT MIN(postDateToDate), MAX(postDateToDate)
                INTO vdate1, vdate2
                FROM szcc_jk.v_data@szcclnk
                WHERE deviceCode = dcode;

                if utl_file.is_open(lgr) then
                    utl_file.put_line(lgr,
                                      'pgsql data ' || dcode || ' from ' ||
                                      to_char(pdate1, 'YYYY-MM-DD HH24:MI:SS') || ' - ' ||
                                      to_char(pdate2, 'YYYY-MM-DD HH24:MI:SS'),
                                      true);
                    utl_file.put_line(lgr,
                                      'oracle data ' || dcode || ' from ' ||
                                      to_char(vdate1, 'YYYY-MM-DD HH24:MI:SS') || ' - ' ||
                                      to_char(vdate2, 'YYYY-MM-DD HH24:MI:SS'),
                                      true);
                end if;

                -- left period
                insert into szv_data(devicecode, metercode, pipe, postdate, postdatetodate, meternum, diametername)
                select devicecode, metercode, pipe, postdate, postdatetodate, meternum, diametername
                from szcc_jk.v_data@szcclnk
                where devicecode = dcode
                  AND postDateToDate BETWEEN vdate1 AND pdate1;

                -- right period
                insert into szv_data(devicecode, metercode, pipe, postdate, postdatetodate, meternum, diametername)
                select devicecode, metercode, pipe, postdate, postdatetodate, meternum, diametername
                from szcc_jk.v_data@szcclnk
                where devicecode = dcode
                  AND postDateToDate BETWEEN pdate2 AND vdate2;
            end;
        end if;
    end copyOraSingleSzcc;
/

declare
    lgr utl_file.file_type;
begin
    lgr := utl_file.fopen('TMP', 'copy_szcc.log', 'A', 1000);
    copyOraSingleSzcc(0, 'SS_SK_557325', lgr);
end;
/


-- 拷贝某个分公司的远传数据
create or replace procedure copyLuohuSzcc
as
    lgr    utl_file.file_type;
begin
    lgr := utl_file.fopen('TMP', 'copy_szcc.log', 'A', 1000);
    if utl_file.is_open(lgr) then
        dbms_output.put_line('lgr open good.');
    else
        dbms_output.put_line('lgr open failed.');
    end if;

    for dev in (select distinct d.deviceCode
        from szv_data_device d
        join szv_userinfo u on d.metercode = u.metercode
        where u.subbranch like '%罗湖%')
    loop
        begin
            copyOraSingleSzcc(1, dev.deviceCode, lgr);

            commit;
        end;
    end loop;

end copyLuohuSzcc;
/

/*
execute copyLuohuoSzcc;
/
*/

create or replace directory tmp as '/tmp/';
grant read, write on directory tmp to public;

-- 批量拷贝远传表数据
create or replace procedure copy_szcc(devid1 integer, devid2 integer)
as
    lgr    utl_file.file_type;
begin
    lgr := utl_file.fopen('TMP', 'copy_szcc.log', 'A', 1000);
    if utl_file.is_open(lgr) then
        dbms_output.put_line('lgr open good.');
    else
        dbms_output.put_line('lgr open failed.');
    end if;

    -- are meters changed?
    insert into szv_data_device(devicecode, pipe, metercode, diameterName)
    select devicecode, pipe, metercode, '0'
    from szcc_jk.v_meterinfo@szcclnk
    where devicecode not in (
        select distinct devicecode from szv_data_device
        );
    dbms_output.put_line(current_timestamp || ' 新装远传表: ' || sql%rowcount );
    utl_file.put_line(lgr, current_timestamp || ' 新装远传表: ' || sql%rowcount );

    for dcode in (select deviceId, devicecode
                  from szv_data_device
                  where deviceid between devid1 and devid2
                    order by deviceId
                )
        LOOP
            copyOraSingleSzcc(dcode.deviceId, dcode.devicecode, lgr);

            commit;
        END LOOP;

    dbms_output.put_line(current_timestamp || ' end.');
    utl_file.put_line(lgr, current_timestamp || ' end.', true);
    utl_file.fclose(lgr);
EXCEPTION
    WHEN OTHERS THEN
        begin
            ROLLBACK;
            utl_file.put_line(lgr, 'exception:' || SQLCODE || ', ' || SQLERRM, true);
            utl_file.fclose(lgr);
        end;

end copy_szcc;
/

set serveroutput on;
create or replace directory tmp as '/tmp/';
grant read, write on directory tmp to public;
-- execute copy_szcc(1, 100);
/


desc UCIS.V_JSB_METERCHECK@UCISLNK;

-- DN40+大表, 6万+
select count(distinct metercode) mcnt from szv_userinfo
where userstatusid = 1
and userwatermeterstatusid = 1;

select substr(metertype, 0, 20) mt,
       count(distinct metercode) scnt
from szv_userinfo m1
where metercode in (
    select metercode from szv_userinfo)
group by metertype;

select substr(metertype, 0, 20) mt,
       sizeid,
       substr(sizename, 0, 10) sn,
       count(distinct metercode) scnt
from szv_userinfo m1
where metercode in (
select metercode from szv_userinfo)
group by metertype, sizeid, sizename;


set serveroutput on;
create or replace directory tmp as '/tmp/';
grant read, write on directory tmp to public;

-- copy ucis data from ym1 (like: 201011) to ym2 (202011)
create or replace procedure copy_ucis(ym1 integer, ym2 integer)
as
    lgr    utl_file.file_type;
        testBym1 number(6);
        testBym2 number(6);
        ucisBym1 number(6);
        ucisBym2 number(6);
        thisYr number(4);
        thisMth number(2);
        prd1 number(6);
        prd2 number(6);
    begin
        lgr := utl_file.fopen('TMP', 'copy_ucis.log', 'A', 1000);
        if utl_file.is_open(lgr) then
            dbms_output.put_line('lgr open good.');
        else
            dbms_output.put_line('lgr open failed.');
        end if;

        /*
        drop sequence SEQ_MUSER2;
        create sequence SEQ_MUSER2 increment by 1 start with 1;
         */
        utl_file.put_line(lgr, 'prepare to truncate local szv_userinfo2.', true);
        delete from szv_userinfo2;
        commit;

        INSERT INTO SZV_USERINFO2(
                                   DEPTID
                                 , SUBFIRM
                                 , SUBBRANCH
                                 , ROOTDEPTID
                                 , METERCODE
                                 , METERSERIAL
                                 , USERSTATUSID
                                 , USERWATERMETERSTATUSID
                                 , USERNAME
                                 , USERADDR
                                 , METERBRAND
                                 , MODELSIZE
                                 , SIZEID
                                 , SIZENAME
                                 , USETYPE
                                 , METERTYPE
                                 , FIRSTINSTALL
                                 , RECENTINSTALL
                                 , RECENTREAD
                                 , RECENTFWD
        )
        SELECT DISTINCT
            DEPTID
                      , "分公司"
                      , "水务所"
                      , ROOTDEPTID
                      , "水表编号"
                      , "表码"
                      , USERSTATUSID
                      , USERWATERMETERSTATUSID
                      , "客户名称"
                      , "用水地址"
                      , "水表品牌"
                      , "水表型号"
                      , DIAMETERVALUE
                      , "水表口径"
                      , SUBSTR("用水类型", 0, 500)
                      , "水表类型"
                      , "初装日期"
                      , "最近安装日期"
                      , "最近抄表日期"
                      , "最近抄表行度"
        FROM UCIS.V_JSB_METERCHECK@UCISLNK;
        dbms_output.put_line('intermediate userinfo ' || sql%rowcount );
        utl_file.put_line(lgr, current_timestamp || ' intermediate userinfo ' || sql%rowcount , true);
        commit;

        INSERT INTO SZV_USERINFO(
                                  DEPTID
                                , SUBFIRM
                                , SUBBRANCH
                                , ROOTDEPTID
                                , METERCODE
                                , METERSERIAL
                                , USERSTATUSID
                                , USERWATERMETERSTATUSID
                                , USERNAME
                                , USERADDR
                                , METERBRAND
                                , MODELSIZE
                                , SIZEID
                                , SIZENAME
                                , USETYPE
                                , METERTYPE
                                , FIRSTINSTALL
                                , RECENTINSTALL
                                , RECENTREAD
                                , RECENTFWD
        )
        SELECT DISTINCT
            DEPTID
                      , SUBFIRM
                      , SUBBRANCH
                      , ROOTDEPTID
                      , METERCODE
                      , METERSERIAL
                      , USERSTATUSID
                      , USERWATERMETERSTATUSID
                      , USERNAME
                      , USERADDR
                      , METERBRAND
                      , MODELSIZE
                      , SIZEID
                      , SIZENAME
                      , USETYPE
                      , METERTYPE
                      , FIRSTINSTALL
                      , RECENTINSTALL
                      , RECENTREAD
                      , RECENTFWD
        FROM szv_userinfo2 u2
        where meterCode not in (
            select distinct meterCode
            from szv_userinfo
        );
        dbms_output.put_line('ucis 新装水表: ' || sql%rowcount );
        utl_file.put_line(lgr, 'ucis 新装水表: ' || sql%rowcount , true);
        commit;

        update szv_userinfo u
        set (userStatusId, userWaterMeterStatusId)
                = (select v.userStatusId,
                          v.userWaterMeterStatusId
                   from (select distinct rootDeptId,
                                         meterCode,
                                         userStatusId,
                                         userWaterMeterStatusId
                         from szv_userinfo2
                        ) v
                   where u.rootDeptId = v.rootDeptId
                     and u.meterCode = v.meterCode
            )
        where exists(select 1
                     from szv_userinfo2 v2
                     where u.rootDeptId = v2.rootDeptId
                       and u.meterCode = v2.meterCode);
        dbms_output.put_line('ucis 变动水表: ' || sql%rowcount );
        utl_file.put_line(lgr, 'ucis 变动水表: ' || sql%rowcount , true);
        commit;

        thisYr := to_number(to_char(current_date, 'YYYY'));
        thisMth := to_number(to_char(current_date, 'MM'));
        for ucode in (select distinct rootDeptId from szv_userinfo order by rootDeptId)
            loop
                if ym1 > 201000 and ym2 >= 201000 then
                    if ym2 > (thisYr * 100 + thisMth) then
                        ucisBym2 := thisYr * 100 + thisMth;
                        else
                        ucisBym2 := ym2;
                    end if;

                    testBym2 := ym1;

                    testBym1 := 0;
                    ucisBym1 := 0;
                else
                    select min(businessYearMonth), max(businessYearMonth)
                    into testBym1, testBym2
                    from szv_meter_read
                    where rootDeptId = ucode.rootDeptId;

                    select min(businessYearMonth), max(businessYearMonth)
                    into ucisBym1, ucisBym2
                    from ucis.v_jsb_metercheck@ucislnk
                    where rootDeptId = ucode.rootDeptId;

                    if ucisBym2 > thisYr * 100 + thisMth then
                            ucisBym2 := thisYr * 100 + thisMth;
                    end if;
                    if testBym2 > thisYr * 100 + thisMth then
                        if thisMth - 2 > 0 then
                            testBym2 := thisYr * 100 + thisMth - 2;
                            else
                            testBym2 := (thisYr - 1) * 100 + thisMth + 10;
                        end if;
                    end if;
                    commit;
                end if;

                dbms_output.put_line('同步抄表范围 ' || ucode.rootDeptId || ': test[' || testBym1 || ', ' || testBym2 || '], ucis[' || ucisBym1 || ', ' || ucisBym2 || ']');
                utl_file.put_line(lgr, '同步抄表范围 ' || ucode.rootDeptId || ': test[' || testBym1 || ', ' || testBym2 || '], ucis[' || ucisBym1 || ', ' || ucisBym2 || ']', true);

                while testBym2 <= ucisBym2
                    loop
                        thisYr := trunc(testBym2 / 100);
                        thisMth := mod(testBym2, 100);
                        if thisMth > 11 then
                            prd1 := (thisYr + 1) * 100 + thisMth - 11;
                            else
                            prd1 := thisYr * 100 + thisMth + 1;
                        end if;

                        if thisMth > 10 then
                            prd2 := (thisYr + 1) * 100 + thisMth - 10;
                            else
                            prd2 := thisYr * 100 + thisMth + 2;
                        end if;

                        insert into szv_meter_read(
                            DEPTID
                            , SUBFIRM
                            , SUBBRANCH
                            , METERCODE
                            , USERNAME
                            , USERADDR
                            , METERBRAND
                            , MODELSIZE
                            , SIZENAME
                            , USETYPE
                            , METERTYPE
                            , FIRSTINSTALL
                            , RECENTINSTALL
                            , RECENTREAD
                            , RECENTFWD
                            , BUSINESSYEARMONTH
                            , LASTREAD
                            , LASTFWD
                            , THISFWD
                            , THISREADINGTIME
                            , READWATER
                              , ROOTDEPTID
                              , SIZEID
                              , METERSERIAL
                              , USERSTATUSID
                              , USERWATERMETERSTATUSID
                        )
                        select
                            "DEPTID",
                            "分公司",
                            "水务所",
                            "水表编号",
                            "客户名称",
                            "用水地址",
                            "水表品牌",
                            "水表型号",
                            "水表口径",
                            SUBSTR("用水类型", 0, 500) USETYPE,
                            "水表类型",
                            "初装日期",
                            "最近安装日期",
                            "最近抄表日期",
                            "最近抄表行度",
                            "BUSINESSYEARMONTH",
                            "上次抄表日期",
                            "上次抄表行度",
                            "本次抄表行度",
                            "THISREADINGTIME",
                            "抄表水量",
                            "ROOTDEPTID",
                            "DIAMETERVALUE",
                            "表码",
                            "USERSTATUSID",
                            "USERWATERMETERSTATUSID"
                        from ucis.v_jsb_metercheck@ucislnk
                        where rootDeptId = ucode.rootDeptId
                          and businessYearMonth between prd1 and prd2;

                        testBym2 := prd2;

                        dbms_output.put_line(current_timestamp || ' 同步抄表数据 from ' || prd1 || ' to ' || prd2 || ' rows: ' || sql%rowcount );
                        utl_file.put_line(lgr, current_timestamp || ' 同步抄表数据 from ' || prd1 || ' to ' || prd2 || ' rows: ' || sql%rowcount , true);

                        commit;
                    end loop;

                utl_file.fflush(lgr);
            end loop;
        utl_file.put_line(lgr, current_timestamp || ' 同步ucis数据完毕.', true);
        utl_file.fclose(lgr);
    EXCEPTION
        WHEN OTHERS THEN
            begin
                ROLLBACK;
                utl_file.put_line(lgr, 'exception:' || SQLCODE || ', ' || SQLERRM, true);
                utl_file.fclose(lgr);
            end;
    end copy_ucis;
/

/*

execute copy_ucis(201900, 202101);
/

*/
