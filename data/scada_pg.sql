/*==============================================================*/
/* DBMS name:      PostgreSQL 9.x                               */
/* Created on:     2021/2/5 16:59:05                            */
/*==============================================================*/


drop index idx_scada_auth_client_auto;

drop index idx_scada_auth_client_time;

drop index idx_scada_auth_resp;

drop index idx_scada_auth_req;

drop table scada.scada_auth_list;

drop index idx_scada_auth_refresh_auto;

drop index idx_scada_auth_refresh_time;

drop index idx_scada_refresh_resp;

drop index idx_scada_refresh_req;

drop table scada.scada_auth_refresh;

drop index idx_scada_hist_auto;

drop index idx_scada_hist_code;

drop table scada.scada_hist;

drop index idx_scada_hist_start;

drop index idx_scada_hist_req;

drop table scada.scada_hist_req;

drop index idx_scada_real_auto;

drop index idx_scada_real_stat;

drop table scada.scada_realtime;

drop index idx_scada_stat_state;

drop index idx_scada_stat_group;

drop index idx_scada_stat_code;

drop table scada.scada_stat;

drop index idx_scada_stat_real_msn;

drop index idx_scada_stat_real_group;

drop index idx_scada_stat_real_code;

drop table scada.scada_stat_real_data;

drop index idx_scada_stat_sensor_code;

drop table scada.scada_stat_real_sensor;

drop index idx_scada_stat_req;

drop table scada.scada_stat_req;

/*==============================================================*/
/* Table: scada_auth_list                                       */
/*==============================================================*/
create table scada.scada_auth_list (
   authAuto             SERIAL8              not null,
   reqTime              TIMESTAMP WITH TIME ZONE not null,
   grantType            VARCHAR(60)          null,
   clientId             VARCHAR(60)          null,
   clientSecret         VARCHAR(160)         null,
   respTime             TIMESTAMP WITH TIME ZONE null,
   statusCode           INT4                 null,
   retFine              BOOL                 not null,
   tokenType            VARCHAR(60)          null,
   accessToken          VARCHAR(160)         null,
   refreshToken         VARCHAR(160)         null,
   expiresIn            INT8                 null,
   respText             JSON                 null,
   constraint PK_SCADA_AUTH_LIST primary key (authAuto)
);

/*==============================================================*/
/* Index: idx_scada_auth_req                                    */
/*==============================================================*/
create  index idx_scada_auth_req on scada_auth_list (
reqTime
);

/*==============================================================*/
/* Index: idx_scada_auth_resp                                   */
/*==============================================================*/
create  index idx_scada_auth_resp on scada_auth_list (
respTime
);

/*==============================================================*/
/* Index: idx_scada_auth_client_time                            */
/*==============================================================*/
create  index idx_scada_auth_client_time on scada_auth_list (
clientId,
reqTime
);

/*==============================================================*/
/* Index: idx_scada_auth_client_auto                            */
/*==============================================================*/
create  index idx_scada_auth_client_auto on scada_auth_list (
clientId,
authAuto
);

/*==============================================================*/
/* Table: scada_auth_refresh                                    */
/*==============================================================*/
create table scada.scada_auth_refresh (
   refreshAuto          SERIAL8              not null,
   grantType            VARCHAR(60)          null,
   clientId             VARCHAR(60)          null,
   clientSecret         VARCHAR(160)         null,
   refreshToken1        VARCHAR(160)         null,
   requestTime          TIMESTAMP WITH TIME ZONE null,
   tokenType            VARCHAR(60)          null,
   accessToken          VARCHAR(160)         null,
   refreshToken2        VARCHAR(160)         null,
   expiresIn            INT8                 null,
   responseTime         TIMESTAMP WITH TIME ZONE null,
   constraint PK_SCADA_AUTH_REFRESH primary key (refreshAuto)
);

/*==============================================================*/
/* Index: idx_scada_refresh_req                                 */
/*==============================================================*/
create  index idx_scada_refresh_req on scada_auth_refresh (
requestTime
);

/*==============================================================*/
/* Index: idx_scada_refresh_resp                                */
/*==============================================================*/
create  index idx_scada_refresh_resp on scada_auth_refresh (
responseTime
);

/*==============================================================*/
/* Index: idx_scada_auth_refresh_time                           */
/*==============================================================*/
create  index idx_scada_auth_refresh_time on scada_auth_refresh (
clientId,
requestTime
);

