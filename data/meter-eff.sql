/*==============================================================*/
/* DBMS name:      PostgreSQL 9.x                               */
/* Created on:     2020/8/19 16:59:39                           */
/*==============================================================*/


drop table BwConfig;

drop index idx_data_time;

drop table BwData;

drop index idx_dma_firm;

drop table BwDma;

drop table BwDmaMeter;

drop table BwEffDecay;

drop table BwEffDetail;

drop table BwEffTask;

drop table BwFirm;

drop index idx_meter_steel;

drop index idx_meter_firm;

drop index idx_meter_ext;

drop index idx_meter_user;

drop index idx_meter_code;

drop table BwMeter;

drop table BwRight;

drop table BwRole;

drop table BwRoleRight;

drop table BwUser;

drop index idx_login_user;

drop index idx_sid;

drop table BwUserLogin;

drop index idx_oper_time;

drop index idx_oper_role;

drop index idx_oper_ip;

drop index idx_oper_user;

drop table BwUserOper;

drop table BwUserRole;

drop index idx_zone_firm;

drop table BwZone;

drop table BwZoneMeter;

drop table vc_code;

drop table vc_code_value;

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

drop index idx_work_holi_yr;

drop index idx_work_holi_date;

drop table vc_workday_holiday;

/*==============================================================*/
/* Table: BwConfig                                              */
/*==============================================================*/
create table BwConfig (
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
   constraint PK_BWCONFIG primary key (firmId, configId, groupId)
);

/*==============================================================*/
/* Table: BwData                                                */
/*==============================================================*/
create table BwData (
   extId                VARCHAR(45)          not null,
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
   constraint PK_BWDATA primary key (extId, sampleTime)
);

/*==============================================================*/
/* Index: idx_data_time                                         */
/*==============================================================*/
create  index idx_data_time on BwData (
sampleTime,
extId
);

/*==============================================================*/
/* Table: BwDma                                                 */
/*==============================================================*/
create table BwDma (
   dmaId                VARCHAR(45)          not null,
   dmaName              VARCHAR(45)          null,
   location             VARCHAR(200)         null,
   dmaLoc               POINT                null,
   dmaRegion            POLYGON              null,
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
   constraint PK_BWDMA primary key (dmaId)
);

/*==============================================================*/
/* Index: idx_dma_firm                                          */
/*==============================================================*/
create  index idx_dma_firm on BwDma (
firmId
);

/*==============================================================*/
/* Table: BwDmaMeter                                            */
/*==============================================================*/
create table BwDmaMeter (
   dmaId                VARCHAR(45)          not null,
   meterId              VARCHAR(45)          not null,
   inOutput             INT4                 not null,
   constraint PK_BWDMAMETER primary key (dmaId, meterId)
);

/*==============================================================*/
/* Table: BwEffDecay                                            */
/*==============================================================*/
create table BwEffDecay (
   wid                  SERIAL not null,
   sizeId               VARCHAR(45)          not null,
   sizeName             VARCHAR(45)          not null,
   modelSize            VARCHAR(100)         null,
   totalFwd             DECIMAL(15,3)        null,
   decayEff             VARCHAR(20)          null,
   createBy             VARCHAR(45)          null,
   createDate           TIMESTAMP WITH TIME ZONE null,
   updateBy             VARCHAR(45)          null,
   updateDate           TIMESTAMP WITH TIME ZONE null,
   deprecated           BOOL                 null,
   constraint PK_BWEFFDECAY primary key (wid)
);

