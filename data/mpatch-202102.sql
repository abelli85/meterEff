-- <editor-fold desc='meff-optimization'>

alter table bw_data add column
    dtype                VARCHAR(20)          null default 'TOTAL';
alter table bw_data add column
    createDate           TIMESTAMP WITH TIME ZONE null default CURRENT_TIMESTAMP;
alter table bw_data add column
    updateDate           TIMESTAMP WITH TIME ZONE null default CURRENT_TIMESTAMP;

-- alter table bw_data alter column dataId type int8;

-- select max(dataId) from bw_data;
-- create sequence bw_data_dataid_seq8 start with max_dataId_from_data;
-- alter table bw_data alter column dataId set default null;
-- alter table bw_data alter column dataId set default nextval('bw_data_dataid_seq8' ::regclass);
-- drop sequence bw_data_dataid_seq;

comment on column bw_data.dtype is
    'TOTAL/AVG/REAL/DELTA';

-- drop index idx_data_szid;
-- very slow because of large data. it may cost longer than 1 quarter.
/*==============================================================*/
/* Index: idx_data_szid                                         */
/*==============================================================*/
create  index idx_data_szid on bw_data (
                                        firmId,
                                        szid
    );

/*==============================================================*/
/* Index: idx_data_create                                       */
/*==============================================================*/
/* DO NOT APPLY just now, because it costs too long time! */
/*
create  index idx_data_create on bw_data (
                                          createDate,
                                          extId,
                                          sampleTime
    );
*/

-- drop index idx_data_ext_dtype;

/*==============================================================*/
/* Index: idx_data_ext_dtype                                    */
/*==============================================================*/
create  index idx_data_ext_dtype on bw_data (
                                             extId,
                                             dtype,
                                             sampleTime
    );

alter table bw_dma_meter add column
    childType            VARCHAR(20)          null default 'PARENT';

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

alter table bw_meter add column
    dtype                VARCHAR(20)          null default 'TOTAL';

comment on column bw_meter.dtype is
    'TOTAL/AVG/REAL/DELTA';

/*==============================================================*/
/* Index: idx_meter_dtype                                       */
/*==============================================================*/
create  index idx_meter_dtype on bw_meter (
                                           dtype,
                                           meterCode
    );

alter table bw_zone_meter add column
    childType            VARCHAR(20)          null default 'PARENT';

comment on column bw_zone_meter.childType is
    'PARENT/CHILD';

-- </editor-fold>

-- <editor-fold desc='scada'>
alter table scada.scada_stat add column
meterBrandId         VARCHAR(45)          null;
alter table scada.scada_stat add column
   sizeId               INT4                 not null default 0;
alter table scada.scada_stat add column
   sizeName             VARCHAR(45)          null;
alter table scada.scada_stat add column
   modelSize            VARCHAR(45)          null;

-- </editor-fold>