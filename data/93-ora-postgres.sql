CREATE EXTENSION oracle_fdw;
CREATE SERVER jdora FOREIGN DATA WRAPPER oracle_fdw
    OPTIONS (dbserver 'test');
GRANT USAGE ON FOREIGN SERVER jdora TO test;

-- 99-copy-verify.sql
--创建server(在pgAdmin中执行)
-- drop server verifydb cascade;
CREATE SERVER verifydb
    FOREIGN DATA WRAPPER tds_fdw
    OPTIONS (servername '192.168.215.198',character_set 'UTF-8', port '1433', database 'huangdb');

GRANT USAGE ON FOREIGN SERVER verifydb TO meff, public ;

ALTER SERVER verifydb
    OPTIONS (ADD msg_handler 'notice');

-- huang sqlserver: 192.168.215.199
CREATE SERVER huangdb
    FOREIGN DATA WRAPPER tds_fdw
    OPTIONS (servername '192.168.215.199',character_set 'UTF-8', port '1433');

GRANT USAGE ON FOREIGN SERVER huangdb TO meff, public ;

ALTER SERVER huangdb
    OPTIONS (ADD msg_handler 'notice');