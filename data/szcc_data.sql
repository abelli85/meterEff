/*==============================================================*/
/* DBMS name:      ORACLE Version 11g                           */
/* Created on:     2020/11/7 17:17:48                           */
/*==============================================================*/



-- Type package declaration
create or replace package PDTypes  
as
    TYPE ref_cursor IS REF CURSOR;
end;

-- Integrity package declaration
create or replace package IntegrityPackage AS
 procedure InitNestLevel;
 function GetNestLevel return number;
 procedure NextNestLevel;
 procedure PreviousNestLevel;
 end IntegrityPackage;
/

-- Integrity package definition
create or replace package body IntegrityPackage AS
 NestLevel number;

-- Procedure to initialize the trigger nest level
 procedure InitNestLevel is
 begin
 NestLevel := 0;
 end;


-- Function to return the trigger nest level
 function GetNestLevel return number is
 begin
 if NestLevel is null then
     NestLevel := 0;
 end if;
 return(NestLevel);
 end;

-- Procedure to increase the trigger nest level
 procedure NextNestLevel is
 begin
 if NestLevel is null then
     NestLevel := 0;
 end if;
 NestLevel := NestLevel + 1;
 end;

-- Procedure to decrease the trigger nest level
 procedure PreviousNestLevel is
 begin
 NestLevel := NestLevel - 1;
 end;

 end IntegrityPackage;
/


drop trigger "tib_szv_data"
/

drop trigger "tib_szv_data_device"
/

drop trigger "tib_szv_firm"
/

drop trigger "tib_szv_meter_read"
/

drop trigger "tib_szv_userinfo"
/

drop trigger "tib_szv_userinfo2"
/

drop index IDX_DATA_METER_ID
/

drop index IDX_DATA_METER_TS
/

drop index INDEX_DATA_ID
/

drop index INDEX_DATA_TS
/

drop index INDEX_DATA_DEVICE
/

drop table SZV_DATA cascade constraints
/

drop index IDX_DEVICE_CODE
/

drop index IDX_DEVICE_METER_CODE
/

drop index IDX_DEVICE_METER_SIZE
/

drop index INDEX_DEVICE_SIZE
/

drop table SZV_DATA_DEVICE cascade constraints
/

drop table SZV_FIRM cascade constraints
/

drop index IDX_METER_READ
/

drop table SZV_METER_READ cascade constraints
/

drop index IDX_USER_DEPT
/

drop table SZV_USERINFO cascade constraints
/

drop index IDX_USER_DEPT2
/

drop table SZV_USERINFO2 cascade constraints
/

drop sequence SEQ_DATA
/

drop sequence SEQ_DEPT
/

drop sequence SEQ_DEVICE
/

drop sequence SEQ_METER_READ
/

drop sequence SEQ_MUSER
/

drop sequence SEQ_MUSER2
/

create sequence SEQ_DATA
increment by 1
start with 1
/

create sequence SEQ_DEPT
increment by 1
start with 1
/

create sequence SEQ_DEVICE
increment by 1
start with 1
/

create sequence SEQ_METER_READ
increment by 1
start with 1
/

create sequence SEQ_MUSER
increment by 1
start with 1
/

create sequence SEQ_MUSER2
increment by 1
start with 1
/

/*==============================================================*/
/* Table: SZV_DATA                                              */
/*==============================================================*/
create table SZV_DATA 
(
   DATAID               NUMBER(38)           not null,
   DEVICECODE           VARCHAR2(200),
   METERCODE            VARCHAR2(200),
   PIPE                 NUMBER(10),
   POSTDATE             NUMBER(20),
   POSTDATETODATE       DATE,
   METERNUM             NUMBER(18,3),
   DIAMETERNAME         VARCHAR2(60)         not null,
   constraint PK_SZV_DATA primary key (DATAID)
)
/

/*==============================================================*/
/* Index: INDEX_DATA_DEVICE                                     */
/*==============================================================*/
create index INDEX_DATA_DEVICE on SZV_DATA (
   DEVICECODE ASC,
   POSTDATETODATE ASC
)
/

/*==============================================================*/
/* Index: INDEX_DATA_TS                                         */
/*==============================================================*/
create index INDEX_DATA_TS on SZV_DATA (
   DEVICECODE ASC,
   PIPE ASC,
   POSTDATE ASC
)
/

/*==============================================================*/
/* Index: INDEX_DATA_ID                                         */
/*==============================================================*/
create index INDEX_DATA_ID on SZV_DATA (
   DEVICECODE ASC,
   DATAID ASC
)
/

/*==============================================================*/
/* Index: IDX_DATA_METER_TS                                     */
/*==============================================================*/
create index IDX_DATA_METER_TS on SZV_DATA (
   METERCODE ASC,
   POSTDATETODATE ASC
)
/

/*==============================================================*/
/* Index: IDX_DATA_METER_ID                                     */
/*==============================================================*/
create index IDX_DATA_METER_ID on SZV_DATA (
   METERCODE ASC,
   DATAID ASC
)
/

