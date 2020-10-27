-- set serveroutput on
-- transfer data from szcc-oracle to jdzx-oracle

/*
drop index uidx_data_device_data;
/

create unique index uidx_data_device_data on szv_data (devicecode, pipe, postdatetodate);
/

 */

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