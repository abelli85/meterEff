/*==============================================================*/
/* DBMS name:      PostgreSQL 9.x                               */
/* Created on:     2021/2/3 18:27:06                            */
/*==============================================================*/


drop table bw_config;

drop index idx_data_create;

drop index idx_data_szid;

drop index idx_data_ext;

drop index idx_data_time;

drop table bw_data;

drop index idx_dma_online;

drop index idx_dma_name;

drop index idx_dma_firm;

drop table bw_dma;

drop index idx_loss_firm;

drop index idx_loss_dma;

drop table bw_dma_loss;

drop index idx_dma_meter_child;

drop table bw_dma_meter;

drop index idx_meter_decay;

drop table bw_eff_decay;

drop index idx_eff_failure_result;

drop index idx_eff_failure_task;

drop index idx_eff_failure_size_model;

drop index idx_eff_failure_time;

drop table bw_eff_failure;

drop index idx_meter_eff_task;

drop index idx_meter_eff_size_model;

drop index idx_meter_eff_time;

drop table bw_eff_meter;

drop index idx_eff_time;

drop index idx_eff_firm;

drop table bw_eff_task;

drop table bw_firm;

drop index idx_meter_dtype;

drop index idx_meter_online;

drop index idx_meter_name;

drop index idx_meter_steel;

drop index idx_meter_firm;

drop index idx_meter_ext;

drop index idx_meter_user;

drop index idx_meter_code;

drop table bw_meter;

drop index idx_eff_meter_point;

drop index idx_eff_meter_flow;

drop table bw_meter_eff_point;

drop table bw_right;

drop table bw_role;

drop table bw_role_right;

drop index idx_rtu_meter;

drop index idx_rtu_firm;

drop table bw_rtu;

drop index idx_rtu_log_cmd;

drop index idx_rtu_log_meter;

drop index idx_rtu_log_rtu;

drop index idx_rtu_log_time;

drop table bw_rtu_log;

drop table bw_user;

drop index idx_login_user;

drop index idx_sid;

drop table bw_user_login;

drop index idx_oper_time;

drop index idx_oper_role;

drop index idx_oper_ip;

drop index idx_oper_user;

drop table bw_user_oper;

drop table bw_user_role;

drop index idx_zone_firm;

drop table bw_zone;

drop table bw_zone_meter;

drop table vc_code;

drop table vc_code_value;

drop index idx_factory_meter_model_order;

drop table vc_factory_meter_model;

drop table vc_meter_factory;

drop table vc_meter_type;

drop index idx_verify_point_batch;

drop index idx_verify_point_item;

drop index idx_verify_point_mid;

drop table vc_meter_verify_point;

drop index idx_verify_item;

drop index idx_verify_stuff;

drop index idx_verify_meter_date;

drop table vc_meter_verify_result;

drop index idx_rpt_point_id;

drop index idx_rpt_point_aid;

drop index idx_rpt_verify_point_item;

drop index idx_rpt_point_meter;

drop table vc_report_verify_point;

drop index idx_rpt_verify;

drop index idx_rpt_verify_item;

drop index idx_rpt_verify_stuff;

drop index idx_rpt_verify_meter_date;

drop table vc_report_verify_result;

drop index idx_work_holi_yr;

drop index idx_work_holi_date;

drop table vc_workday_holiday;

/*==============================================================*/
/* Table: bw_config                                             */
/*==============================================================*/
create table bw_config (
   firmId               VARCHAR(45)          not null,
   groupId              VARCHAR(45)          not null,
   configId             VARCHAR(45)          not null,
   configName           VARCHAR(45)          null,
   configDesc           VARCHAR(45)          null,
   configType           VARCHAR(45)          null,
   value                VARCHAR(200)         null,
   createBy             VARCHAR(45)          not null,
   createDate           TIMESTAMP WITH TIME ZONE not null,
   updateBy             VARCHAR(45)          null,
   updateDate           TIMESTAMP WITH TIME ZONE null,
   constraint PK_BW_CONFIG primary key (firmId, configId, groupId)
);

/*==============================================================*/
/* Table: bw_data                                               */
/*==============================================================*/
create table bw_data (
   dataId               SERIAL8              not null,
   extId                VARCHAR(200)         not null,
   sampleTime           TIMESTAMP WITH TIME ZONE not null,
   endTime              TIMESTAMP WITH TIME ZONE null,
   durationSecond       INT4                 null,
   forwardSum           DECIMAL(15,3)        null,
   revertSum            DECIMAL(15,3)        null,
   pressure             DECIMAL(15,3)        null,
   pressureMin          DECIMAL(15,3)        null,
   pressureMax          DECIMAL(15,3)        null,
   pressureDigits       DECIMAL(15,3)        null,
   avgFlow              DECIMAL(15,3)        null,
   flowMin              DECIMAL(15,3)        null,
   flowMax              DECIMAL(15,3)        null,
   baseDigits           DECIMAL(15,3)        null,
   forwardDigits        DECIMAL(15,3)        null,
   revertDigits         DECIMAL(15,3)        null,
   literPulse           DECIMAL(15,3)        null,
   q1Sum                DECIMAL(15,3)        null,
   q2Sum                DECIMAL(15,3)        null,
   q3Sum                DECIMAL(15,3)        null,
   q0Sum                DECIMAL(15,3)        null,
   q4Sum                DECIMAL(15,3)        null,
   firmId               VARCHAR(45)          null,
   rssi                 INT4                 null,
   szid                 INT8                 null,
   dtype                VARCHAR(20)          null default 'TOTAL',
   createDate           TIMESTAMP WITH TIME ZONE null default CURRENT_TIMESTAMP,
   updateDate           TIMESTAMP WITH TIME ZONE null default CURRENT_TIMESTAMP,
   constraint PK_BW_DATA primary key (dataId)
);

comment on column bw_data.dtype is
'TOTAL/AVG/REAL/DELTA';

/*==============================================================*/
/* Index: idx_data_time                                         */
/*==============================================================*/
create  index idx_data_time on bw_data (
sampleTime,
extId
);

/*==============================================================*/
/* Index: idx_data_ext                                          */
/*==============================================================*/
create unique index idx_data_ext on bw_data (
extId,
sampleTime
);

/*==============================================================*/
/* Index: idx_data_szid                                         */
/*==============================================================*/
create  index idx_data_szid on bw_data (
szid
);

/*==============================================================*/
/* Index: idx_data_create                                       */
/*==============================================================*/
create  index idx_data_create on bw_data (
createDate,
extId,
sampleTime
);

