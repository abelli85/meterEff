/*==============================================================*/
/* DBMS name:      PostgreSQL 9.x                               */
/* Created on:     2020/11/11 10:51:17                          */
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

drop index idx_verify_point_batch;

drop index idx_verify_point_item;

drop index idx_verify_point_mid;

drop table hg_meter_verify_point;

drop index idx_verify_item;

drop index idx_verify_stuff;

drop index idx_verify_meter_date;

drop table hg_meter_verify_result;

drop sequence SEQ_DATA;

drop sequence SEQ_DEPT;

drop sequence SEQ_DEVICE;

drop sequence SEQ_METER_READ;

drop sequence SEQ_MUSER;

drop user vc;

/*==============================================================*/
/* User: vc                                                     */
/*==============================================================*/
create user vc;

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

/*==============================================================*/
/* Table: hg_meter_verify_point                                 */
/*==============================================================*/
create table hg_meter_verify_point (
   pointId              bigint               not null,
   pointIdx             int                  null,
   tempId               varchar(32)          not null,
   pointFlow            double precision     not null,
   pointDev             double precision     null,
   highLimit            double precision     null,
   lowLimit             double precision     null,
   exceed               int                  null,
   meterId              varchar(45)          not null,
   verifyDate           TIMESTAMP WITH TIME ZONE not null,
   stuffName            varchar(45)          null,
   boardResult          varchar(100)         null,
   pointName            varchar(20)          null,
   startReading         double precision     null,
   endReading           double precision     null,
   totalVolume          double precision     null,
   verifyDura           double precision     null,
   avgFlow              double precision     null,
   waterTemp            double precision     null,
   startMass            double precision     null,
   endMass              double precision     null,
   standardMass         double precision     null,
   density              double precision     null,
   standardVolume       double precision     null,
   standardDura         double precision     null,
   standardFlow         double precision     null,
   instrumentNo         varchar(100)         null,
   batchId              varchar(100)         null,
   boardMemo            varchar(100)         null,
   itemId               bigint               null,
   constraint PK_HG_METER_VERIFY_POINT primary key (pointId)
);

comment on column hg_meter_verify_point.exceed is
'1-超限; 0-不超限';

-- set table ownership
alter table hg_meter_verify_point owner to vc
;
/*==============================================================*/
/* Index: idx_verify_point_mid                                  */
/*==============================================================*/
create  index idx_verify_point_mid on hg_meter_verify_point (
meterId,
verifyDate,
pointFlow
);

/*==============================================================*/
/* Index: idx_verify_point_item                                 */
/*==============================================================*/
create  index idx_verify_point_item on hg_meter_verify_point (
instrumentNo,
itemId
);

/*==============================================================*/
/* Index: idx_verify_point_batch                                */
/*==============================================================*/
create  index idx_verify_point_batch on hg_meter_verify_point (
pointIdx,
meterId,
batchId
);

/*==============================================================*/
/* Table: hg_meter_verify_result                                */
/*==============================================================*/
create table hg_meter_verify_result (
   verifyId             bigint               not null,
   meterId              varchar(45)          not null,
   batchId              varchar(45)          not null,
   tempId               varchar(32)          null,
   verifyDate           TIMESTAMP WITH TIME ZONE not null,
   stuffName            varchar(45)          null,
   verifyResult         varchar(10)          null,
   boardResult          varchar(200)         null,
   forceVerifyNo        varchar(100)         null,
   moduleNo             varchar(100)         null,
   clientName           varchar(100)         null,
   factoryName          varchar(100)         null,
   meterName            varchar(100)         null,
   meterType            varchar(100)         null,
   sizeId               varchar(20)          null,
   modelSize            varchar(100)         null,
   portType             varchar(100)         null,
   verifyRule           varchar(100)         null,
   standardInstrument   varchar(100)         null,
   standardParam        varchar(100)         null,
   indoorTemp           double precision     null,
   moisture             double precision     null,
   validDate            TIMESTAMP WITH TIME ZONE null,
   convertResult        varchar(100)         null,
   pressResult          varchar(100)         null,
   instrumentNo         varchar(100)         null,
   accurateGrade        varchar(100)         null,
   pipeTemp             double precision     null,
   pipePressure         double precision     null,
   pulse                double precision     null,
   density              double precision     null,
   displayDiff          double precision     null,
   convertDiff          double precision     null,
   q4                   double precision     null,
   q3                   double precision     null,
   q3toq1               double precision     null,
   q4toq3               double precision     null,
   q2toq1               double precision     null,
   qr1                  decimal(6,2)         null,
   qr2                  decimal(6,2)         null,
   qr3                  decimal(6,2)         null,
   qr4                  decimal(6,2)         null,
   maxFlow              double precision     null,
   minFlow              double precision     null,
   commonFlow           double precision     null,
   convertLimit         double precision     null,
   verifyAgain          varchar(100)         null,
   outerCheck           varchar(100)         null,
   dataSrc              varchar(100)         null,
   itemId               bigint               null,
   constraint PK_HG_METER_VERIFY_RESULT primary key (verifyId)
);

-- set table ownership
alter table hg_meter_verify_result owner to vc
;
/*==============================================================*/
/* Index: idx_verify_meter_date                                 */
/*==============================================================*/
create  index idx_verify_meter_date on hg_meter_verify_result (
batchId,
meterId,
verifyDate
);

/*==============================================================*/
/* Index: idx_verify_stuff                                      */
/*==============================================================*/
create  index idx_verify_stuff on hg_meter_verify_result (
stuffName,
verifyDate
);

/*==============================================================*/
/* Index: idx_verify_item                                       */
/*==============================================================*/
create  index idx_verify_item on hg_meter_verify_result (
instrumentNo,
dataSrc,
itemId
);

