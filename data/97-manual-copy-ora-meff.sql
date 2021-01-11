
desc szcc_jk.v_meterinfo@szcclnk;

-- 语句1
insert into szv_data_device(devicecode, metercode, pipe, diametername)
select distinct devicecode, metercode, pipe, '0'
from szcc_jk.v_meterinfo@szcclnk;

commit;
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
FROM UCIS.V_JSB_METERCHECK@UCISLNK;
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
  AND "水表编号" = '141030005301';
/

-- 数据统计
select count(1) from szv_data_device where metercode in (select metercode from szv_userinfo );
-- 18617

select count(1) from szv_data_device ;
-- 19086

insert into bw_meter(meterid,
                     -- usercode,
                     metercode,
                     metername,
                     extid,
                     location,
                     installdate,
                     -- onlinedate,

                     sizeid,
                     sizename,
                     modelsize,
                     typeid,
                     usertype,
                     firmid,
                     meterbrandid,
                     steelno,
                     -- remotebrandid,
                     rtuid,

                     powertype,
                     meterstatus,
                     lastcalib,
                     memo,
                     createby,
                     updatedate
)

select usr.muid,
       usr.meterCode,
       usr.userName,
        dd.devicecode,
       usr.userAddr,
       usr.firstinstall,

       0 sizeId,
       usr.sizeName,
       usr.modelSize,
       usr.meterType,
       substr(usr.useType, 0, 100) userType,
        '27' firmId,
       meterBrand meterbrandid,
       meterserial steelno,
       dd.deviceId,
        (case when dd.metercode is null then 'MANUAL' else 'BATTERY' end) powertype,
       (case when usr.userstatusid = 1 and usr.userwatermeterstatusid = 1 then 'WORK' else 'PAUSE' end) meterstatus,
       recentInstall lastcalib,
                rootDeptId memo,
                'abel' createby,
                current_timestamp as createdate
       from szv_userinfo usr
        left join szv_data_device dd on usr.metercode = dd.metercode
    join (
        select metercode,
               max(muid) xmuid
        from szv_userinfo
        group by metercode
           ) xm on usr.muid = xm.xmuid
    where usr.userstatusid = 1
    and usr.userwatermeterstatusid = 1
-- limit 10
-- firstinstall cannot be converted to/from oracle!
-- update szv_userinfo set recentread = firstinstall;
-- commit;
;

update bw_meter
set sizeid = to_number(sizename, '99999')
;

select powertype, count(1) pcnt
from bw_meter
group by powertype;
-- MANUAL    | 45447
-- BATTERY   | 18283

select count(1)
from szv_userinfo usr
         left join szv_data_device dd on usr.metercode = dd.metercode
         join (
    select metercode,
           max(muid) xmuid
    from szv_userinfo
    group by metercode
) xm on usr.muid = xm.xmuid
where usr.userstatusid = 1
  and usr.userwatermeterstatusid = 1
-- 63730
;

-- 更新远传表
select count(1)
from bw_meter as m
join szv_data_device dd on m.metercode = dd.meterCode;

select count(1)
    from bw_meter m
where m.extid in (select distinct extid from bw_data);

update bw_meter AS m
set firmid = '2703',
    extid = dd.deviceCode
from szv_data_device dd
where m.meterCode = dd.meterCode and m.extid in (select distinct extid from bw_data);
-- 95


DESC ucis.t_ms_department@ucislnk;
create table szv_deptment as select * from ucis.t_ms_department@ucislnk;
create index idx_dept_id on szv_deptment(deptid);
create index idx_dept_code on szv_deptment(deptcode);
/*
Name					   Null?    Type
 ----------------------------------------- -------- ----------------------------
 DEPTID 				   NOT NULL NUMBER(6)
 ROWVERSION				   NOT NULL NUMBER(10)
 PARENTDEPTID					    NUMBER(6)
 DEPTCODE					    VARCHAR2(20)
 DEPTNAME					    VARCHAR2(50)
 DESCRIBE					    VARCHAR2(200)
 ORDERNUMBER					    NUMBER(3)
 LEVELCODE				   NOT NULL NUMBER(3)
 DEPTTYPE				   NOT NULL NUMBER(3)
 ISACTIVE				   NOT NULL NUMBER(3)
 GISLOCATIONID					    NUMBER(10)
 SHORTNAME					    VARCHAR2(50)
 DELETESTATUS					    NUMBER(1)
 EDEPTID					    NUMBER(6)
*/
select LEVEL || ' | ' || deptcode || ' | ' || deptname AS "层级 | 代码 | 名称"
from szv_deptment t
start with t.deptid = 1
connect by prior t.deptid = t.parentdeptid;


insert into bw_firm(firmid, firmname, grade)
select deptcode || '-' || deptid, deptname, deptid from szv_deptment;

-- 保留分公司、所
delete from bw_firm
    where firmid like '1%'
      and firmid not like '1701%'
      and "right"(firmname, 1) not in ('所', '司');

-- 本部区域分公司的下属单位
update bw_firm
    set firmId = '27' || substr(firmid, 2, strpos(firmid, '-') - 2)
where firmid like '101%'
  and firmid < '10106'
  and firmid != '10102';

-- 本部区域分公司
update bw_firm
    set firmid = '2701'
where firmid like '101-%' and grade = 2;

-- 水表归属
update bw_meter AS m
set firmid = f.firmid
from szv_userinfo AS u,
    bw_firm AS f
where f.firmid like '27%'
  and f.grade = u.deptid
  and m.metercode = u.metercode;

INSERT INTO bw_eff_decay(
meterbrandid
, meterbrandname
, sizeid
, sizename
, modelsize
, totalfwd
, decayeff
, q1
, q2
, q3
, q1r
, q2r
, q3r)
values(
'NB'
, 'NB'
, 100
, 'DN100'
, 'WPD'
, 1000000
, 10
, 1
, 1.6
, 100
, 3.0
, 2.0
, 1.0);


UPDATE bw_meter
SET decayId = 1
WHERE decayId IS NULL
and extid in (select distinct extid from bw_data);
;