/*==============================================================*/
/* Table: BwEffDetail                                           */
/*==============================================================*/
create table BwEffDetail (
   wid                  INT8                 not null,
   meterId              VARCHAR(45)          not null,
   taskName             VARCHAR(45)          not null,
   taskStart            TIMESTAMP WITH TIME ZONE not null,
   taskEnd              TIMESTAMP WITH TIME ZONE not null,
   runTime              TIMESTAMP WITH TIME ZONE null,
   runDuration          INT4                 null,
   taskResult           VARCHAR(500)         null,
   meterWater           DECIMAL(15,3)        null,
   q0v                  DECIMAL(15,3)        null,
   q1v                  DECIMAL(15,3)        null,
   q2v                  DECIMAL(15,3)        null,
   q3v                  DECIMAL(15,3)        null,
   q4v                  DECIMAL(15,3)        null,
   qtv                  DECIMAL(15,3)        null,
   meterEff             DECIMAL(15,3)        null,
   realWater            DECIMAL(15,3)        null,
   startFwd             DECIMAL(15,3)        null,
   endFwd               DECIMAL(15,3)        null,
   sizeId               VARCHAR(45)          not null,
   sizeName             VARCHAR(45)          not null,
   modelSize            VARCHAR(100)         null,
   decayEff             VARCHAR(20)          null,
   q4                   DECIMAL(15,3)        null,
   q3                   DECIMAL(15,3)        null,
   q3toq1               DECIMAL(15,3)        null,
   q4toq3               DECIMAL(15,3)        null,
   q2toq1               DECIMAL(15,3)        null,
   qr1                  DECIMAL(15,3)        null,
   qr2                  DECIMAL(15,3)        null,
   qr3                  DECIMAL(15,3)        null,
   qr4                  DECIMAL(15,3)        null,
   constraint PK_BWEFFDETAIL primary key (wid, meterId)
);

/*==============================================================*/
/* Table: BwEffTask                                             */
/*==============================================================*/
create table BwEffTask (
   wid                  SERIAL not null,
   taskName             VARCHAR(45)          not null,
   createBy             VARCHAR(45)          null,
   createDate           TIMESTAMP WITH TIME ZONE null,
   firmId               VARCHAR(45)          not null,
   firmName             VARCHAR(45)          not null,
   meterCount           INT4                 null,
   taskMemo             VARCHAR(500)         null,
   taskStart            TIMESTAMP WITH TIME ZONE not null,
   taskEnd              TIMESTAMP WITH TIME ZONE not null,
   runTime              TIMESTAMP WITH TIME ZONE null,
   runDuration          INT4                 null,
   taskResult           VARCHAR(500)         null,
   totalWater           DECIMAL(15,3)        null,
   totalEff             DECIMAL(15,3)        null,
   realWater            DECIMAL(15,3)        null,
   deprecated           BOOL                 null,
   constraint PK_BWEFFTASK primary key (wid)
);

/*==============================================================*/
/* Table: BwFirm                                                */
/*==============================================================*/
create table BwFirm (
   firmId               VARCHAR(45)          not null,
   firmName             VARCHAR(45)          not null,
   firmLoc              POINT                null,
   firmRegion           POLYGON              null,
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
   constraint PK_BWFIRM primary key (firmId)
);

/*==============================================================*/
/* Table: BwMeter                                               */
/*==============================================================*/
create table BwMeter (
   meterId              VARCHAR(45)          not null,
   userCode             VARCHAR(45)          null,
   meterCode            VARCHAR(45)          null,
   meterName            VARCHAR(45)          null,
   meterOrder           INT4                 null,
   extId                VARCHAR(45)          null,
   location             VARCHAR(200)         null,
   installDate          TIMESTAMP WITH TIME ZONE null,
   meterPulse           DECIMAL(15,3)        null,
   q1                   DECIMAL(15,3)        null,
   q2                   DECIMAL(15,3)        null,
   q3                   DECIMAL(15,3)        null,
   q4                   DECIMAL(15,3)        null,
   q1r                  DECIMAL(15,3)        null,
   q2r                  DECIMAL(15,3)        null,
   q3r                  DECIMAL(15,3)        null,
   q4r                  DECIMAL(15,3)        null,
   sizeId               VARCHAR(45)          not null,
   sizeName             VARCHAR(45)          not null,
   modelSize            VARCHAR(45)          null,
   typeId               VARCHAR(45)          null,
   userType             VARCHAR(45)          null,
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
   meterLoc             POINT                null,
   lastCalib            TIMESTAMP WITH TIME ZONE null,
   memo                 VARCHAR(45)          null,
   createBy             VARCHAR(45)          null,
   createDate           TIMESTAMP WITH TIME ZONE null,
   updateBy             VARCHAR(45)          null,
   updateDate           TIMESTAMP WITH TIME ZONE null,
   constraint PK_BWMETER primary key (meterId)
);