/*==============================================================*/
/* Table: bw_dma                                                */
/*==============================================================*/
create table bw_dma (
   dmaId                VARCHAR(45)          not null,
   dmaName              VARCHAR(45)          not null,
   location             VARCHAR(200)         null,
   dmaLoc               geography            null,
   dmaRegion            geography            null,
   firmId               VARCHAR(45)          not null,
   dmaType              VARCHAR(45)          null,
   showType             VARCHAR(45)          null,
   legalLnf             DECIMAL(15,3)        null,
   status               VARCHAR(45)          null,
   sumUser              DECIMAL(15,3)        null,
   cntUser              INT4                 null,
   avgMonthWater        DECIMAL(15,3)        null,
   devMonthWater        DECIMAL(15,3)        null,
   cntMonth             INT4                 null,
   avgMnfTotal          DECIMAL(15,3)        null,
   devMnfTotal          DECIMAL(15,3)        null,
   cntMnf               INT4                 null,
   onlineDate           TIMESTAMP WITH TIME ZONE null,
   memo                 VARCHAR(45)          null,
   createBy             VARCHAR(45)          null,
   createDate           TIMESTAMP WITH TIME ZONE null,
   updateBy             VARCHAR(45)          null,
   updateDate           TIMESTAMP WITH TIME ZONE null,
   constraint PK_BW_DMA primary key (dmaId)
);

/*==============================================================*/
/* Index: idx_dma_firm                                          */
/*==============================================================*/
create  index idx_dma_firm on bw_dma (
firmId,
dmaId
);

/*==============================================================*/
/* Index: idx_dma_name                                          */
/*==============================================================*/
create  index idx_dma_name on bw_dma (
firmId,
dmaName
);

/*==============================================================*/
/* Index: idx_dma_online                                        */
/*==============================================================*/
create  index idx_dma_online on bw_dma (
firmId,
onlineDate
);

/*==============================================================*/
/* Table: bw_dma_loss                                           */
/*==============================================================*/
create table bw_dma_loss (
   wid                  SERIAL8              not null,
   dmaId                VARCHAR(45)          not null,
   statDate             TIMESTAMP WITH TIME ZONE not null,
   durationDay          INT4                 not null default 1,
   parentPhyloss1       DECIMAL(15,3)        null,
   parentV0             DECIMAL(15,3)        null,
   firmId               VARCHAR(45)          not null,
   comments             VARCHAR(200)         null,
   flowNight            DECIMAL(15,3)        null,
   avgFlow              DECIMAL(15,3)        null,
   flowMax              DECIMAL(15,3)        null,
   flowMin              DECIMAL(15,3)        null,
   numSeconds           INT8                 null,
   status               VARCHAR(45)          null,
   meterId              VARCHAR(45)          null,
   reff                 DECIMAL(15,3)        null,
   parentPhyloss0       DECIMAL(15,3)        null,
   pressureTeff         DECIMAL(15,3)        null,
   phylossRate          DECIMAL(15,3)        null,
   numberMeter          DECIMAL(15,3)        null,
   avgMeterv            DECIMAL(15,3)        null,
   grossMeterv          DECIMAL(15,3)        null,
   visualLoss           DECIMAL(15,3)        null,
   visualLossRate       DECIMAL(15,3)        null,
   constraint PK_BW_DMA_LOSS primary key (wid)
);

/*==============================================================*/
/* Index: idx_loss_dma                                          */
/*==============================================================*/
create  index idx_loss_dma on bw_dma_loss (
dmaId,
statDate,
durationDay
);

/*==============================================================*/
/* Index: idx_loss_firm                                         */
/*==============================================================*/
create  index idx_loss_firm on bw_dma_loss (
firmId,
dmaId,
durationDay,
statDate
);

/*==============================================================*/
/* Table: bw_dma_meter                                          */
/*==============================================================*/
create table bw_dma_meter (
   dmaId                VARCHAR(45)          not null,
   meterId              VARCHAR(45)          not null,
   inOutput             INT4                 not null,
   childType            VARCHAR(20)          null default 'PARENT',
   constraint PK_BW_DMA_METER primary key (dmaId, meterId)
);

comment on column bw_dma_meter.childType is
'PARENT/CHILD';

/*==============================================================*/
/* Index: idx_dma_meter_child                                   */
/*==============================================================*/
create  index idx_dma_meter_child on bw_dma_meter (
dmaId,
childType,
meterId
);

/*==============================================================*/
/* Table: bw_eff_decay                                          */
/*==============================================================*/
create table bw_eff_decay (
   decayId              SERIAL8              not null,
   meterBrandId         VARCHAR(45)          null,
   meterBrandName       VARCHAR(45)          null,
   sizeId               INT4                 not null,
   sizeName             VARCHAR(45)          not null,
   modelSize            VARCHAR(45)          null,
   totalFwd             DECIMAL(15,3)        null,
   decayEff             VARCHAR(20)          null,
   createBy             VARCHAR(45)          null,
   createDate           TIMESTAMP WITH TIME ZONE null,
   updateBy             VARCHAR(45)          null,
   updateDate           TIMESTAMP WITH TIME ZONE null,
   deprecated           BOOL                 null,
   q1                   DECIMAL(15,3)        null,
   q2                   DECIMAL(15,3)        null,
   q3                   DECIMAL(15,3)        null,
   q4                   DECIMAL(15,3)        null,
   q1r                  DECIMAL(15,3)        null,
   q2r                  DECIMAL(15,3)        null,
   q3r                  DECIMAL(15,3)        null,
   q4r                  DECIMAL(15,3)        null,
   qs1                  DECIMAL(15,3)        null,
   qs2                  DECIMAL(15,3)        null,
   qs3                  DECIMAL(15,3)        null,
   qs4                  DECIMAL(15,3)        null,
   qs5                  DECIMAL(15,3)        null,
   qs6                  DECIMAL(15,3)        null,
   qs7                  DECIMAL(15,3)        null,
   qs8                  DECIMAL(15,3)        null,
   qs9                  DECIMAL(15,3)        null,
   qs10                 DECIMAL(15,3)        null,
   qs1r                 DECIMAL(15,3)        null,
   qs2r                 DECIMAL(15,3)        null,
   qs3r                 DECIMAL(15,3)        null,
   qs4r                 DECIMAL(15,3)        null,
   qs5r                 DECIMAL(15,3)        null,
   qs6r                 DECIMAL(15,3)        null,
   qs7r                 DECIMAL(15,3)        null,
   qs8r                 DECIMAL(15,3)        null,
   qs9r                 DECIMAL(15,3)        null,
   qs10r                DECIMAL(15,3)        null,
   constraint PK_BW_EFF_DECAY primary key (decayId)
);

/*==============================================================*/
/* Index: idx_meter_decay                                       */
/*==============================================================*/
create  index idx_meter_decay on bw_eff_decay (
meterBrandId,
sizeId,
modelSize
);

