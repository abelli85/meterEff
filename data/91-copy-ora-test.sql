-- set serveroutput on
-- transfer data from szcc-oracle to jdzx-oracle

/*
drop index uidx_data_device_data;
/

create unique index uidx_data_device_data on szv_data (devicecode, pipe, postdatetodate);
/

 */

set serveroutput on;

create or replace procedure copy_szcc(devid1 integer, devid2 integer)
as
    pcnt   integer;
    pdate1 DATE;
    pdate2 DATE;
    vdate1 DATE;
    vdate2 DATE;
begin
    for dcode in (select devicecode
                  from szv_data_device
                  where deviceid between devid1 and devid2)
        LOOP
            dbms_output.put_line(current_timestamp || '~' || dcode.devicecode);

            pcnt := 0;
            SELECT COUNT(1)
            INTO pcnt
            FROM szv_data
            WHERE deviceCode = dcode.deviceCode;

            if pcnt = 0 THEN
                BEGIN
                    -- insert all data from szcc-oracle
                    /** + ignore_row_on_dupkey_index (szv_data(devicecode, pipe, postdatetodate)) */
                    insert into szv_data(devicecode, pipe, postdate, postdatetodate, meternum, diametername)
                    select devicecode, pipe, postdate, postdatetodate, meternum, diametername
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

                    -- left period
                    insert into szv_data(devicecode, pipe, postdate, postdatetodate, meternum, diametername)
                    select devicecode, pipe, postdate, postdatetodate, meternum, diametername
                    from szcc_jk.v_data@szcclnk
                    where devicecode = dcode.devicecode
                      AND postDateToDate BETWEEN vdate1 AND pdate1;

                    -- right period
                    insert into szv_data(devicecode, pipe, postdate, postdatetodate, meternum, diametername)
                    select devicecode, pipe, postdate, postdatetodate, meternum, diametername
                    from szcc_jk.v_data@szcclnk
                    where devicecode = dcode.devicecode
                      AND postDateToDate BETWEEN pdate2 AND vdate2;
                end;
            end if;

            commit;
        END LOOP;

    dbms_output.put_line(current_timestamp || ' end.');
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;

end;
/

set serveroutput on;
execute copy_szcc(1001, 1100);
/