/*==============================================================*/
/* Index: idx_meter_code                                        */
/*==============================================================*/
create  index idx_meter_code on BwMeter (
firmId,
meterCode
);

/*==============================================================*/
/* Index: idx_meter_user                                        */
/*==============================================================*/
create  index idx_meter_user on BwMeter (
firmId,
userCode
);

/*==============================================================*/
/* Index: idx_meter_ext                                         */
/*==============================================================*/
create  index idx_meter_ext on BwMeter (
firmId,
extId
);

/*==============================================================*/
/* Index: idx_meter_firm                                        */
/*==============================================================*/
create  index idx_meter_firm on BwMeter (
firmId
);

/*==============================================================*/
/* Index: idx_meter_steel                                       */
/*==============================================================*/
create  index idx_meter_steel on BwMeter (
steelNo
);

/*==============================================================*/
/* Table: BwRight                                               */
/*==============================================================*/
create table BwRight (
   rightName            VARCHAR(45)          not null,
   rightDesc            VARCHAR(45)          null,
   preInit              BOOL                 null,
   constraint PK_BWRIGHT primary key (rightName)
);

/*==============================================================*/
/* Table: BwRole                                                */
/*==============================================================*/
create table BwRole (
   roleName             VARCHAR(45)          not null,
   roleDesc             VARCHAR(45)          null,
   preInit              BOOL                 null,
   constraint PK_BWROLE primary key (roleName)
);

/*==============================================================*/
/* Table: BwRoleRight                                           */
/*==============================================================*/
create table BwRoleRight (
   roleName             VARCHAR(45)          not null,
   rightName            VARCHAR(45)          not null,
   constraint PK_BWROLERIGHT primary key (roleName, rightName)
);

/*==============================================================*/
/* Table: BwUser                                                */
/*==============================================================*/
create table BwUser (
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
   lastLoginLoc         POINT                null,
   loginCount           INT4                 null,
   lastLoginTime        TIMESTAMP WITH TIME ZONE null,
   lastOperTime         TIMESTAMP WITH TIME ZONE null,
   lastOperIp           VARCHAR(200)         null,
   lastOperLoc          POINT                null,
   createBy             VARCHAR(45)          null,
   createDate           TIMESTAMP WITH TIME ZONE null,
   updateBy             VARCHAR(45)          null,
   updateDate           TIMESTAMP WITH TIME ZONE null,
   constraint PK_BWUSER primary key (userId)
);

/*==============================================================*/
/* Table: BwUserLogin                                           */
/*==============================================================*/
create table BwUserLogin (
   wid                  SERIAL not null,
   sessionId            VARCHAR(200)         not null,
   userId               VARCHAR(45)          not null,
   loginTime            TIMESTAMP WITH TIME ZONE not null,
   loginIp              VARCHAR(200)         null,
   loginHost            VARCHAR(200)         null,
   loginAddr            VARCHAR(200)         null,
   loginLoc             POINT                null,
   logoutTime           TIMESTAMP WITH TIME ZONE null,
   logoutIp             VARCHAR(200)         null,
   logoutLoc            VARCHAR(200)         null,
   ipAddr               VARCHAR(200)         null,
   devId                VARCHAR(200)         null,
   shareSalt            VARCHAR(45)          null,
   constraint PK_BWUSERLOGIN primary key (wid)
);

/*==============================================================*/
/* Index: idx_sid                                               */
/*==============================================================*/
create  index idx_sid on BwUserLogin (
sessionId
);

/*==============================================================*/
/* Index: idx_login_user                                        */
/*==============================================================*/
create  index idx_login_user on BwUserLogin (
userId,
loginTime
);

/*==============================================================*/
/* Table: BwUserOper                                            */
/*==============================================================*/
create table BwUserOper (
   operId               SERIAL not null,
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
   loginLoc             POINT                null,
   clientIp             VARCHAR(45)          null,
   serverIp             VARCHAR(45)          null,
   operCity             VARCHAR(45)          null,
   operResult           VARCHAR(45)          null,
   operCount            INT4                 null,
   operDesc             VARCHAR(200)         null,
   constraint PK_BWUSEROPER primary key (operId)
);