/*==============================================================*/
/* Table: bw_eff_failure                                        */
/*==============================================================*/
create table bw_eff_failure (
   effId                SERIAL8              not null,
   taskId               INT8                 not null,
   meterId              VARCHAR(45)          not null,
   meterName            VARCHAR(100)         not null,
   taskName             VARCHAR(45)          not null,
   taskStart            TIMESTAMP WITH TIME ZONE not null,
   taskEnd              TIMESTAMP WITH TIME ZONE not null,
   periodType           VARCHAR(20)          not null,
   runTime              TIMESTAMP WITH TIME ZONE null,
   runDuration          INT4                 null,
   taskResult           VARCHAR(100)         not null,
   checkResult          VARCHAR(500)         null,
   meterWater           DECIMAL(15,3)        null,
   q0v                  DECIMAL(15,3)        null,
   q1v                  DECIMAL(15,3)        null,
   q2v                  DECIMAL(15,3)        null,
   q3v                  DECIMAL(15,3)        null,
   q4v                  DECIMAL(15,3)        null,
   qtv                  DECIMAL(15,3)        null,
   meterEff             DECIMAL(15,3)        null,
   dataRows             INT4                 null,
   realWater            DECIMAL(15,3)        null,
   startTime            TIMESTAMP WITH TIME ZONE null,
   endTime              TIMESTAMP WITH TIME ZONE null,
   stdDays              DECIMAL(15,3)        null,
   startFwd             DECIMAL(15,3)        null,
   endFwd               DECIMAL(15,3)        null,
   powerType            VARCHAR(45)          null,
   meterBrandId         VARCHAR(45)          not null,
   sizeId               INT4                 not null,
   sizeName             VARCHAR(45)          not null,
   modelSize            VARCHAR(100)         null,
   decayEff             VARCHAR(20)          null,
   srcError             VARCHAR(20)          null,
   q4                   DECIMAL(15,3)        null,
   q3                   DECIMAL(15,3)        null,
   q3toq1               DECIMAL(15,3)        null,
   q4toq3               DECIMAL(15,3)        null,
   q2toq1               DECIMAL(15,3)        null,
   qr1                  DECIMAL(15,3)        null,
   qr2                  DECIMAL(15,3)        null,
   qr3                  DECIMAL(15,3)        null,
   qr4                  DECIMAL(15,3)        null,
   constraint PK_BW_EFF_FAILURE primary key (effId)
);

/*==============================================================*/
/* Index: idx_eff_failure_time                                  */
/*==============================================================*/
create  index idx_eff_failure_time on bw_eff_failure (
meterId,
periodType,
taskStart,
taskEnd,
effId
);

/*==============================================================*/
/* Index: idx_eff_failure_size_model                            */
/*==============================================================*/
create  index idx_eff_failure_size_model on bw_eff_failure (
meterBrandId,
sizeId,
modelSize,
sizeName,
meterId,
periodType,
taskStart
);

/*==============================================================*/
/* Index: idx_eff_failure_task                                  */
/*==============================================================*/
create  index idx_eff_failure_task on bw_eff_failure (
taskId,
meterId,
taskStart
);

/*==============================================================*/
/* Index: idx_eff_failure_result                                */
/*==============================================================*/
create  index idx_eff_failure_result on bw_eff_failure (
taskResult,
periodType,
taskStart,
taskEnd,
meterId,
effId
);

/*==============================================================*/
/* Table: bw_eff_meter                                          */
/*==============================================================*/
create table bw_eff_meter (
   effId                SERIAL8              not null,
   taskId               INT8                 not null,
   meterId              VARCHAR(45)          not null,
   meterName            VARCHAR(100)         not null,
   taskName             VARCHAR(45)          not null,
   taskStart            TIMESTAMP WITH TIME ZONE not null,
   taskEnd              TIMESTAMP WITH TIME ZONE not null,
   periodType           VARCHAR(20)          not null,
   runTime              TIMESTAMP WITH TIME ZONE null,
   runDuration          INT4                 null,
   taskResult           VARCHAR(100)         null,
   meterWater           DECIMAL(15,3)        null,
   q0v                  DECIMAL(15,3)        null,
   q1v                  DECIMAL(15,3)        null,
   q2v                  DECIMAL(15,3)        null,
   q3v                  DECIMAL(15,3)        null,
   q4v                  DECIMAL(15,3)        null,
   qtv                  DECIMAL(15,3)        null,
   meterEff             DECIMAL(15,3)        null,
   dataRows             INT4                 null,
   realWater            DECIMAL(15,3)        null,
   startTime            TIMESTAMP WITH TIME ZONE null,
   endTime              TIMESTAMP WITH TIME ZONE null,
   stdDays              DECIMAL(15,3)        null,
   startFwd             DECIMAL(15,3)        null,
   endFwd               DECIMAL(15,3)        null,
   powerType            VARCHAR(45)          null,
   meterBrandId         VARCHAR(45)          not null,
   sizeId               INT4                 not null,
   sizeName             VARCHAR(45)          not null,
   modelSize            VARCHAR(100)         null,
   decayEff             VARCHAR(20)          null,
   srcError             VARCHAR(20)          null,
   q4                   DECIMAL(15,3)        null,
   q3                   DECIMAL(15,3)        null,
   q3toq1               DECIMAL(15,3)        null,
   q4toq3               DECIMAL(15,3)        null,
   q2toq1               DECIMAL(15,3)        null,
   qr1                  DECIMAL(15,3)        null,
   qr2                  DECIMAL(15,3)        null,
   qr3                  DECIMAL(15,3)        null,
   qr4                  DECIMAL(15,3)        null,
   constraint PK_BW_EFF_METER primary key (effId)
);

/*==============================================================*/
/* Index: idx_meter_eff_time                                    */
/*==============================================================*/
create  index idx_meter_eff_time on bw_eff_meter (
meterId,
periodType,
taskStart,
taskEnd,
effId
);

/*==============================================================*/
/* Index: idx_meter_eff_size_model                              */
/*==============================================================*/
create  index idx_meter_eff_size_model on bw_eff_meter (
meterBrandId,
sizeId,
modelSize,
sizeName,
meterId,
periodType,
taskStart
);

/*==============================================================*/
/* Index: idx_meter_eff_task                                    */
/*==============================================================*/
create  index idx_meter_eff_task on bw_eff_meter (
taskId,
meterId,
taskStart
);

/*==============================================================*/
/* Table: bw_eff_task                                           */
/*==============================================================*/
create table bw_eff_task (
   taskId               SERIAL8              not null,
   taskName             VARCHAR(45)          not null,
   createBy             VARCHAR(45)          null,
   createDate           TIMESTAMP WITH TIME ZONE null,
   firmId               VARCHAR(45)          not null,
   firmName             VARCHAR(45)          not null,
   meterCount           INT4                 null,
   taskMemo             VARCHAR(500)         null,
   taskStart            TIMESTAMP WITH TIME ZONE not null,
   taskEnd              TIMESTAMP WITH TIME ZONE not null,
   periodType           VARCHAR(20)          not null,
   runTime              TIMESTAMP WITH TIME ZONE null,
   runDuration          INT4                 null,
   taskResult           VARCHAR(500)         null,
   totalWater           DECIMAL(15,3)        null,
   totalEff             DECIMAL(15,3)        null,
   realWater            DECIMAL(15,3)        null,
   deprecated           BOOL                 null,
   constraint PK_BW_EFF_TASK primary key (taskId)
);

/*==============================================================*/
/* Index: idx_eff_firm                                          */
/*==============================================================*/
create  index idx_eff_firm on bw_eff_task (
firmId,
taskStart,
taskEnd
);

