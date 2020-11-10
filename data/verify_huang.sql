/*==============================================================*/
/* DBMS name:      Microsoft SQL Server 2008                    */
/* Created on:     2020/11/10 12:55:41                          */
/*==============================================================*/


if exists (select 1
            from  sysindexes
           where  id    = object_id('hg_meter_verify_point')
            and   name  = 'idx_verify_point_batch'
            and   indid > 0
            and   indid < 255)
   drop index hg_meter_verify_point.idx_verify_point_batch
go

if exists (select 1
            from  sysindexes
           where  id    = object_id('hg_meter_verify_point')
            and   name  = 'idx_verify_point_item'
            and   indid > 0
            and   indid < 255)
   drop index hg_meter_verify_point.idx_verify_point_item
go

if exists (select 1
            from  sysindexes
           where  id    = object_id('hg_meter_verify_point')
            and   name  = 'idx_verify_point_mid'
            and   indid > 0
            and   indid < 255)
   drop index hg_meter_verify_point.idx_verify_point_mid
go

if exists (select 1
            from  sysobjects
           where  id = object_id('hg_meter_verify_point')
            and   type = 'U')
   drop table hg_meter_verify_point
go

if exists (select 1
            from  sysindexes
           where  id    = object_id('hg_meter_verify_result')
            and   name  = 'idx_verify_item'
            and   indid > 0
            and   indid < 255)
   drop index hg_meter_verify_result.idx_verify_item
go

if exists (select 1
            from  sysindexes
           where  id    = object_id('hg_meter_verify_result')
            and   name  = 'idx_verify_stuff'
            and   indid > 0
            and   indid < 255)
   drop index hg_meter_verify_result.idx_verify_stuff
go

if exists (select 1
            from  sysindexes
           where  id    = object_id('hg_meter_verify_result')
            and   name  = 'idx_verify_meter_date'
            and   indid > 0
            and   indid < 255)
   drop index hg_meter_verify_result.idx_verify_meter_date
go

if exists (select 1
            from  sysobjects
           where  id = object_id('hg_meter_verify_result')
            and   type = 'U')
   drop table hg_meter_verify_result
go

if exists (select 1
            from  sysindexes
           where  id    = object_id('huang_verify_detail')
            and   name  = 'idx_huang_date'
            and   indid > 0
            and   indid < 255)
   drop index huang_verify_detail.idx_huang_date
go

if exists (select 1
            from  sysobjects
           where  id = object_id('huang_verify_detail')
            and   type = 'U')
   drop table huang_verify_detail
go

/*==============================================================*/
/* Table: hg_meter_verify_point                                 */
/*==============================================================*/
create table hg_meter_verify_point (
   pointId              bigint               identity,
   pointIdx             int                  null,
   tempId               varchar(32)          not null,
   pointFlow            double precision     not null,
   pointDev             double precision     null,
   highLimit            double precision     null,
   lowLimit             double precision     null,
   exceed               int                  null,
   meterId              varchar(45)          not null,
   verifyDate           datetime             not null,
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
   constraint PK_HG_METER_VERIFY_POINT primary key nonclustered (pointId)
)
go

if exists(select 1 from sys.extended_properties p where
      p.major_id = object_id('hg_meter_verify_point')
  and p.minor_id = (select c.column_id from sys.columns c where c.object_id = p.major_id and c.name = 'exceed')
)
begin
   execute sp_dropextendedproperty 'MS_Description', 
   'user', 'vc', 'table', 'hg_meter_verify_point', 'column', 'exceed'

end


execute sp_addextendedproperty 'MS_Description', 
   '1-超限; 0-不超限',
   'user', 'vc', 'table', 'hg_meter_verify_point', 'column', 'exceed'
go

/*==============================================================*/
/* Index: idx_verify_point_mid                                  */
/*==============================================================*/
create index idx_verify_point_mid on hg_meter_verify_point (
meterId ASC,
verifyDate DESC,
pointFlow ASC
)
go

/*==============================================================*/
/* Index: idx_verify_point_item                                 */
/*==============================================================*/
create index idx_verify_point_item on hg_meter_verify_point (
instrumentNo ASC,
itemId ASC
)
go

/*==============================================================*/
/* Index: idx_verify_point_batch                                */
/*==============================================================*/
create index idx_verify_point_batch on hg_meter_verify_point (
pointIdx ASC,
meterId ASC,
batchId ASC
)
go

/*==============================================================*/
/* Table: hg_meter_verify_result                                */
/*==============================================================*/
create table hg_meter_verify_result (
   verifyId             bigint               identity,
   meterId              varchar(45)          not null,
   batchId              varchar(45)          not null,
   tempId               varchar(32)          null,
   verifyDate           datetime             not null,
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
   validDate            datetime             null,
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
   constraint PK_HG_METER_VERIFY_RESULT primary key nonclustered (verifyId)
)
go

/*==============================================================*/
/* Index: idx_verify_meter_date                                 */
/*==============================================================*/
create index idx_verify_meter_date on hg_meter_verify_result (
batchId ASC,
meterId ASC,
verifyDate ASC
)
go

/*==============================================================*/
/* Index: idx_verify_stuff                                      */
/*==============================================================*/
create index idx_verify_stuff on hg_meter_verify_result (
stuffName ASC,
verifyDate ASC
)
go

/*==============================================================*/
/* Index: idx_verify_item                                       */
/*==============================================================*/
create index idx_verify_item on hg_meter_verify_result (
instrumentNo ASC,
dataSrc ASC,
itemId ASC
)
go

/*==============================================================*/
/* Table: huang_verify_detail                                   */
/*==============================================================*/
create table huang_verify_detail (
   detailId             bigint               identity,
   batchId              varchar(20)          not null,
   serialId             int                  null,
   meterId              varchar(20)          null,
   verifyDate           datetime             null,
   q3                   float                null,
   q3r                  float                null,
   q2                   float                null,
   q2r                  float                null,
   q1                   float                null,
   q1r                  float                null,
   constraint PK_HUANG_VERIFY_DETAIL primary key (detailId)
)
go

/*==============================================================*/
/* Index: idx_huang_date                                        */
/*==============================================================*/
create index idx_huang_date on huang_verify_detail (
meterId ASC,
verifyDate ASC
)
go

