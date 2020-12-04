
-- <editor-fold desc='factory-model-size'>

drop index idx_factory_meter_model_order;

drop table vc_factory_meter_model;


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

-- </editor-fold>