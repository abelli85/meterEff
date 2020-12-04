
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

TRUNCATE TABLE vc_meter_factory;

/**
  "01-宁波水表股份有限公司
02-申舒斯仪表系统（福州）有限公司
03-杭州山科智能科技股份有限公司
04-深圳市星源鼎新科技有限公司
05-浙江和达科技股份有限公司
06-三川智慧科技股份有限公司
07-宁波东海集团有限公司
08-重庆智慧水务有限公司
09-杭州水表股份有限公司"
 */
INSERT INTO vc_meter_factory(factId, factName, createBy, createDate) VALUES
('01', '宁波水表股份有限公司', 'robot', timestamptz '2020-8-19'),
('02', '申舒斯仪表系统（福州）有限公司', 'robot', timestamptz '2020-8-19'),
('03', '杭州山科智能科技股份有限公司', 'robot', timestamptz '2020-8-19'),
('04', '深圳市兴源鼎新科技有限公司', 'robot', timestamptz '2020-8-19'),
('05', '浙江和达科技股份有限公司', 'robot', timestamptz '2020-8-19'),
('06', '三川智慧科技股份有限公司', 'robot', timestamptz '2020-8-19'),
('07', '宁波东海集团有限公司', 'robot', timestamptz '2020-8-19'),
('08', '重庆智慧水务有限公司', 'robot', timestamptz '2020-8-19'),
('09', '杭州水表股份有限公司', 'robot', timestamptz '2020-8-19'),
('11', '拓安信', 'robot', '2020-12-1'::timestamptz),
('12', '爱知时计', 'robot', '2020-12-1'::timestamptz);

TRUNCATE TABLE vc_factory_meter_model;
TRUNCATE TABLE vc_meter_type;
INSERT INTO vc_meter_type(typeId, typeName) VALUES
('1', '普通'), ('2', '宽量程');
INSERT INTO vc_factory_meter_model(factId, typeId, modelSize, sizeId, valueOrder, preInit) VALUES
('01', '1', 'LXS', 40, 10, true),
('01', '1', 'WS', 50, 20, true),
('01', '1', 'LXLC', 80, 30, true),
('01', '1', 'LXLC', 100, 40, true),
('01', '1', 'LXLC', 150, 50, true),
('01', '1', 'LXLC', 200, 60, true),
('01', '1', 'LXLC', 300, 70, true),
('01', '1', 'WPD', 50, 80, true),
('01', '1', 'WPD', 80, 90, true),
('01', '1', 'WPD', 100, 100, true),
('01', '1', 'WPD', 150, 110, true),
('01', '1', 'WPD', 200, 120, true),
('01', '1', 'WPD', 300, 130, true),
('02', '1', '420PC', 40, 140, true),
('02', '1', 'WSD', 50, 150, true),
('02', '1', 'WPD', 50, 160, true),
('02', '1', 'WPD', 80, 170, true),
('02', '1', 'WPD', 100, 180, true),
('02', '1', 'WPD', 150, 190, true),
('02', '1', 'WPD', 200, 200, true),
('02', '1', 'WPD', 300, 210, true),
('02', '1', 'MSP', 50, 220, true),
('02', '1', 'MSP', 80, 230, true),
('02', '1', 'MSP', 100, 240, true),
('02', '1', 'MSP', 150, 250, true),
('02', '1', 'MS', 50, 260, true),
('02', '1', 'MS', 80, 270, true),
('02', '1', 'MS', 100, 280, true),
('02', '1', 'MS', 150, 290, true),
('04', '1', 'LXS', 40, 300, true),
('04', '1', 'WS', 50, 310, true),
('04', '1', 'LXLC', 80, 320, true),
('04', '1', 'LXLC', 100, 330, true),
('04', '1', 'LXLC', 150, 340, true),
('04', '1', 'LXLC', 200, 350, true),
('04', '1', 'LXLC', 300, 360, true),
('04', '1', 'WPD', 50, 370, true),
('04', '1', 'WPD', 80, 380, true),
('04', '1', 'WPD', 100, 390, true),
('04', '1', 'WPD', 150, 400, true),
('04', '1', 'WPD', 200, 410, true),
('04', '1', 'WPD', 300, 420, true),
('07', '1', 'LXSY', 40, 430, true),
('07', '1', 'LXRY', 50, 440, true),
('07', '1', 'LXLKY', 80, 450, true),
('07', '1', 'LXLKY', 100, 460, true),
('07', '1', 'LXLKY', 150, 470, true),
('07', '1', 'LXLKY', 200, 480, true),
('07', '1', 'LXLKY', 300, 490, true),
('11', '1', 'MAG-AX', 80, 500, true),
('11', '1', 'MAG-AX', 100, 510, true),
('11', '1', 'MAG-AX', 150, 520, true),
('11', '1', 'MAG-AX', 200, 530, true),
('11', '1', 'MAG-AX', 300, 540, true),
('12', '1', 'SU', 80, 550, true),
('12', '1', 'SU', 100, 560, true),
('12', '1', 'SU', 150, 570, true),
('12', '1', 'SU', 200, 580, true),
('12', '1', 'SU', 300, 590, true);

-- </editor-fold>