/*==============================================================*/
/* Index: idx_eff_time                                          */
/*==============================================================*/
create  index idx_eff_time on bw_eff_task (
firmId,
runTime
);

/*==============================================================*/
/* Table: bw_firm                                               */
/*==============================================================*/
create table bw_firm (
   firmId               VARCHAR(45)          not null,
   firmName             VARCHAR(45)          not null,
   firmCity             VARCHAR(100)         null,
   firmLoc              geography            null,
   firmRegion           geography            null,
   smallIcon            VARCHAR(200)         null,
   largeIcon            VARCHAR(200)         null,
   grade                INT4                 null,
   addr                 VARCHAR(200)         null,
   postcode             VARCHAR(8)           null,
   contact              VARCHAR(45)          null,
   phone                VARCHAR(22)          null,
   phone2               VARCHAR(22)          null,
   fax                  VARCHAR(22)          null,
   email                VARCHAR(200)         null,
   memo                 TEXT                 null,
   preinit              BOOL                 null,
   createBy             VARCHAR(45)          null,
   createDate           TIMESTAMP WITH TIME ZONE null,
   updateBy             VARCHAR(45)          null,
   updateDate           TIMESTAMP WITH TIME ZONE null,
   constraint PK_BW_FIRM primary key (firmId)
);

/*==============================================================*/
/* Table: bw_meter                                              */
/*==============================================================*/
create table bw_meter (
   meterId              VARCHAR(45)          not null,
   userCode             VARCHAR(50)          null,
   meterCode            VARCHAR(50)          null,
   meterName            VARCHAR(100)         not null,
   meterOrder           INT4                 null,
   extId                VARCHAR(200)         null,
   location             VARCHAR(200)         null,
   installDate          TIMESTAMP WITH TIME ZONE null,
   onlineDate           TIMESTAMP WITH TIME ZONE null,
   meterPulse           DECIMAL(15,3)        null,
   q1                   DECIMAL(15,3)        null,
   q2                   DECIMAL(15,3)        null,
   q3                   DECIMAL(15,3)        null,
   q4                   DECIMAL(15,3)        null,
   q1r                  DECIMAL(15,3)        null,
   q2r                  DECIMAL(15,3)        null,
   q3r                  DECIMAL(15,3)        null,
   q4r                  DECIMAL(15,3)        null,
   sizeId               INT4                 not null,
   sizeName             VARCHAR(45)          null,
   modelSize            VARCHAR(45)          null,
   typeId               VARCHAR(50)          null,
   userType             VARCHAR(100)         null,
   waterPrice           DECIMAL(15,3)        null,
   serviceArea          DECIMAL(15,3)        null,
   servicePopulation    DECIMAL(15,3)        null,
   contactNumber        VARCHAR(45)          null,
   chargable            VARCHAR(45)          null,
   firmId               VARCHAR(45)          not null,
   meterBrandId         VARCHAR(45)          null,
   steelNo              VARCHAR(45)          null,
   remoteBrandId        VARCHAR(45)          null,
   rtuId                VARCHAR(45)          null,
   rtuCode              VARCHAR(45)          null,
   rtuAddr              VARCHAR(200)         null,
   rtuInstallDate       TIMESTAMP WITH TIME ZONE null,
   rtuInstallPerson     VARCHAR(45)          null,
   rtuContact           VARCHAR(45)          null,
   commCard             VARCHAR(45)          null,
   remoteModel          VARCHAR(45)          null,
   remoteMemo           VARCHAR(45)          null,
   commIsp              VARCHAR(45)          null,
   pressureRange        DECIMAL(15,3)        null,
   pressureMaxLimit     DECIMAL(15,3)        null,
   pressureMinLimit     DECIMAL(15,3)        null,
   powerType            VARCHAR(45)          null,
   meterStatus          VARCHAR(45)          null,
   adminMobile          VARCHAR(45)          null,
   meterLoc             geography            null,
   lastCalib            TIMESTAMP WITH TIME ZONE null,
   memo                 VARCHAR(45)          null,
   decayId              int8                 null,
   createBy             VARCHAR(45)          null,
   createDate           TIMESTAMP WITH TIME ZONE null,
   updateBy             VARCHAR(45)          null,
   updateDate           TIMESTAMP WITH TIME ZONE null,
   dtype                VARCHAR(20)          null default 'TOTAL',
   constraint PK_BW_METER primary key (meterId)
);

comment on column bw_meter.dtype is
'TOTAL/AVG/REAL/DELTA';

/*==============================================================*/
/* Index: idx_meter_code                                        */
/*==============================================================*/
create  index idx_meter_code on bw_meter (
firmId,
meterCode
);

/*==============================================================*/
/* Index: idx_meter_user                                        */
/*==============================================================*/
create  index idx_meter_user on bw_meter (
firmId,
userCode
);

/*==============================================================*/
/* Index: idx_meter_ext                                         */
/*==============================================================*/
create  index idx_meter_ext on bw_meter (
firmId,
extId
);

/*==============================================================*/
/* Index: idx_meter_firm                                        */
/*==============================================================*/
create  index idx_meter_firm on bw_meter (
firmId,
meterId
);

/*==============================================================*/
/* Index: idx_meter_steel                                       */
/*==============================================================*/
create  index idx_meter_steel on bw_meter (
steelNo
);

/*==============================================================*/
/* Index: idx_meter_name                                        */
/*==============================================================*/
create  index idx_meter_name on bw_meter (
firmId,
meterName
);

/*==============================================================*/
/* Index: idx_meter_online                                      */
/*==============================================================*/
create  index idx_meter_online on bw_meter (
firmId,
onlineDate
);

/*==============================================================*/
/* Index: idx_meter_dtype                                       */
/*==============================================================*/
create  index idx_meter_dtype on bw_meter (
dtype,
meterCode
);

/*==============================================================*/
/* Table: bw_meter_eff_point                                    */
/*==============================================================*/
create table bw_meter_eff_point (
   wid                  SERIAL8              not null,
   meterId              VARCHAR(45)          not null,
   taskId               INT8                 not null,
   effId                INT8                 not null,
   periodType           VARCHAR(20)          not null,
   taskStart            TIMESTAMP WITH TIME ZONE not null,
   taskEnd              TIMESTAMP WITH TIME ZONE not null,
   pointType            varchar(20)          not null,
   pointName            varchar(20)          not null,
   pointNo              INT4                 not null,
   pointFlow            DECIMAL(15,3)        not null,
   pointDev             DECIMAL(15,3)        null,
   highLimit            DECIMAL(15,3)        null,
   lowLimit             DECIMAL(15,3)        null,
   pointWater           DECIMAL(15,3)        null,
   realWater            DECIMAL(15,3)        null,
   constraint PK_BW_METER_EFF_POINT primary key (wid)
);

comment on column bw_meter_eff_point.pointType is
'MODEL/EFF';

