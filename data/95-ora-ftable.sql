/*
CREATE EXTENSION oracle_fdw;
CREATE SERVER oradb FOREIGN DATA WRAPPER oracle_fdw
    OPTIONS (dbserver 'jdora');
GRANT USAGE ON FOREIGN SERVER jdora TO test;
 */

CREATE USER MAPPING FOR current_user SERVER jdora
    OPTIONS (user 'test', password 'abelli');

drop foreign table ora_t1;
CREATE FOREIGN TABLE ora_t1(
    f1 int options (key 'true'),
    f2 varchar(20))
    SERVER jdora OPTIONS (schema 'TEST', table 'T1');

drop foreign table szv_data_device;
CREATE FOREIGN TABLE szv_data_device(
    deviceId bigint options (key 'true') not null,
    deviceCode character varying(200),
    metercode character varying(200),
    pipe integer ,
    postDate bigint ,
    postDateToDate timestamp with time zone ,
    meterNum numeric(18, 3) ,
    diameterName character varying(60) not null)
    Server jdora
    options (schema 'TEST', "table" 'SZV_DATA_DEVICE');

drop foreign table szv_data;
CREATE FOREIGN TABLE szv_data(
    dataId bigint options (key 'true') not null,
    deviceCode character varying(200),
    metercode character varying(200),
    pipe integer ,
    postDate bigint ,
    postDateToDate timestamp with time zone ,
    meterNum numeric(18, 3) ,
    diameterName character varying(60) not null)
    Server jdora
    options (schema 'TEST', "table" 'SZV_DATA');


drop foreign table szv_firm;
CREATE FOREIGN TABLE szv_firm(
    fid bigint options (key 'true') not null,
    deptId numeric(6) not null ,
    subFirm character varying(50),
    subBranch character varying(50),
    rootDeptId numeric(6) not null )
    Server jdora
    options (schema 'TEST', "table" 'SZV_FIRM');

drop foreign table szv_userInfo;
CREATE FOREIGN TABLE szv_userInfo(
    muid bigint options (key 'true') not null,
    deptId numeric(6) not null ,
    subFirm character varying(50),
    subBranch character varying(50),
    rootDeptId numeric(6) not null,
    meterCode character varying(12) not null ,
    meterserial character varying(50) not null,
    userstatusid NUMERIC(3) not null,
    userwatermeterstatusid NUMERIC(3) not null,
    userName character varying(100) not null ,
    userAddr character varying(200) not null ,
    meterBrand character varying(50),
    modelSize character varying(20) not null ,
    sizeId numeric(10),
    sizeName character varying(20) not null ,
    useType character varying(500),
    meterType character varying(150),
    firstInstall timestamp not null,
    recentInstall timestamp with time zone,
    recentRead timestamp with time zone,
    recentFwd numeric(10))
    Server jdora
    options (schema 'TEST', "table" 'SZV_USERINFO');


drop foreign table szv_meter_read;
create foreign table szv_meter_read (
    readid bigint not null ,
    deptid numeric(6) NOT NULL,
        subfirm VARCHAR(50),
        subbranch VARCHAR(50),
        rootdeptid numeric(6) NOT NULL,
        metercode VARCHAR(12) NOT NULL,
        meterserial VARCHAR(50) NOT NULL,
        userstatusid numeric(3) NOT NULL,
        userwatermeterstatusid numeric(3) NOT NULL,
        username VARCHAR(100) NOT NULL,
        useraddr VARCHAR(200) NOT NULL,
        meterbrand VARCHAR(50),
        modelsize VARCHAR(20) NOT NULL,
        sizeid numeric(10),
        sizename VARCHAR(20) NOT NULL,
        usetype VARCHAR(500),
        metertype VARCHAR(150),
        firstinstall timestamptz NOT NULL,
        recentinstall timestamptz,
        recentread timestamptz,
        recentfwd numeric(10),
        businessyearmonth numeric(6),
        lastread timestamptz,
        lastfwd numeric(10),
        thisfwd numeric(10),
        thisreadingtime timestamptz,
        readwater numeric
    )
    server jdora
    options (schema 'TEST', "table" 'SZV_METER_READ');

-- firstinstall cannot be converted to/from oracle!
-- update szv_userinfo set recentread = firstinstall;
-- commit;
/*
desc szv_deptment;
Name					   Null?    Type
 ----------------------------------------- -------- ----------------------------
 DEPTID 				   NOT NULL NUMBER(6)
 ROWVERSION				   NOT NULL NUMBER(10)
 PARENTDEPTID					    NUMBER(6)
 DEPTCODE					    VARCHAR2(60)
 DEPTNAME					    VARCHAR2(150)
 DESCRIBE					    VARCHAR2(600)
 ORDERNUMBER					    NUMBER(3)
 LEVELCODE				   NOT NULL NUMBER(3)
 DEPTTYPE				   NOT NULL NUMBER(3)
 ISACTIVE				   NOT NULL NUMBER(3)
 GISLOCATIONID					    NUMBER(10)
 SHORTNAME					    VARCHAR2(150)
 DELETESTATUS					    NUMBER(1)
 EDEPTID					    NUMBER(6)
*/

drop foreign table szv_deptment;
create foreign table szv_deptment (
    deptid numeric(6) not null,
    rowversion numeric(10) not null ,
    parentdeptid numeric(6),
    deptcode varchar(60),
    deptname varchar(150),
    describe varchar(600),
    ordernumber numeric(3),
    levelcode numeric(3) not null ,
    depttype numeric(3) not null ,
    isactive numeric(3) not null ,
    gislocationid numeric(10),
    shortname varchar(150),
    deletestatus numeric(1),
    edeptid numeric(6)
    ) server jdora
    options (schema 'TEST', "table" 'SZV_DEPTMENT');

/*
-- rule NOT work under big-data?
DROP RULE rule_ignore_dupkey_data ON public.bw_data;

CREATE OR REPLACE RULE rule_ignore_dupkey_data AS
    ON INSERT TO public.bw_data
    WHERE (EXISTS(SELECT 1
                  FROM public.bw_data
                  WHERE bw_data.extid::text = new.extid::text
                    AND bw_data.sampletime = new.sampletime)) DO INSTEAD NOTHING
;

create or replace function sp_ignore_dupkey_data() returns trigger as
$ytt$
begin
    perform 1 from public.bw_data where bw_data.extId = new.extId AND bw_data.sampleTime = new.sampleTime;
    if found then
        return null;
    end if;
    return new;
end;
$ytt$ language 'plpgsql';
-- CREATE FUNCTION

drop trigger tr_ignore_dupkey_data ON bw_data;
create trigger tr_ignore_dupkey_data
    before insert
    on public.bw_data
    for each row
execute procedure sp_ignore_dupkey_data();
-- CREATE TRIGGER
*/