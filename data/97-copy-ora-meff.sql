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

INSERT INTO bw_meter(meterId, meterCode, meterName, extId, firmId)
SELECT deviceId, deviceCode, deviceCode, deviceCode, '27'
FROM szv_data_device;

INSERT INTO bw_data(extId, sampleTime, forwarddigits, literpulse, firmId)
SELECT zd.deviceCode
     , zd.postDateToDate
     , zd.meterNum
     , 1000
     , '27'
FROM szv_data zd
         JOIN (
    SELECT deviceCode, postDateToDate, MAX(dataId) datId
    FROM szv_data
    WHERE dataId BETWEEN 0 AND 10000000
    GROUP BY deviceCode, postDateToDate
) zdx ON zd.dataId = zdx.datId;
-- first copy:
-- WHERE dataId BETWEEN 0 AND 10000000;


INSERT INTO bw_data(extId, sampleTime, forwarddigits, literpulse, firmId)
SELECT zd.deviceCode
     , zd.postDateToDate
     , zd.meterNum
     , 1000
     , '27'
FROM szv_data zd
WHERE dataId BETWEEN 20000000 AND 30000000;


UPDATE bw_meter m
    SET metername = mu.userName,
        location = mu.userAddr,
        sizename = mu.sizename,
        modelsize = mu.modelsize,
        meterbrandid = mu.meterBrand,
        usertype = substr(mu.useType, 0, 45),
        typeid = mu.meterType,
        installdate = mu.firstInstall,
        onlinedate = mu.recentInstall,
        updatedate = mu.recentRead,
        servicepopulation = mu.recentFwd
FROM szv_userinfo mu
WHERE firmId like '27%'
  and to_number(meterid, '99999') = mu.muid
and mu.muid < 1;


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
, 5
, 10
, 15);


UPDATE bw_meter
SET decayId = 1
WHERE decayId IS NULL;