/*==============================================================*/
/* Index: idx_eff_meter_flow                                    */
/*==============================================================*/
create  index idx_eff_meter_flow on bw_meter_eff_point (
meterId,
taskId,
effId,
taskStart,
taskEnd,
periodType,
pointType,
pointFlow
);

/*==============================================================*/
/* Index: idx_eff_meter_point                                   */
/*==============================================================*/
create  index idx_eff_meter_point on bw_meter_eff_point (
meterId,
taskId,
effId,
taskStart,
taskEnd,
periodType,
pointType,
pointNo
);

/*==============================================================*/
/* Table: bw_right                                              */
/*==============================================================*/
create table bw_right (
   rightName            VARCHAR(45)          not null,
   rightDesc            VARCHAR(45)          null,
   preInit              BOOL                 null,
   constraint PK_BW_RIGHT primary key (rightName)
);

/*==============================================================*/
/* Table: bw_role                                               */
/*==============================================================*/
create table bw_role (
   roleName             VARCHAR(45)          not null,
   roleDesc             VARCHAR(45)          null,
   preInit              BOOL                 null,
   constraint PK_BW_ROLE primary key (roleName)
);

/*==============================================================*/
/* Table: bw_role_right                                         */
/*==============================================================*/
create table bw_role_right (
   roleName             VARCHAR(45)          not null,
   rightName            VARCHAR(45)          not null,
   constraint PK_BW_ROLE_RIGHT primary key (roleName, rightName)
);

/*==============================================================*/
/* Table: bw_rtu                                                */
/*==============================================================*/
create table bw_rtu (
   rtuId                VARCHAR(45)          not null,
   meterId              VARCHAR(45)          null,
   lastTime             TIMESTAMP WITH TIME ZONE null,
   lastCmd              VARCHAR(45)          null,
   lastDataTime         TIMESTAMP WITH TIME ZONE null,
   lastText             VARCHAR(500)         null,
   lastResp             VARCHAR(200)         null,
   firstTime            TIMESTAMP WITH TIME ZONE null,
   firstCmd             VARCHAR(45)          null,
   firstText            VARCHAR(500)         null,
   firstResp            VARCHAR(200)         null,
   meterName            VARCHAR(100)         null,
   meterAddr            VARCHAR(45)          null,
   rtuLoc               POINT                null,
   forwardReading       DECIMAL(15,3)        null,
   revertReading        DECIMAL(15,3)        null,
   literPulse           DECIMAL(15,3)        null,
   rssi                 DECIMAL(15,3)        null,
   reverseWarn          DECIMAL(15,3)        null,
   maxWarn              DECIMAL(15,3)        null,
   cutWarn              DECIMAL(15,3)        null,
   voltWarn             DECIMAL(15,3)        null,
   rssiWarn             DECIMAL(15,3)        null,
   remoteServer         VARCHAR(45)          null,
   stateDesc            VARCHAR(200)         null,
   hoursSleep           DECIMAL(15,3)        null,
   firmId               VARCHAR(45)          null,
   constraint PK_BW_RTU primary key (rtuId)
);

/*==============================================================*/
/* Index: idx_rtu_firm                                          */
/*==============================================================*/
create  index idx_rtu_firm on bw_rtu (
firmId,
rtuId,
meterId
);

/*==============================================================*/
/* Index: idx_rtu_meter                                         */
/*==============================================================*/
create  index idx_rtu_meter on bw_rtu (
firmId,
meterId
);

/*==============================================================*/
/* Table: bw_rtu_log                                            */
/*==============================================================*/
create table bw_rtu_log (
   logId                SERIAL8              not null,
   logTime              TIMESTAMP WITH TIME ZONE null,
   logCmd               VARCHAR(45)          null,
   logLen               INT4                 null,
   rtuId                VARCHAR(45)          null,
   meterId              VARCHAR(45)          null,
   logText              VARCHAR(500)         null,
   logResp              VARCHAR(200)         null,
   logComment           VARCHAR(500)         null,
   forwardReading       DECIMAL(15,3)        null,
   revertReading        DECIMAL(15,3)        null,
   literPulse           DECIMAL(15,3)        null,
   rssi                 DECIMAL(15,3)        null,
   reverseWarn          DECIMAL(15,3)        null,
   maxWarn              DECIMAL(15,3)        null,
   cutWarn              DECIMAL(15,3)        null,
   voltWarn             DECIMAL(15,3)        null,
   rssiWarn             DECIMAL(15,3)        null,
   remoteDevice         VARCHAR(45)          null,
   remoteServer         VARCHAR(45)          null,
   f18                  VARCHAR(45)          null,
   firmId               VARCHAR(45)          null,
   constraint PK_BW_RTU_LOG primary key (logId)
);

/*==============================================================*/
/* Index: idx_rtu_log_time                                      */
/*==============================================================*/
create  index idx_rtu_log_time on bw_rtu_log (
logTime
);

/*==============================================================*/
/* Index: idx_rtu_log_rtu                                       */
/*==============================================================*/
create  index idx_rtu_log_rtu on bw_rtu_log (
rtuId,
meterId,
logTime
);

/*==============================================================*/
/* Index: idx_rtu_log_meter                                     */
/*==============================================================*/
create  index idx_rtu_log_meter on bw_rtu_log (
meterId,
rtuId,
logTime
);

/*==============================================================*/
/* Index: idx_rtu_log_cmd                                       */
/*==============================================================*/
create  index idx_rtu_log_cmd on bw_rtu_log (
logCmd,
rtuId,
meterId,
logTime
);

/*==============================================================*/
/* Table: bw_user                                               */
/*==============================================================*/
create table bw_user (
   userId               VARCHAR(45)          not null,
   userName             VARCHAR(45)          not null,
   verifyStuff          VARCHAR(45)          null,
   mobile               VARCHAR(22)          null,
   email                VARCHAR(200)         null,
   passHash             VARCHAR(45)          null,
   status               VARCHAR(10)          null,
   firmId               VARCHAR(45)          not null,
   smallIcon            VARCHAR(200)         null,
   bigIcon              VARCHAR(200)         null,
   signPic              VARCHAR(200)         null,
   emailValid           BOOL                 null,
   emailToken           VARCHAR(200)         null,
   userToken            VARCHAR(200)         null,
   lastLoginIp          VARCHAR(200)         null,
   lastLoginLoc         geography            null,
   loginCount           INT4                 null,
   lastLoginTime        TIMESTAMP WITH TIME ZONE null,
   lastOperTime         TIMESTAMP WITH TIME ZONE null,
   lastOperIp           VARCHAR(200)         null,
   lastOperLoc          geography            null,
   createBy             VARCHAR(45)          null,
   createDate           TIMESTAMP WITH TIME ZONE null,
   updateBy             VARCHAR(45)          null,
   updateDate           TIMESTAMP WITH TIME ZONE null,
   constraint PK_BW_USER primary key (userId)
);

