/*==============================================================*/
/* DBMS name:      PostgreSQL 9.x                               */
/* Created on:     2020/11/5 21:36:00                           */
/*==============================================================*/


drop index IDX_DATA_METER_ID;

drop index IDX_DATA_METER_TS;

drop index INDEX_DATA_ID;

drop index INDEX_DATA_TS;

drop index INDEX_DATA_DEVICE;

drop table SZV_DATA;

drop index IDX_DEVICE_CODE;

drop index IDX_DEVICE_METER_CODE;

drop index IDX_DEVICE_METER_SIZE;

drop index INDEX_DEVICE_SIZE;

drop table SZV_DATA_DEVICE;

drop table SZV_FIRM;

drop index IDX_METER_READ;

drop table SZV_METER_READ;

drop index IDX_USER_DEPT;

drop table SZV_USERINFO;

drop sequence SEQ_DATA;

drop sequence SEQ_DEPT;

drop sequence SEQ_DEVICE;

drop sequence SEQ_METER_READ;

drop sequence SEQ_MUSER;

create sequence SEQ_DATA;

create sequence SEQ_DEPT;

create sequence SEQ_DEVICE;

create sequence SEQ_METER_READ;

create sequence SEQ_MUSER;

/*==============================================================*/
/* Table: SZV_DATA                                              */
/*==============================================================*/
create table SZV_DATA (
   DATAID               NUMERIC(38)          not null,
   DEVICECODE           VARCHAR(200)         null,
   METERCODE            VARCHAR(200)         null,
   PIPE                 NUMERIC(10)          null,
   POSTDATE             NUMERIC(20)          null,
   POSTDATETODATE       DATE                 null,
   METERNUM             NUMERIC(18,3)        null,
   DIAMETERNAME         VARCHAR(60)          not null,
   constraint PK_SZV_DATA primary key (DATAID)
);

/*==============================================================*/
/* Index: INDEX_DATA_DEVICE                                     */
/*==============================================================*/
create  index INDEX_DATA_DEVICE on SZV_DATA (
DEVICECODE,
POSTDATETODATE
);

/*==============================================================*/
/* Index: INDEX_DATA_TS                                         */
/*==============================================================*/
create  index INDEX_DATA_TS on SZV_DATA (
DEVICECODE,
PIPE,
POSTDATE
);

/*==============================================================*/
/* Index: INDEX_DATA_ID                                         */
/*==============================================================*/
create  index INDEX_DATA_ID on SZV_DATA (
DEVICECODE,
DATAID
);

/*==============================================================*/
/* Index: IDX_DATA_METER_TS                                     */
/*==============================================================*/
create  index IDX_DATA_METER_TS on SZV_DATA (
METERCODE,
POSTDATETODATE
);

/*==============================================================*/
/* Index: IDX_DATA_METER_ID                                     */
/*==============================================================*/
create  index IDX_DATA_METER_ID on SZV_DATA (
METERCODE,
DATAID
);

/*==============================================================*/
/* Table: SZV_DATA_DEVICE                                       */
/*==============================================================*/
create table SZV_DATA_DEVICE (
   DEVICEID             NUMERIC(38)          not null,
   DEVICECODE           VARCHAR(200)         null,
   METERCODE            VARCHAR(200)         null,
   PIPE                 NUMERIC(10)          null,
   POSTDATE             NUMERIC(20)          null,
   POSTDATETODATE       DATE                 null,
   METERNUM             NUMERIC(18,3)        null,
   DIAMETERNAME         VARCHAR(60)          not null,
   constraint PK_SZV_DATA_DEVICE primary key (DEVICEID)
);

/*==============================================================*/
/* Index: INDEX_DEVICE_SIZE                                     */
/*==============================================================*/
create  index INDEX_DEVICE_SIZE on SZV_DATA_DEVICE (
DIAMETERNAME,
DEVICECODE
);

/*==============================================================*/
/* Index: IDX_DEVICE_METER_SIZE                                 */
/*==============================================================*/
create  index IDX_DEVICE_METER_SIZE on SZV_DATA_DEVICE (
DIAMETERNAME,
METERCODE
);

/*==============================================================*/
/* Index: IDX_DEVICE_METER_CODE                                 */
/*==============================================================*/
create  index IDX_DEVICE_METER_CODE on SZV_DATA_DEVICE (
METERCODE
);

