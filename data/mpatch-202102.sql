alter table bw_data add column
    dtype                VARCHAR(20)          null default 'TOTAL';
alter table bw_data add column
    createDate           TIMESTAMP WITH TIME ZONE null default CURRENT_TIMESTAMP;
alter table bw_data add column
    updateDate           TIMESTAMP WITH TIME ZONE null default CURRENT_TIMESTAMP;

comment on column bw_data.dtype is
    'TOTAL/AVG/REAL/DELTA';

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

