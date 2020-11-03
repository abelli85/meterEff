/*
CREATE EXTENSION oracle_fdw;
CREATE SERVER oradb FOREIGN DATA WRAPPER oracle_fdw
    OPTIONS (dbserver 'jdora');
GRANT USAGE ON FOREIGN SERVER jdora TO test;
 */

CREATE USER MAPPING FOR current_user SERVER jdora
    OPTIONS (user 'test', password 'abelli');

CREATE FOREIGN TABLE ora_t1(
    f1 int options (key 'true'),
    f2 varchar(20))
    SERVER jdora OPTIONS (schema 'TEST', table 'T1');

CREATE FOREIGN TABLE szv_data_device(
    deviceId bigint options (key 'true') not null,
    deviceCode character varying(200),
    pipe integer ,
    postDate bigint ,
    postDateToDate timestamp with time zone ,
    meterNum numeric(18, 3) ,
    diameterName character varying(60) not null)
    Server jdora
    options (schema 'TEST', "table" 'SZV_DATA_DEVICE');


CREATE FOREIGN TABLE szv_data(
    dataId bigint options (key 'true') not null,
    deviceCode character varying(200),
    pipe integer ,
    postDate bigint ,
    postDateToDate timestamp with time zone ,
    meterNum numeric(18, 3) ,
    diameterName character varying(60) not null)
    Server jdora
    options (schema 'TEST', "table" 'SZV_DATA');


CREATE FOREIGN TABLE szv_firm(
    fid bigint options (key 'true') not null,
    deptId numeric(6) not null ,
    subFirm character varying(50),
    subBranch character varying(50),
    rootDeptId numeric(6) not null )
    Server jdora
    options (schema 'TEST', "table" 'SZV_FIRM');

CREATE FOREIGN TABLE szv_userInfo(
    muid bigint options (key 'true') not null,
    deptId numeric(6) not null ,
    subFirm character varying(50),
    subBranch character varying(50),
    rootDeptId numeric(6) not null,
    meterCode character varying(12) not null ,
    userName character varying(100) not null ,
    userAddr character varying(200) not null ,
    meterBrand character varying(50),
    modelSize character varying(20) not null ,
    sizeName character varying(20) not null ,
    useType character varying(500),
    meterType character varying(150),
    firstInstall timestamp with time zone not null,
    recentInstall timestamp with time zone,
    recentRead timestamp with time zone,
    recentFwd numeric(10))
    Server jdora
    options (schema 'TEST', "table" 'SZV_USERINFO');