/*==============================================================*/
/* Index: IDX_DEVICE_CODE                                       */
/*==============================================================*/
create  index IDX_DEVICE_CODE on SZV_DATA_DEVICE (
DEVICECODE
);

/*==============================================================*/
/* Table: SZV_FIRM                                              */
/*==============================================================*/
create table SZV_FIRM (
   FID                  NUMERIC(38)          not null,
   DEPTID               NUMERIC(6)           not null,
   SUBFIRM              VARCHAR(50)          null,
   SUBBRANCH            VARCHAR(50)          null,
   ROOTDEPTID           NUMERIC(6)           not null,
   constraint PK_SZV_FIRM primary key (FID)
);

/*==============================================================*/
/* Table: SZV_METER_READ                                        */
/*==============================================================*/
create table SZV_METER_READ (
   READID               NUMERIC(38)          not null,
   DEPTID               NUMERIC(6)           not null,
   SUBFIRM              VARCHAR(50)          null,
   SUBBRANCH            VARCHAR(50)          null,
   ROOTDEPTID           NUMERIC(6)           not null,
   METERCODE            VARCHAR(12)          not null,
   METERSERIAL          VARCHAR(50)          not null,
   USERSTATUSID         NUMERIC(3)           not null,
   USERWATERMETERSTATUSID NUMERIC(3)           not null,
   USERNAME             VARCHAR(100)         not null,
   USERADDR             VARCHAR(200)         not null,
   METERBRAND           VARCHAR(50)          null,
   MODELSIZE            VARCHAR(20)          not null,
   SIZEID               NUMERIC(10)          null,
   SIZENAME             VARCHAR(20)          not null,
   USETYPE              VARCHAR(500)         null,
   METERTYPE            VARCHAR(150)         null,
   FIRSTINSTALL         DATE                 not null,
   RECENTINSTALL        DATE                 null,
   RECENTREAD           DATE                 null,
   RECENTFWD            NUMERIC(10)          null,
   BUSINESSYEARMONTH    NUMERIC(6)           null,
   LASTREAD             DATE                 null,
   LASTFWD              NUMERIC(10)          null,
   THISFWD              NUMERIC(10)          null,
   THISREADINGTIME      DATE                 null,
   READWATER            NUMERIC              null,
   constraint PK_SZV_METER_READ primary key (READID)
);

/*==============================================================*/
/* Index: IDX_METER_READ                                        */
/*==============================================================*/
create  index IDX_METER_READ on SZV_METER_READ (
ROOTDEPTID,
DEPTID,
METERCODE,
THISREADINGTIME
);

/*==============================================================*/
/* Table: SZV_USERINFO                                          */
/*==============================================================*/
create table SZV_USERINFO (
   MUID                 NUMERIC(38)          not null,
   DEPTID               NUMERIC(6)           not null,
   SUBFIRM              VARCHAR(50)          null,
   SUBBRANCH            VARCHAR(50)          null,
   ROOTDEPTID           NUMERIC(6)           not null,
   METERCODE            VARCHAR(12)          not null,
   METERSERIAL          VARCHAR(50)          not null,
   USERSTATUSID         NUMERIC(3)           not null,
   USERWATERMETERSTATUSID NUMERIC(3)           not null,
   USERNAME             VARCHAR(100)         not null,
   USERADDR             VARCHAR(200)         not null,
   METERBRAND           VARCHAR(50)          null,
   MODELSIZE            VARCHAR(20)          not null,
   SIZEID               NUMERIC(10)          null,
   SIZENAME             VARCHAR(20)          not null,
   USETYPE              VARCHAR(500)         null,
   METERTYPE            VARCHAR(150)         null,
   FIRSTINSTALL         DATE                 not null,
   RECENTINSTALL        DATE                 null,
   RECENTREAD           DATE                 null,
   RECENTFWD            NUMERIC(10)          null,
   constraint PK_SZV_USERINFO primary key (MUID)
);

/*==============================================================*/
/* Index: IDX_USER_DEPT                                         */
/*==============================================================*/
create  index IDX_USER_DEPT on SZV_USERINFO (
ROOTDEPTID,
DEPTID,
METERCODE
);

