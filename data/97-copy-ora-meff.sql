/*
CREATE EXTENSION oracle_fdw;
CREATE SERVER oradb FOREIGN DATA WRAPPER oracle_fdw
    OPTIONS (dbserver 'jdora');
GRANT USAGE ON FOREIGN SERVER jdora TO test;
 */

/*
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
*/

INSERT INTO bw_meter(meterId, meterCode, meterName, extId, firmId)
SELECT deviceId, deviceCode, deviceCode, deviceCode, '27'
FROM szv_data_device;

INSERT INTO bw_data(extId, sampleTime, forwarddigits, literpulse, firmId)
SELECT zd.deviceCode
     , zd.postDateToDate
     , zd.meterNum
     , 1000, '27'
FROM szv_data zd
JOIN (
    SELECT deviceCode, postDateToDate, MAX(dataId) datId
    FROM szv_data
    WHERE dataId BETWEEN 0 AND 10000000
    GROUP BY deviceCode, postDateToDate
    ) zdx ON zd.dataId = zdx.datId;
-- first copy:
-- WHERE dataId BETWEEN 0 AND 10000000;