/*==============================================================*/
/* Index: idx_oper_user                                         */
/*==============================================================*/
create  index idx_oper_user on BwUserOper (
firmId,
userId,
operTime
);

/*==============================================================*/
/* Index: idx_oper_ip                                           */
/*==============================================================*/
create  index idx_oper_ip on BwUserOper (
operCity,
loginIp,
operTime
);

/*==============================================================*/
/* Index: idx_oper_role                                         */
/*==============================================================*/
create  index idx_oper_role on BwUserOper (
operCase,
operRole,
operTime
);

/*==============================================================*/
/* Index: idx_oper_time                                         */
/*==============================================================*/
create  index idx_oper_time on BwUserOper (
operTime,
firmId,
operRole,
loginIp
);

/*==============================================================*/
/* Table: BwUserRole                                            */
/*==============================================================*/
create table BwUserRole (
   userId               VARCHAR(45)          not null,
   roleName             VARCHAR(45)          not null,
   constraint PK_BWUSERROLE primary key (userId, roleName)
);

/*==============================================================*/
/* Table: BwZone                                                */
/*==============================================================*/
create table BwZone (
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
   zoneLoc              POINT                null,
   zoneRegion           POLYGON              null,
   firmId               VARCHAR(45)          not null,
   leafable             INT4                 null,
   createBy             VARCHAR(45)          null,
   createDate           TIMESTAMP WITH TIME ZONE null,
   updateBy             VARCHAR(45)          null,
   updateDate           TIMESTAMP WITH TIME ZONE null,
   constraint PK_BWZONE primary key (zoneId)
);

/*==============================================================*/
/* Index: idx_zone_firm                                         */
/*==============================================================*/
create  index idx_zone_firm on BwZone (
firmId
);

/*==============================================================*/
/* Table: BwZoneMeter                                           */
/*==============================================================*/
create table BwZoneMeter (
   zoneId               VARCHAR(45)          not null,
   meterId              VARCHAR(45)          not null,
   inOutput             INT4                 not null,
   constraint PK_BWZONEMETER primary key (zoneId, meterId)
);

/*==============================================================*/
/* Table: vc_code                                               */
/*==============================================================*/
create table vc_code (
   codeId               varchar(20)          not null,
   codeName             varchar(40)          null,
   memo                 varchar(200)         null,
   preInit              BOOL                 null,
   constraint PK_VC_CODE primary key (codeId)
);

/*==============================================================*/
/* Table: vc_code_value                                         */
/*==============================================================*/
create table vc_code_value (
   codeId               varchar(20)          not null,
   valueId              varchar(20)          not null,
   valueName            varchar(40)          null,
   valueOrder           int                  null,
   valueType            varchar(20)          null,
   preInit              BOOL                 null,
   disabled             BOOL                 null,
   constraint PK_VC_CODE_VALUE primary key (codeId, valueId)
);

/*==============================================================*/
/* Table: vc_factory_meter_model                                */
/*==============================================================*/
create table vc_factory_meter_model (
   factId               varchar(2)           not null,
   typeId               varchar(2)           not null,
   modelSize            varchar(100)         not null,
   modelList            varchar(500)         null,
   preInit              BOOL                 null default true,
   constraint PK_VC_FACTORY_METER_MODEL primary key (factId, typeId, modelSize)
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
   createBy             varchar(45)          null,
   createDate           TIMESTAMP WITH TIME ZONE null,
   updateBy             varchar(45)          null,
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
   wid                  SERIAL not null,
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
pointNo,
meterId,
batchId
);

/*==============================================================*/
/* Table: vc_meter_verify_result                                */
/*==============================================================*/
create table vc_meter_verify_result (
   wid                  SERIAL not null,
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
   sizeId               varchar(20)          null,
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
/* Table: vc_workday_holiday                                    */
/*==============================================================*/
create table vc_workday_holiday (
   wid                  SERIAL not null,
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