/*==============================================================*/
/* Table: bw_user_login                                         */
/*==============================================================*/
create table bw_user_login (
   wid                  SERIAL8              not null,
   sessionId            VARCHAR(200)         not null,
   userId               VARCHAR(45)          not null,
   loginTime            TIMESTAMP WITH TIME ZONE not null,
   loginIp              VARCHAR(200)         null,
   loginHost            VARCHAR(200)         null,
   loginAddr            VARCHAR(200)         null,
   loginLoc             geography            null,
   logoutTime           TIMESTAMP WITH TIME ZONE null,
   logoutIp             VARCHAR(200)         null,
   logoutLoc            VARCHAR(200)         null,
   ipAddr               VARCHAR(200)         null,
   devId                VARCHAR(200)         null,
   shareSalt            VARCHAR(45)          null,
   constraint PK_BW_USER_LOGIN primary key (wid)
);

/*==============================================================*/
/* Index: idx_sid                                               */
/*==============================================================*/
create  index idx_sid on bw_user_login (
sessionId
);

/*==============================================================*/
/* Index: idx_login_user                                        */
/*==============================================================*/
create  index idx_login_user on bw_user_login (
userId,
loginTime
);

/*==============================================================*/
/* Table: bw_user_oper                                          */
/*==============================================================*/
create table bw_user_oper (
   operId               SERIAL8              not null,
   userId               VARCHAR(45)          not null,
   operTime             TIMESTAMP WITH TIME ZONE not null,
   returnTime           TIMESTAMP WITH TIME ZONE null,
   firmId               VARCHAR(45)          not null,
   devId                VARCHAR(45)          not null,
   operCase             VARCHAR(45)          null,
   operRole             VARCHAR(45)          null,
   operRight            VARCHAR(45)          null,
   loginIp              VARCHAR(45)          null,
   loginHost            VARCHAR(45)          null,
   loginAddr            VARCHAR(45)          null,
   loginLoc             geography            null,
   clientIp             VARCHAR(45)          null,
   serverIp             VARCHAR(45)          null,
   operCity             VARCHAR(45)          null,
   operResult           VARCHAR(45)          null,
   operCount            INT4                 null,
   operDesc             VARCHAR(200)         null,
   constraint PK_BW_USER_OPER primary key (operId)
);

/*==============================================================*/
/* Index: idx_oper_user                                         */
/*==============================================================*/
create  index idx_oper_user on bw_user_oper (
firmId,
userId,
operTime
);

/*==============================================================*/
/* Index: idx_oper_ip                                           */
/*==============================================================*/
create  index idx_oper_ip on bw_user_oper (
operCity,
loginIp,
operTime
);

/*==============================================================*/
/* Index: idx_oper_role                                         */
/*==============================================================*/
create  index idx_oper_role on bw_user_oper (
operCase,
operRole,
operTime
);

/*==============================================================*/
/* Index: idx_oper_time                                         */
/*==============================================================*/
create  index idx_oper_time on bw_user_oper (
operTime,
firmId,
operRole,
loginIp
);

/*==============================================================*/
/* Table: bw_user_role                                          */
/*==============================================================*/
create table bw_user_role (
   userId               VARCHAR(45)          not null,
   roleName             VARCHAR(45)          not null,
   constraint PK_BW_USER_ROLE primary key (userId, roleName)
);

/*==============================================================*/
/* Table: bw_zone                                               */
/*==============================================================*/
create table bw_zone (
   zoneId               VARCHAR(45)          not null,
   zoneName             VARCHAR(45)          not null,
   zoneType             VARCHAR(45)          not null,
   dmaId                VARCHAR(45)          null,
   level                INT4                 not null,
   leafType             VARCHAR(45)          null,
   memo                 VARCHAR(45)          null,
   bigMeterCount        INT4                 null,
   resiMeterCount       INT4                 null,
   onlineBigMeterCount  INT4                 null,
   onlineResiMeterCount INT4                 null,
   zoneLoc              geography            null,
   zoneRegion           geography            null,
   firmId               VARCHAR(45)          not null,
   leafable             INT4                 null,
   createBy             VARCHAR(45)          null,
   createDate           TIMESTAMP WITH TIME ZONE null,
   updateBy             VARCHAR(45)          null,
   updateDate           TIMESTAMP WITH TIME ZONE null,
   constraint PK_BW_ZONE primary key (zoneId)
);

/*==============================================================*/
/* Index: idx_zone_firm                                         */
/*==============================================================*/
create  index idx_zone_firm on bw_zone (
firmId
);

/*==============================================================*/
/* Table: bw_zone_meter                                         */
/*==============================================================*/
create table bw_zone_meter (
   zoneId               VARCHAR(45)          not null,
   meterId              VARCHAR(45)          not null,
   inOutput             INT4                 not null,
   childType            VARCHAR(20)          null default 'PARENT',
   constraint PK_BW_ZONE_METER primary key (zoneId, meterId)
);

comment on column bw_zone_meter.childType is
'PARENT/CHILD';

/*==============================================================*/
/* Table: vc_code                                               */
/*==============================================================*/
create table vc_code (
   codeId               varchar(20)          not null,
   codeName             varchar(40)          null,
   memo                 varchar(200)         null,
   preInit              BOOL                 null,
   createBy             VARCHAR(45)          null,
   createDate           TIMESTAMP WITH TIME ZONE null default CURRENT_TIMESTAMP,
   updateBy             VARCHAR(45)          null,
   updateDate           TIMESTAMP WITH TIME ZONE null,
   constraint PK_VC_CODE primary key (codeId)
);

/*==============================================================*/
/* Table: vc_code_value                                         */
/*==============================================================*/
create table vc_code_value (
   codeId               varchar(20)          not null,
   valueId              varchar(20)          not null,
   valueName            varchar(40)          null,
   valueOrder           INT4                 null,
   valueType            varchar(20)          null,
   preInit              BOOL                 null,
   disabled             BOOL                 null,
   createBy             VARCHAR(45)          null,
   createDate           TIMESTAMP WITH TIME ZONE null default CURRENT_TIMESTAMP,
   updateBy             VARCHAR(45)          null,
   updateDate           TIMESTAMP WITH TIME ZONE null,
   constraint PK_VC_CODE_VALUE primary key (codeId, valueId)
);

/*==============================================================*/
/* Table: vc_factory_meter_model                                */
/*==============================================================*/
create table vc_factory_meter_model (
   factId               varchar(2)           not null,
   typeId               varchar(2)           not null,
   modelSize            varchar(100)         not null,
   sizeId               NUMERIC(6)           not null,
   modelList            varchar(500)         null,
   valueOrder           INT4                 null,
   preInit              BOOL                 null default true,
   constraint PK_VC_FACTORY_METER_MODEL primary key (factId, typeId, modelSize, sizeId)
);

/*==============================================================*/
/* Index: idx_factory_meter_model_order                         */
/*==============================================================*/
create  index idx_factory_meter_model_order on vc_factory_meter_model (
factId,
valueOrder
);

