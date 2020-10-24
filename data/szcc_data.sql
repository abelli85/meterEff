/*==============================================================*/
/* DBMS name:      ORACLE Version 11g                           */
/* Created on:     2020/10/24 17:03:42                          */
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

drop trigger "CompoundInsertTrigger_szv_data"
/

drop trigger "CompoundUpdateTrigger_szv_data"
/

drop trigger "tib_szv_data_device"
/

drop index INDEX_DATA_ID
/

drop index INDEX_DATA_SIZE
/

drop index INDEX_DATA_TS
/

drop index INDEX_DATA_DEVICE
/

drop table SZV_DATA cascade constraints
/

drop index INDEX_DEVICE_SIZE
/

drop table SZV_DATA_DEVICE cascade constraints
/

drop sequence SEQ_DATA
/

drop sequence SEQ_DEVICE
/

create sequence SEQ_DATA
increment by 1
start with 1
/

create sequence SEQ_DEVICE
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
/* Index: INDEX_DATA_SIZE                                       */
/*==============================================================*/
create index INDEX_DATA_SIZE on SZV_DATA (
   DIAMETERNAME ASC,
   DEVICECODE ASC,
   POSTDATETODATE ASC
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
/* Table: SZV_DATA_DEVICE                                       */
/*==============================================================*/
create table SZV_DATA_DEVICE 
(
   DEVICEID             NUMBER(38)           not null,
   DEVICECODE           VARCHAR2(200),
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
   DEVICECODE ASC,
   DIAMETERNAME ASC
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


create or replace trigger "CompoundInsertTrigger_szv_data"
for insert on SZV_DATA_DEVICE compound trigger
// Declaration
// Body
  before statement is
  begin
     NULL;
  end before statement;

  before each row is
  begin
     NULL;
  end before each row;

  after each row is
  begin
     NULL;
  end after each row;

  after statement is
  begin
     NULL;
  end after statement;

END
/


create or replace trigger "CompoundUpdateTrigger_szv_data"
for update on SZV_DATA_DEVICE compound trigger
// Declaration
// Body
  before statement is
  begin
     NULL;
  end before statement;

  before each row is
  begin
     NULL;
  end before each row;

  after each row is
  begin
     NULL;
  end after each row;

  after statement is
  begin
     NULL;
  end after statement;

END
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

