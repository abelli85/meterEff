-- set serveroutput on
-- transfer data from szcc-oracle to jdzx-oracle

/*
drop index uidx_data_device_data;
/

create unique index uidx_data_device_data on szv_data (devicecode, pipe, postdatetodate);
/

 */

desc szcc_jk.v_data@szcclnk;

insert into szv_data_device(devicecode, metercode, pipe)
select distinct devicecode, metercode, pipe
from szcc_jk.v_data@szcclnk
/

insert into szv_data_device(devicecode, metercode, pipe, postdate)
select devicecode, metercode, pipe, max(postdate)
from szcc_jk.v_data@szcclnk
group by devicecode, metercode, pipe
/

set serveroutput on;
grant execute on utl_file to public;

create or replace directory tmp as '/tmp/';
grant read, write on directory tmp to public;
create or replace procedure copy_szcc(devid1 integer, devid2 integer)
as
    pcnt   integer;
    pdate1 DATE;
    pdate2 DATE;
    vdate1 DATE;
    vdate2 DATE;
    lgr    utl_file.file_type;
begin
    lgr := utl_file.fopen('TMP', 'copy_szcc.log', 'A', 1000);
    if utl_file.is_open(lgr) then
        dbms_output.put_line('lgr open good.');
    else
        dbms_output.put_line('lgr open failed.');
    end if;

    for dcode in (select deviceId, devicecode
                  from szv_data_device
                  where deviceid between devid1 and devid2)
        LOOP
            dbms_output.put_line(current_timestamp || ', deviceId:' || dcode.deviceId || ' @ ' || dcode.devicecode);
            utl_file.put_line(lgr, current_timestamp || ', deviceId:' || dcode.deviceId || ' @ ' || dcode.devicecode,
                              true);

            pcnt := 0;
            SELECT COUNT(1)
            INTO pcnt
            FROM szv_data
            WHERE deviceCode = dcode.deviceCode;

            if pcnt = 0 THEN
                BEGIN
                    utl_file.put_line(lgr, 'first time copy data for ' || dcode.devicecode, true);

                    -- insert all data from szcc-oracle
                    /** + ignore_row_on_dupkey_index (szv_data(devicecode, pipe, postdatetodate)) */
                    insert into szv_data(devicecode, metercode, pipe, postdate, postdatetodate, meternum, diametername)
                    select devicecode, metercode, pipe, postdate, postdatetodate, meternum, diametername
                    from szcc_jk.v_data@szcclnk
                    where devicecode = dcode.devicecode;
                END;
            ELSE
                BEGIN
                    -- data period in oracle-jdzx
                    SELECT MIN(postDateToDate), MAX(postDateToDate)
                    INTO pdate1, pdate2
                    FROM szv_data
                    WHERE deviceCode = dcode.deviceCode;

                    -- data period in oracle-szcc
                    SELECT MIN(postDateToDate), MAX(postDateToDate)
                    INTO vdate1, vdate2
                    FROM szcc_jk.v_data@szcclnk
                    WHERE deviceCode = dcode.deviceCode;

                    utl_file.put_line(lgr,
                                      'pgsql data ' || dcode.devicecode || ' from ' ||
                                      to_char(pdate1, 'YYYY-MM-DD HH24:MI:SS') || ' - ' ||
                                      to_char(pdate2, 'YYYY-MM-DD HH24:MI:SS'),
                                      true);
                    utl_file.put_line(lgr,
                                      'oracle data ' || dcode.devicecode || ' from ' ||
                                      to_char(vdate1, 'YYYY-MM-DD HH24:MI:SS') || ' - ' ||
                                      to_char(vdate2, 'YYYY-MM-DD HH24:MI:SS'),
                                      true);

                    -- left period
                    insert into szv_data(devicecode, metercode, pipe, postdate, postdatetodate, meternum, diametername)
                    select devicecode, metercode, pipe, postdate, postdatetodate, meternum, diametername
                    from szcc_jk.v_data@szcclnk
                    where devicecode = dcode.devicecode
                      AND postDateToDate BETWEEN vdate1 AND pdate1;

                    -- right period
                    insert into szv_data(devicecode, metercode, pipe, postdate, postdatetodate, meternum, diametername)
                    select devicecode, metercode, pipe, postdate, postdatetodate, meternum, diametername
                    from szcc_jk.v_data@szcclnk
                    where devicecode = dcode.devicecode
                      AND postDateToDate BETWEEN pdate2 AND vdate2;
                end;
            end if;

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
execute copy_szcc(1201, 1250);
/


-- 账册
insert into szv_firm(deptId, subFirm, subBranch, rootDeptId)
select distinct deptId, "分公司", "水务所", rootDeptId
from ucis.v_jsb_metercheck@ucislnk
/

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
FROM UCIS.V_JSB_METERCHECK@UCISLNK
/

INSERT INTO SZV_METER_READ
( DEPTID
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
, BUSINESSYEARMONTH
, LASTREAD
, LASTFWD
, THISFWD
, THISREADINGTIME
, READWATER)
SELECT
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
     , "BUSINESSYEARMONTH"
     , "上次抄表日期"
     , "上次抄表行度"
     , "本次抄表行度"
     , "THISREADINGTIME"
     , "抄表水量"
FROM UCIS.V_JSB_METERCHECK@UCISLNK
WHERE DEPTID = 723 AND ROOTDEPTID = 210
AND "水表编号" = '141030005301'
/

desc UCIS.V_JSB_METERCHECK@UCISLNK;

select count(distinct metercode) mcnt from szv_userinfo;

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