/*==============================================================*/
/* Table: vc_meter_factory                                      */
/*==============================================================*/
create table vc_meter_factory (
   factId               varchar(2)           not null,
   factName             varchar(45)          null,
   addr                 varchar(45)          null,
   contact              varchar(45)          null,
   phone                varchar(45)          null,
   phone2               varchar(45)          null,
   fax                  varchar(45)          null,
   email                varchar(160)         null,
   memo                 text                 null,
   preInit              BOOL                 null default true,
   createBy             VARCHAR(45)          null,
   createDate           TIMESTAMP WITH TIME ZONE null default CURRENT_TIMESTAMP,
   updateBy             VARCHAR(45)          null,
   updateDate           TIMESTAMP WITH TIME ZONE null,
   constraint PK_VC_METER_FACTORY primary key (factId)
);

/*==============================================================*/
/* Table: vc_meter_type                                         */
/*==============================================================*/
create table vc_meter_type (
   typeId               varchar(2)           not null,
   typeName             varchar(45)          null,
   memo                 varchar(200)         null,
   preInit              BOOL                 null default true,
   constraint PK_VC_METER_TYPE primary key (typeId)
);

/*==============================================================*/
/* Table: vc_meter_verify_point                                 */
/*==============================================================*/
create table vc_meter_verify_point (
   wid                  SERIAL8              not null,
   pointName            varchar(20)          null,
   pointNo              INT4                 null,
   pointFlow            DECIMAL(15,3)        not null,
   pointDev             DECIMAL(15,3)        null,
   highLimit            DECIMAL(15,3)        null,
   lowLimit             DECIMAL(15,3)        null,
   exceed               INT4                 null,
   meterId              varchar(45)          not null,
   verifyDate           TIMESTAMP WITH TIME ZONE not null,
   stuffName            varchar(45)          null,
   boardResult          varchar(100)         null,
   startReading         DECIMAL(15,3)        null,
   endReading           DECIMAL(15,3)        null,
   totalVolume          DECIMAL(15,3)        null,
   verifyDura           DECIMAL(15,3)        null,
   avgFlow              DECIMAL(15,3)        null,
   waterTemp            DECIMAL(15,3)        null,
   startMass            DECIMAL(15,3)        null,
   endMass              DECIMAL(15,3)        null,
   standardMass         DECIMAL(15,3)        null,
   density              DECIMAL(15,3)        null,
   standardVolume       DECIMAL(15,3)        null,
   standardDura         DECIMAL(15,3)        null,
   standardFlow         DECIMAL(15,3)        null,
   instrumentNo         varchar(100)         null,
   batchId              varchar(100)         null,
   boardMemo            varchar(100)         null,
   itemId               INT8                 null,
   constraint PK_VC_METER_VERIFY_POINT primary key (wid)
);

comment on column vc_meter_verify_point.exceed is
'1-超限; 0-不超限';

/*==============================================================*/
/* Index: idx_verify_point_mid                                  */
/*==============================================================*/
create  index idx_verify_point_mid on vc_meter_verify_point (
meterId,
verifyDate,
pointFlow
);

/*==============================================================*/
/* Index: idx_verify_point_item                                 */
/*==============================================================*/
create  index idx_verify_point_item on vc_meter_verify_point (
instrumentNo,
itemId
);

/*==============================================================*/
/* Index: idx_verify_point_batch                                */
/*==============================================================*/
create  index idx_verify_point_batch on vc_meter_verify_point (
meterId,
verifyDate,
pointNo
);

/*==============================================================*/
/* Table: vc_meter_verify_result                                */
/*==============================================================*/
create table vc_meter_verify_result (
   wid                  SERIAL8              not null,
   meterId              varchar(45)          not null,
   batchId              varchar(45)          not null,
   tempId               varchar(32)          null,
   verifyDate           TIMESTAMP WITH TIME ZONE not null,
   stuffName            varchar(45)          null,
   verifyResult         varchar(8)           null,
   boardResult          varchar(200)         null,
   forceVerifyNo        varchar(100)         null,
   moduleNo             varchar(100)         null,
   clientName           varchar(100)         null,
   factoryName          varchar(100)         null,
   meterName            varchar(100)         null,
   meterType            varchar(100)         null,
   sizeId               INT4                 null,
   modelSize            varchar(100)         null,
   portType             varchar(100)         null,
   verifyRule           varchar(100)         null,
   standardInstrument   varchar(100)         null,
   standardParam        varchar(100)         null,
   indoorTemp           DECIMAL(15,3)        null,
   moisture             DECIMAL(15,3)        null,
   validDate            TIMESTAMP WITH TIME ZONE null,
   convertResult        varchar(100)         null,
   pressResult          varchar(100)         null,
   instrumentNo         varchar(100)         null,
   accurateGrade        varchar(100)         null,
   pipeTemp             DECIMAL(15,3)        null,
   pipePressure         DECIMAL(15,3)        null,
   pulse                DECIMAL(15,3)        null,
   density              DECIMAL(15,3)        null,
   displayDiff          DECIMAL(15,3)        null,
   convertDiff          DECIMAL(15,3)        null,
   q4                   DECIMAL(15,3)        null,
   q3                   DECIMAL(15,3)        null,
   q3toq1               DECIMAL(15,3)        null,
   q4toq3               DECIMAL(15,3)        null,
   q2toq1               DECIMAL(15,3)        null,
   qr1                  DECIMAL(15,3)        null,
   qr2                  DECIMAL(15,3)        null,
   qr3                  DECIMAL(15,3)        null,
   qr4                  DECIMAL(15,3)        null,
   maxFlow              DECIMAL(15,3)        null,
   minFlow              DECIMAL(15,3)        null,
   commonFlow           DECIMAL(15,3)        null,
   convertLimit         DECIMAL(15,3)        null,
   verifyAgain          varchar(100)         null,
   outerCheck           varchar(100)         null,
   dataSrc              varchar(100)         null,
   itemId               INT8                 null,
   constraint PK_VC_METER_VERIFY_RESULT primary key (wid)
);

/*==============================================================*/
/* Index: idx_verify_meter_date                                 */
/*==============================================================*/
create  index idx_verify_meter_date on vc_meter_verify_result (
meterId,
verifyDate
);

/*==============================================================*/
/* Index: idx_verify_stuff                                      */
/*==============================================================*/
create  index idx_verify_stuff on vc_meter_verify_result (
stuffName,
verifyDate
);

/*==============================================================*/
/* Index: idx_verify_item                                       */
/*==============================================================*/
create  index idx_verify_item on vc_meter_verify_result (
instrumentNo,
itemId
);