/*==============================================================*/
/* Index: idx_scada_auth_refresh_auto                           */
/*==============================================================*/
create  index idx_scada_auth_refresh_auto on scada_auth_refresh (
clientId,
refreshAuto
);

/*==============================================================*/
/* Table: scada_hist                                            */
/*==============================================================*/
create table scada.scada_hist (
   fauto                SERIAL8              not null,
   fcode                VARCHAR(100)         null,
   ftime                TIMESTAMP WITH TIME ZONE null,
   fvalue               VARCHAR(100)         null,
   dtype                VARCHAR(20)          null default 'TOTAL',
   createDate           TIMESTAMP WITH TIME ZONE null default CURRENT_TIMESTAMP,
   updateDate           TIMESTAMP WITH TIME ZONE null default CURRENT_TIMESTAMP,
   fts                  INT8                 null,
   ftype                VARCHAR(100)         null,
   constraint PK_SCADA_HIST primary key (fauto)
);

comment on column scada_hist.dtype is
'TOTAL/AVG/REAL/DELTA';

/*==============================================================*/
/* Index: idx_scada_hist_code                                   */
/*==============================================================*/
create  index idx_scada_hist_code on scada_hist (
fcode,
ftime
);

/*==============================================================*/
/* Index: idx_scada_hist_auto                                   */
/*==============================================================*/
create  index idx_scada_hist_auto on scada_hist (
fcode,
fauto
);

/*==============================================================*/
/* Table: scada_hist_req                                        */
/*==============================================================*/
create table scada.scada_hist_req (
   reqAuto              SERIAL8              not null,
   codes                JSON                 null,
   dataStartTime        TIMESTAMP WITH TIME ZONE null,
   dataEndTime          TIMESTAMP WITH TIME ZONE null,
   reqTime              TIMESTAMP WITH TIME ZONE not null,
   respTime             TIMESTAMP WITH TIME ZONE null,
   statusCode           INT4                 null,
   retFine              BOOL                 null,
   dataCount            INT4                 null,
   respText             JSON                 null,
   constraint PK_SCADA_HIST_REQ primary key (reqAuto)
);

/*==============================================================*/
/* Index: idx_scada_hist_req                                    */
/*==============================================================*/
create  index idx_scada_hist_req on scada_hist_req (
reqTime
);

/*==============================================================*/
/* Index: idx_scada_hist_start                                  */
/*==============================================================*/
create  index idx_scada_hist_start on scada_hist_req (
dataStartTime,
dataEndTime
);

/*==============================================================*/
/* Table: scada_realtime                                        */
/*==============================================================*/
create table scada.scada_realtime (
   fauto                SERIAL8              not null,
   fstation             VARCHAR(100)         null,
   fcode                VARCHAR(100)         null,
   ftime                TIMESTAMP WITH TIME ZONE null,
   fvalue               VARCHAR(100)         null,
   dtype                VARCHAR(20)          null default 'TOTAL',
   ftype                VARCHAR(100)         null,
   createDate           TIMESTAMP WITH TIME ZONE null default CURRENT_TIMESTAMP,
   updateDate           TIMESTAMP WITH TIME ZONE null default CURRENT_TIMESTAMP,
   fts                  INT8                 null,
   constraint PK_SCADA_REALTIME primary key (fauto)
);

comment on column scada_realtime.dtype is
'TOTAL/AVG/REAL/DELTA';

comment on column scada_realtime.ftype is
'tag';

/*==============================================================*/
/* Index: idx_scada_real_stat                                   */
/*==============================================================*/
create  index idx_scada_real_stat on scada_realtime (
fstation,
fcode,
ftime
);

/*==============================================================*/
/* Index: idx_scada_real_auto                                   */
/*==============================================================*/
create  index idx_scada_real_auto on scada_realtime (
fstation,
fcode,
fauto
);

/*==============================================================*/
/* Table: scada_stat                                            */
/*==============================================================*/
create table scada.scada_stat (
   statAuto             SERIAL8              not null,
   fgroup               VARCHAR(100)         null,
   fcode                VARCHAR(100)         null,
   fname                VARCHAR(100)         null,
   sname                VARCHAR(100)         null,
   dtype                VARCHAR(20)          null default 'TOTAL',
   mty                  VARCHAR(100)         null,
   "position"           VARCHAR(100)         null,
   sensors              JSON                 null,
   statWork             VARCHAR(16)          null default 'WORK',
   createDate           TIMESTAMP WITH TIME ZONE null default CURRENT_TIMESTAMP,
   updateDate           TIMESTAMP WITH TIME ZONE null default CURRENT_TIMESTAMP,
   meterBrandId         VARCHAR(45)          null,
   sizeId               INT4                 not null default 0,
   sizeName             VARCHAR(45)          null,
   modelSize            VARCHAR(45)          null,
   constraint PK_SCADA_STAT primary key (statAuto)
);