/*==============================================================*/
/* Table: SZV_DATA_DEVICE                                       */
/*==============================================================*/
create table SZV_DATA_DEVICE 
(
   DEVICEID             NUMBER(38)           not null,
   DEVICECODE           VARCHAR2(200),
   METERCODE            VARCHAR2(200),
   PIPE                 NUMBER(10),
   POSTDATE             NUMBER(20),
   POSTDATETODATE       DATE,
   METERNUM             NUMBER(18,3),
   DIAMETERNAME         VARCHAR2(60)         not null,
   constraint PK_SZV_DATA_DEVICE primary key (DEVICEID)
)
/

/*==============================================================*/
/* Index: INDEX_DEVICE_SIZE                                     */
/*==============================================================*/
create index INDEX_DEVICE_SIZE on SZV_DATA_DEVICE (
   DIAMETERNAME ASC,
   DEVICECODE ASC
)
/

/*==============================================================*/
/* Index: IDX_DEVICE_METER_SIZE                                 */
/*==============================================================*/
create index IDX_DEVICE_METER_SIZE on SZV_DATA_DEVICE (
   DIAMETERNAME ASC,
   METERCODE ASC
)
/

/*==============================================================*/
/* Index: IDX_DEVICE_METER_CODE                                 */
/*==============================================================*/
create index IDX_DEVICE_METER_CODE on SZV_DATA_DEVICE (
   METERCODE ASC
)
/

/*==============================================================*/
/* Index: IDX_DEVICE_CODE                                       */
/*==============================================================*/
create index IDX_DEVICE_CODE on SZV_DATA_DEVICE (
   DEVICECODE ASC
)
/

/*==============================================================*/
/* Table: SZV_FIRM                                              */
/*==============================================================*/
create table SZV_FIRM 
(
   FID                  NUMBER(38)           not null,
   DEPTID               NUMBER(6)            not null,
   SUBFIRM              VARCHAR2(50),
   SUBBRANCH            VARCHAR2(50),
   ROOTDEPTID           NUMBER(6)            not null,
   constraint PK_SZV_FIRM primary key (FID)
)
/

/*==============================================================*/
/* Table: SZV_METER_READ                                        */
/*==============================================================*/
create table SZV_METER_READ 
(
   READID               NUMBER(38)           not null,
   DEPTID               NUMBER(6)            not null,
   SUBFIRM              VARCHAR2(50),
   SUBBRANCH            VARCHAR2(50),
   ROOTDEPTID           NUMBER(6)            not null,
   METERCODE            VARCHAR2(12)         not null,
   METERSERIAL          VARCHAR2(50)         not null,
   USERSTATUSID         NUMERIC(3)           not null,
   USERWATERMETERSTATUSID NUMERIC(3)           not null,
   USERNAME             NVARCHAR2(100)       not null,
   USERADDR             VARCHAR2(200)        not null,
   METERBRAND           NVARCHAR2(50),
   MODELSIZE             VARCHAR2(20)        not null,
   SIZEID               NUMBER(10),
   SIZENAME              VARCHAR2(20)        not null,
   USETYPE              NVARCHAR2(500),
   METERTYPE             VARCHAR2(150),
   FIRSTINSTALL         DATE                 not null,
   RECENTINSTALL        DATE,
   RECENTREAD           DATE,
   RECENTFWD            NUMBER(10),
   BUSINESSYEARMONTH    NUMBER(6),
   LASTREAD             DATE,
   LASTFWD              NUMBER(10),
   THISFWD              NUMBER(10),
   THISREADINGTIME      DATE,
   READWATER            NUMBER,
   constraint PK_SZV_METER_READ primary key (READID)
)
/

/*==============================================================*/
/* Index: IDX_METER_READ                                        */
/*==============================================================*/
create index IDX_METER_READ on SZV_METER_READ (
   ROOTDEPTID ASC,
   DEPTID ASC,
   METERCODE ASC,
   THISREADINGTIME ASC
)
/

/*==============================================================*/
/* Table: SZV_USERINFO                                          */
/*==============================================================*/
create table SZV_USERINFO 
(
   MUID                 NUMBER(38)           not null,
   DEPTID               NUMBER(6)            not null,
   SUBFIRM              VARCHAR2(50),
   SUBBRANCH            VARCHAR2(50),
   ROOTDEPTID           NUMBER(6)            not null,
   METERCODE            VARCHAR2(12)         not null,
   METERSERIAL          VARCHAR2(50)         not null,
   USERSTATUSID         NUMERIC(3)           not null,
   USERWATERMETERSTATUSID NUMERIC(3)           not null,
   USERNAME             NVARCHAR2(100)       not null,
   USERADDR             VARCHAR2(200)        not null,
   METERBRAND           NVARCHAR2(50),
   MODELSIZE             VARCHAR2(20)        not null,
   SIZEID               NUMBER(10),
   SIZENAME              VARCHAR2(20)        not null,
   USETYPE              NVARCHAR2(500),
   METERTYPE             VARCHAR2(150),
   FIRSTINSTALL         DATE                 not null,
   RECENTINSTALL        DATE,
   RECENTREAD           DATE,
   RECENTFWD            NUMBER(10),
   constraint PK_SZV_USERINFO primary key (MUID)
)
/