/*==============================================================*/
/* Table: vc_report_verify_point                                */
/*==============================================================*/
create table vc_report_verify_point (
   pointId              INT8                 not null,
   reportAid            INT8                 not null,
   meterId              varchar(45)          not null,
   pointIdx             INT4                 not null,
   rptDate              TIMESTAMP WITH TIME ZONE null,
   tempId               varchar(32)          null,
   pointFlow            NUMERIC(15,3)        not null,
   pointDev             NUMERIC(15,3)        null,
   highLimit            NUMERIC(15,3)        null,
   lowLimit             NUMERIC(15,3)        null,
   exceed               INT4                 null,
   verifyDate           TIMESTAMP WITH TIME ZONE not null,
   stuffName            varchar(45)          null,
   boardResult          varchar(100)         null,
   pointName            varchar(20)          null,
   startReading         NUMERIC(15,3)        null,
   endReading           NUMERIC(15,3)        null,
   totalVolume          NUMERIC(15,3)        null,
   verifyDura           NUMERIC(15,3)        null,
   avgFlow              NUMERIC(15,3)        null,
   waterTemp            NUMERIC(15,3)        null,
   startMass            NUMERIC(15,3)        null,
   endMass              NUMERIC(15,3)        null,
   standardMass         NUMERIC(15,3)        null,
   density              NUMERIC(15,3)        null,
   standardVolume       NUMERIC(15,3)        null,
   standardDura         NUMERIC(15,3)        null,
   standardFlow         NUMERIC(15,3)        null,
   instrumentNo         varchar(100)         null,
   batchId              varchar(100)         null,
   boardMemo            varchar(100)         null,
   itemId               INT8                 null,
   constraint PK_VC_REPORT_VERIFY_POINT primary key (reportAid, meterId, pointIdx)
);

comment on column vc_report_verify_point.exceed is
'1-超限; 0-不超限';

/*==============================================================*/
/* Index: idx_rpt_point_meter                                   */
/*==============================================================*/
create  index idx_rpt_point_meter on vc_report_verify_point (
meterId,
verifyDate
);

/*==============================================================*/
/* Index: idx_rpt_verify_point_item                             */
/*==============================================================*/
create  index idx_rpt_verify_point_item on vc_report_verify_point (
instrumentNo,
itemId
);

/*==============================================================*/
/* Index: idx_rpt_point_aid                                     */
/*==============================================================*/
create  index idx_rpt_point_aid on vc_report_verify_point (
reportAid,
pointId
);

/*==============================================================*/
/* Index: idx_rpt_point_id                                      */
/*==============================================================*/
create  index idx_rpt_point_id on vc_report_verify_point (
pointId
);

/*==============================================================*/
/* Table: vc_report_verify_result                               */
/*==============================================================*/
create table vc_report_verify_result (
   verifyId             INT8                 not null,
   reportAid            INT8                 not null,
   meterId              varchar(45)          not null,
   batchId              varchar(45)          not null,
   certId               varchar(45)          null,
   rptDate              TIMESTAMP WITH TIME ZONE null,
   verifyTimes          INT4                 null,
   tempId               varchar(32)          null,
   verifyDate           TIMESTAMP WITH TIME ZONE not null,
   stuffName            varchar(45)          null,
   verifyResult         varchar(8)           null,
   boardResult          varchar(200)         null,
   forceVerifyNo        varchar(100)         null,
   moduleNo             varchar(100)         null,
   clientName           varchar(100)         null,
   factoryName          varchar(100)         null,
   meterName            varchar(100)         null,
   meterType            varchar(100)         null,
   sizeId               INT4                 null,
   modelSize            varchar(100)         null,
   portType             varchar(100)         null,
   verifyRule           varchar(100)         null,
   standardInstrument   varchar(100)         null,
   standardParam        varchar(100)         null,
   indoorTemp           DECIMAL(15,3)        null,
   moisture             DECIMAL(15,3)        null,
   validDate            TIMESTAMP WITH TIME ZONE null,
   convertResult        varchar(100)         null,
   pressResult          varchar(100)         null,
   instrumentNo         varchar(100)         null,
   accurateGrade        varchar(100)         null,
   pipeTemp             DECIMAL(15,3)        null,
   pipePressure         DECIMAL(15,3)        null,
   pulse                DECIMAL(15,3)        null,
   density              DECIMAL(15,3)        null,
   displayDiff          DECIMAL(15,3)        null,
   convertDiff          DECIMAL(15,3)        null,
   q4                   DECIMAL(15,3)        null,
   q3                   DECIMAL(15,3)        null,
   q3toq1               DECIMAL(15,3)        null,
   q4toq3               DECIMAL(15,3)        null,
   q2toq1               DECIMAL(15,3)        null,
   qr1                  DECIMAL(15,3)        null,
   qr2                  DECIMAL(15,3)        null,
   qr3                  DECIMAL(15,3)        null,
   qr4                  DECIMAL(15,3)        null,
   maxFlow              DECIMAL(15,3)        null,
   minFlow              DECIMAL(15,3)        null,
   commonFlow           DECIMAL(15,3)        null,
   convertLimit         DECIMAL(15,3)        null,
   verifyAgain          varchar(100)         null,
   outerCheck           varchar(100)         null,
   dataSrc              varchar(100)         null,
   itemId               INT8                 null,
   certType             varchar(45)          null,
   constraint PK_VC_REPORT_VERIFY_RESULT primary key (reportAid, meterId)
);

/*==============================================================*/
/* Index: idx_rpt_verify_meter_date                             */
/*==============================================================*/
create  index idx_rpt_verify_meter_date on vc_report_verify_result (
meterId,
verifyDate
);

/*==============================================================*/
/* Index: idx_rpt_verify_stuff                                  */
/*==============================================================*/
create  index idx_rpt_verify_stuff on vc_report_verify_result (
stuffName,
verifyDate
);

/*==============================================================*/
/* Index: idx_rpt_verify_item                                   */
/*==============================================================*/
create  index idx_rpt_verify_item on vc_report_verify_result (
instrumentNo,
itemId
);

/*==============================================================*/
/* Index: idx_rpt_verify                                        */
/*==============================================================*/
create  index idx_rpt_verify on vc_report_verify_result (
verifyId
);

/*==============================================================*/
/* Table: vc_workday_holiday                                    */
/*==============================================================*/
create table vc_workday_holiday (
   wid                  SERIAL8              not null,
   startDate            DATE                 not null,
   endDate              DATE                 not null,
   yr                   INT4                 not null,
   holiday              BOOL                 null default false,
   weekend              BOOL                 null default false,
   workday              BOOL                 null default true,
   ruleType             varchar(100)         null,
   createBy             VARCHAR(45)          null,
   createDate           TIMESTAMP WITH TIME ZONE null default CURRENT_TIMESTAMP,
   updateBy             VARCHAR(45)          null,
   updateDate           TIMESTAMP WITH TIME ZONE null,
   constraint PK_VC_WORKDAY_HOLIDAY primary key (wid)
);

comment on table vc_workday_holiday is
'保存国家法定节假日、工作日调休. holiday=1:节日, 无论是否工作日; weekend=1: 假日, 无论是否工作日; weekday=1: 工作日，无论是否周末.';

/*==============================================================*/
/* Index: idx_work_holi_date                                    */
/*==============================================================*/
create unique index idx_work_holi_date on vc_workday_holiday (
startDate,
endDate
);

/*==============================================================*/
/* Index: idx_work_holi_yr                                      */
/*==============================================================*/
create unique index idx_work_holi_yr on vc_workday_holiday (
yr,
startDate,
endDate
);