comment on column scada_stat.dtype is
'TOTAL/AVG/REAL/DELTA';

comment on column scada_stat.statWork is
'WORK/PAUSE/RETIRED/RESERVED';

/*==============================================================*/
/* Index: idx_scada_stat_code                                   */
/*==============================================================*/
create  index idx_scada_stat_code on scada_stat (
fcode
);

/*==============================================================*/
/* Index: idx_scada_stat_group                                  */
/*==============================================================*/
create  index idx_scada_stat_group on scada_stat (
fgroup,
fcode
);

/*==============================================================*/
/* Index: idx_scada_stat_state                                  */
/*==============================================================*/
create  index idx_scada_stat_state on scada_stat (
statWork,
fcode
);

/*==============================================================*/
/* Table: scada_stat_real_data                                  */
/*==============================================================*/
create table scada.scada_stat_real_data (
   fauto                SERIAL8              not null,
   fid                  VARCHAR(100)         null,
   fstation             VARCHAR(100)         null,
   ftime                TIMESTAMP WITH TIME ZONE null,
   fgroup               VARCHAR(100)         null,
   msn                  VARCHAR(100)         null,
   createDate           TIMESTAMP WITH TIME ZONE null default CURRENT_TIMESTAMP,
   updateDate           TIMESTAMP WITH TIME ZONE null default CURRENT_TIMESTAMP,
   fmemo                VARCHAR(100)         null,
   constraint PK_SCADA_STAT_REAL_DATA primary key (fauto)
);

/*==============================================================*/
/* Index: idx_scada_stat_real_code                              */
/*==============================================================*/
create  index idx_scada_stat_real_code on scada_stat_real_data (
fid,
ftime
);

/*==============================================================*/
/* Index: idx_scada_stat_real_group                             */
/*==============================================================*/
create  index idx_scada_stat_real_group on scada_stat_real_data (
fgroup,
msn,
ftime
);

/*==============================================================*/
/* Index: idx_scada_stat_real_msn                               */
/*==============================================================*/
create  index idx_scada_stat_real_msn on scada_stat_real_data (
msn,
ftime
);

/*==============================================================*/
/* Table: scada_stat_real_sensor                                */
/*==============================================================*/
create table scada.scada_stat_real_sensor (
   sensorAuto           SERIAL8              not null,
   sauto                INT8                 not null,
   scode                VARCHAR(100)         null,
   stype                VARCHAR(100)         null,
   sname                VARCHAR(100)         null,
   stime                TIMESTAMP WITH TIME ZONE null,
   sval                 VARCHAR(100)         null,
   sunit                VARCHAR(100)         null,
   sprec                VARCHAR(100)         null,
   salarm               JSON                 null,
   constraint PK_SCADA_STAT_REAL_SENSOR primary key (sensorAuto)
);

/*==============================================================*/
/* Index: idx_scada_stat_sensor_code                            */
/*==============================================================*/
create  index idx_scada_stat_sensor_code on scada_stat_real_sensor (
scode,
stime
);

/*==============================================================*/
/* Table: scada_stat_req                                        */
/*==============================================================*/
create table scada.scada_stat_req (
   reqAuto              SERIAL8              not null,
   accessToken          VARCHAR(160)         null,
   fgroup               VARCHAR(100)         null,
   fmodel               VARCHAR(100)         null,
   sensor               BOOL                 null,
   reqTime              TIMESTAMP WITH TIME ZONE not null,
   respTime             TIMESTAMP WITH TIME ZONE null,
   statusCode           INT4                 null,
   retFine              BOOL                 not null,
   dataCount            INT4                 null,
   respText             JSON                 null,
   constraint PK_SCADA_STAT_REQ primary key (reqAuto)
);

/*==============================================================*/
/* Index: idx_scada_stat_req                                    */
/*==============================================================*/
create  index idx_scada_stat_req on scada_stat_req (
reqTime
);