/*==============================================================*/
/* Index: IDX_USER_DEPT                                         */
/*==============================================================*/
create index IDX_USER_DEPT on SZV_USERINFO (
   ROOTDEPTID ASC,
   DEPTID ASC,
   METERCODE ASC
)
/

/*==============================================================*/
/* Table: SZV_USERINFO2                                         */
/*==============================================================*/
create table SZV_USERINFO2 
(
   MUID                 NUMBER(38)           not null,
   DEPTID               NUMBER(6)            not null,
   SUBFIRM              VARCHAR2(50),
   SUBBRANCH            VARCHAR2(50),
   ROOTDEPTID           NUMBER(6)            not null,
   METERCODE            VARCHAR2(12)         not null,
   METERSERIAL          VARCHAR2(50)         not null,
   USERSTATUSID         NUMERIC(3)           not null,
   USERWATERMETERSTATUSID NUMERIC(3)           not null,
   USERNAME             NVARCHAR2(100)       not null,
   USERADDR             VARCHAR2(200)        not null,
   METERBRAND           NVARCHAR2(50),
   MODELSIZE             VARCHAR2(20)        not null,
   SIZEID               NUMBER(10),
   SIZENAME              VARCHAR2(20)        not null,
   USETYPE              NVARCHAR2(500),
   METERTYPE             VARCHAR2(150),
   FIRSTINSTALL         DATE                 not null,
   RECENTINSTALL        DATE,
   RECENTREAD           DATE,
   RECENTFWD            NUMBER(10),
   constraint PK_SZV_USERINFO2 primary key (MUID)
)
/

/*==============================================================*/
/* Index: IDX_USER_DEPT2                                        */
/*==============================================================*/
create index IDX_USER_DEPT2 on SZV_USERINFO2 (
   ROOTDEPTID ASC,
   DEPTID ASC,
   METERCODE ASC
)
/


create trigger "tib_szv_data" before insert
on SZV_DATA for each row
declare
    integrity_error  exception;
    errno            integer;
    errmsg           char(200);
    dummy            integer;
    found            boolean;

begin
    --  Column "DATAID" uses sequence SEQ_DATA
    select SEQ_DATA.NEXTVAL INTO :new.DATAID from dual;

--  Errors handling
exception
    when integrity_error then
       raise_application_error(errno, errmsg);
end;
/


create trigger "tib_szv_data_device" before insert
on SZV_DATA_DEVICE for each row
declare
    integrity_error  exception;
    errno            integer;
    errmsg           char(200);
    dummy            integer;
    found            boolean;

begin
    --  Column "DEVICEID" uses sequence SEQ_DEVICE
    select SEQ_DEVICE.NEXTVAL INTO :new.DEVICEID from dual;

--  Errors handling
exception
    when integrity_error then
       raise_application_error(errno, errmsg);
end;
/


create trigger "tib_szv_firm" before insert
on SZV_FIRM for each row
declare
    integrity_error  exception;
    errno            integer;
    errmsg           char(200);
    dummy            integer;
    found            boolean;

begin
    --  Column "FID" uses sequence SEQ_DEPT
    select SEQ_DEPT.NEXTVAL INTO :new.FID from dual;

--  Errors handling
exception
    when integrity_error then
       raise_application_error(errno, errmsg);
end;
/


create trigger "tib_szv_meter_read" before insert
on SZV_METER_READ for each row
declare
    integrity_error  exception;
    errno            integer;
    errmsg           char(200);
    dummy            integer;
    found            boolean;

begin
    --  Column "READID" uses sequence SEQ_METER_READ
    select SEQ_METER_READ.NEXTVAL INTO :new.READID from dual;

--  Errors handling
exception
    when integrity_error then
       raise_application_error(errno, errmsg);
end;
/


create trigger "tib_szv_userinfo" before insert
on SZV_USERINFO for each row
declare
    integrity_error  exception;
    errno            integer;
    errmsg           char(200);
    dummy            integer;
    found            boolean;

begin
    --  Column "MUID" uses sequence SEQ_MUSER
    select SEQ_MUSER.NEXTVAL INTO :new.MUID from dual;

--  Errors handling
exception
    when integrity_error then
       raise_application_error(errno, errmsg);
end;
/


create trigger "tib_szv_userinfo2" before insert
on SZV_USERINFO2 for each row
declare
    integrity_error  exception;
    errno            integer;
    errmsg           char(200);
    dummy            integer;
    found            boolean;

begin
    --  Column "MUID" uses sequence SEQ_MUSER2
    select SEQ_MUSER2.NEXTVAL INTO :new.MUID from dual;

--  Errors handling
exception
    when integrity_error then
       raise_application_error(errno, errmsg);
end;
/

