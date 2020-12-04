-- platform operator.

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

/*
INSERT INTO vc_factory_meter_model(factId, typeId, modelSize, modelList) VALUES
('01', '1', 'LXS', 'LXS、LXSY、LXLC'), 
('01', '1', 'LXSY', 'LXS、LXSY、LXLC'), 
('01', '1', 'LXLC', 'LXS、LXSY、LXLC'), 

('01', '2', 'WPD', 'WPD、LXF、WS'),
('01', '2', 'LXF', 'WPD、LXF、WS'),
('01', '2', 'WS', 'WPD、LXF、WS'),

('02', '2', 'WPD', 'WPD、MSP、 420PC、 WSD'),
('02', '2', 'MSP', 'WPD、MSP、 420PC、 WSD'),
('02', '2', '420PC', 'WPD、MSP、 420PC、 WSD'),
('02', '2', 'WSD', 'WPD、MSP、 420PC、 WSD'),

('03', '1', 'LXSG', 'LXSG'),

('04', '1', 'LXS', 'LXS、LXSY、LXLC'),
('04', '1', 'LXSY', 'LXS、LXSY、LXLC'),
('04', '1', 'LXLC', 'LXS、LXSY、LXLC'),
('04', '2', 'WPD', 'WPD'),

('05', '1', 'LXS', 'LXS、LXSY、LXLC'),
('05', '1', 'LXSY', 'LXS、LXSY、LXLC'),
('05', '1', 'LXLC', 'LXS、LXSY、LXLC'),

('06', '1', 'LXS', 'LXS、LXSY'),
('06', '1', 'LXSY', 'LXS、LXSY'),

('07', '1', 'LXSX', 'LXSX、LXSY、LXRY、LXLKY'),
('07', '1', 'LXSY', 'LXSX、LXSY、LXRY、LXLKY'),
('07', '1', 'LXRY', 'LXSX、LXSY、LXRY、LXLKY'),
('07', '1', 'LXLKY', 'LXSX、LXSY、LXRY、LXLKY'),

('08', '1', 'LXY', 'LXY'),

('09', '2', '420PC', '420PC、 WSD'),
('09', '2', 'WSD', '420PC、 WSD');

-- 补充
INSERT INTO vc_factory_meter_model(factId, typeId, modelSize, modelList) VALUES
('01', '1', 'LXS-15F', 'LXS、LXSY、LXLC');
*/

TRUNCATE TABLE vc_code_value;
TRUNCATE TABLE vc_code;

INSERT INTO vc_code(codeId, codeName, memo, preInit, createBy, createDate) VALUES
('SIZE', '水表口径', 'DN15~400', true, 'robot', timestamptz '2020-8-19'),
('VERIFY_REASON', '作业原因', '', true, 'robot', timestamptz '2020-8-19'),
('VERIFY_TYPE', '作业类型', '', true, 'robot', timestamptz '2020-8-19'),
('REPORT_TYPE', '报告类型', '只显示不允许添加', true, 'robot', timestamptz '2020-8-19'),
('BRAND', '水表品牌', '', true, 'robot', timestamptz '2020-8-19'),
('METER_CLASS', '水表类型', '不同于水表分类', true, 'robot', timestamptz '2020-8-19'),
('ACCURACY_GRADE', '准确精度等级', '', true, 'robot', timestamptz '2020-8-19'),
('TEMP_GRADE', '温度等级', '', true, 'robot', timestamptz '2020-8-19'),
('MAP_GRADE', 'MAP', '', true, 'robot', timestamptz '2020-8-19'),
('ABNORMAL_REASON', '异常原因', '', true, 'robot', timestamptz '2020-8-19'),
('BATCH', '批次状态', '只显示不允许添加', true, 'robot', timestamptz '2020-8-19');

DELETE FROM vc_code_value where codeId = 'SIZE';
INSERT INTO vc_code_value(codeId, valueId, valueName, valueOrder, valueType, preInit, disabled, createBy, createDate) VALUES
('SIZE', '15', 'DN15',  15, 'INT', true, false, 'robot', timestamptz '2020-8-19'),
('SIZE', '20', 'DN20',  20, 'INT', true, false, 'robot', timestamptz '2020-8-19'),
('SIZE', '25', 'DN25',  25, 'INT', true, false, 'robot', timestamptz '2020-8-19'),
('SIZE', '40', 'DN40',  40, 'INT', true, false, 'robot', timestamptz '2020-8-19'),
('SIZE', '50', 'DN50',  50, 'INT', true, false, 'robot', timestamptz '2020-8-19'),
('SIZE', '80', 'DN80',  80, 'INT', true, false, 'robot', timestamptz '2020-8-19'),
('SIZE', '90', 'DN100', 90, 'INT', true, true, 'robot', timestamptz '2020-8-19'),
('SIZE', '91', 'DN150', 91, 'INT', true, true, 'robot', timestamptz '2020-8-19'),
('SIZE', '92', 'DN200', 92, 'INT', true, true, 'robot', timestamptz '2020-8-19'),
('SIZE', '93', 'DN300', 93, 'INT', true, true, 'robot', timestamptz '2020-8-19'),
('SIZE', '94', 'DN400', 94, 'INT', true, true, 'robot', timestamptz '2020-8-19'),
('SIZE', '95', 'DN250', 95, 'INT', true, true, 'robot', timestamptz '2020-8-19'),
('SIZE', 'DN15',  'DN15',  115, 'INT', true, true, 'robot', timestamptz '2020-8-19'),
('SIZE', 'DN20',  'DN20',  120, 'INT', true, true, 'robot', timestamptz '2020-8-19'),
('SIZE', 'DN25',  'DN25',  125, 'INT', true, true, 'robot', timestamptz '2020-8-19'),
('SIZE', 'DN40',  'DN40',  140, 'INT', true, true, 'robot', timestamptz '2020-8-19'),
('SIZE', 'DN50',  'DN50',  150, 'INT', true, true, 'robot', timestamptz '2020-8-19'),
('SIZE', 'DN80',  'DN80',  180, 'INT', true, true, 'robot', timestamptz '2020-8-19'),
('SIZE', 'DN100', 'DN100', 190, 'INT', true, true, 'robot', timestamptz '2020-8-19'),
('SIZE', 'DN150', 'DN150', 191, 'INT', true, true, 'robot', timestamptz '2020-8-19'),
('SIZE', 'DN200', 'DN200', 192, 'INT', true, true, 'robot', timestamptz '2020-8-19'),
('SIZE', 'DN300', 'DN300', 193, 'INT', true, true, 'robot', timestamptz '2020-8-19'),
('SIZE', 'DN400', 'DN400', 194, 'INT', true, true, 'robot', timestamptz '2020-8-19'),
('SIZE', 'DN250', 'DN250', 195, 'INT', true, true, 'robot', timestamptz '2020-8-19'),
('SIZE', '100', 'DN100', 190, 'INT', true, false, 'robot', timestamptz '2020-8-19'),
('SIZE', '150', 'DN150', 191, 'INT', true, false, 'robot', timestamptz '2020-8-19'),
('SIZE', '200', 'DN200', 192, 'INT', true, false, 'robot', timestamptz '2020-8-19'),
('SIZE', '300', 'DN300', 193, 'INT', true, false, 'robot', timestamptz '2020-8-19'),
('SIZE', '400', 'DN400', 194, 'INT', true, false, 'robot', timestamptz '2020-8-19'),
('SIZE', '250', 'DN250', 195, 'INT', true, false, 'robot', timestamptz '2020-8-19');

/**
  安装前首检、大表后续检定、争议检定、使用中抽检、水表招标，实验室比对，期间核查，重复性试验，稳定性考核，计量研究
 */
INSERT INTO vc_code_value(codeId, valueId, valueName, valueOrder, valueType, preInit) VALUES
('VERIFY_REASON', 'FIRST', '安装前首检', 15, 'VARCHAR', true),
('VERIFY_REASON', 'BIGFOLLOW', '大表后续检定', 25, 'VARCHAR', true),
('VERIFY_REASON', 'ARGUE', '争议检定', 35, 'VARCHAR', true),
('VERIFY_REASON', 'USING', '使用中抽检', 45, 'VARCHAR', true),
('VERIFY_REASON', 'BID', '水表招标', 55, 'VARCHAR', true),
('VERIFY_REASON', 'COMPARE', '实验室比对', 65, 'VARCHAR', true),
('VERIFY_REASON', 'AUDIT', '期间核查', 75, 'VARCHAR', true),
('VERIFY_REASON', 'AGAIN', '重复性试验', 85, 'VARCHAR', true),
('VERIFY_REASON', 'STABLE', '稳定性考核', 95, 'VARCHAR', true),
('VERIFY_REASON', 'STUDY', '计量研究', 105, 'VARCHAR', true)
;

/**
  作业类型:
  首次检定、后续检定、水表测试、使用中检查
 */
INSERT INTO vc_code_value(codeId, valueId, valueName, valueOrder, valueType, preInit) VALUES
('VERIFY_TYPE', 'FIRST', '首检', 10, 'VARCHAR', true),
('VERIFY_TYPE', 'FOLLOWING', '后续检定', 20, 'VARCHAR', true),
('VERIFY_TYPE', 'TEST', '水表测试', 30, 'VARCHAR', true),
('VERIFY_TYPE', 'USING', '使用中检定', 40, 'VARCHAR', true)
-- ('VERIFY_TYPE', 'STUDY', '计量研究', 50, 'VARCHAR', true),
-- ('VERIFY_TYPE', 'PERIOD', '周期检定', 60, 'VARCHAR', true),
-- ('VERIFY_TYPE', 'BID', '招标检定', 70, 'VARCHAR', true)
;

/**
  "检定报告
测试报告
检定报告和测试报告"
 */
INSERT INTO vc_code_value(codeId, valueId, valueName, valueOrder, valueType, preInit) VALUES
('REPORT_TYPE', 'VERIFY_ONLY', '检定报告', 10, 'VARCHAR', true),
('REPORT_TYPE', 'TEST_ONLY', '测试报告', 20, 'VARCHAR', true),
('REPORT_TYPE', 'TEST_VERIFY', '检定报告和测试报告', 40, 'VARCHAR', true)
;

/**
  "01-宁波水表
02-申舒斯
03-杭州山科
04兴源鼎新
05-浙江和达
06-三川智慧
07-宁波东海
08-重庆智慧
09-杭州水表"
 */
INSERT INTO vc_code_value(codeId, valueId, valueName, valueOrder, valueType, preInit) VALUES
('BRAND', '01', '宁波水表', 10, 'VARCHAR', true),
('BRAND', '02', '申舒斯',   20, 'VARCHAR', true),
('BRAND', '03', '杭州山科', 30, 'VARCHAR', true),
('BRAND', '04', '兴源鼎新', 40, 'VARCHAR', true),
('BRAND', '05', '浙江和达', 50, 'VARCHAR', true),
('BRAND', '06', '三川智慧', 60, 'VARCHAR', true),
('BRAND', '07', '宁波东海', 70, 'VARCHAR', true),
('BRAND', '08', '重庆智慧', 80, 'VARCHAR', true),
('BRAND', '09', '杭州水表', 90, 'VARCHAR', true);
INSERT INTO vc_code_value(codeId, valueId, valueName, valueOrder, valueType, preInit) VALUES
('REMOTE', '01', '宁波水表', 10, 'VARCHAR', true),
('REMOTE', '02', '申舒斯',   20, 'VARCHAR', true),
('REMOTE', '03', '杭州山科', 30, 'VARCHAR', true),
('REMOTE', '04', '兴源鼎新', 40, 'VARCHAR', true),
('REMOTE', '05', '浙江和达', 50, 'VARCHAR', true),
('REMOTE', '06', '三川智慧', 60, 'VARCHAR', true),
('REMOTE', '07', '宁波东海', 70, 'VARCHAR', true),
('REMOTE', '08', '重庆智慧', 80, 'VARCHAR', true),
('REMOTE', '09', '杭州水表', 90, 'VARCHAR', true);

/**
  "01-普通机械水表
02-高精度机械水表
03-无源光电直读水表
04-NB-IOT无磁传感水表
05-超声波水表
06-电磁水表"
 */
INSERT INTO vc_code_value(codeId, valueId, valueName, valueOrder, valueType, preInit) VALUES
('METER_CLASS', '01', '普通机械水表', 15, 'VARCHAR', true),
('METER_CLASS', '02', '高精度机械水表', 25, 'VARCHAR', true),
('METER_CLASS', '03', '无源光电直读水表', 35, 'VARCHAR', true),
('METER_CLASS', '04', 'NB-IOT无磁传感水表', 45, 'VARCHAR', true),
('METER_CLASS', '05', '超声波水表', 55, 'VARCHAR', true),
('METER_CLASS', '06', '电磁水表', 65, 'VARCHAR', true),

('METER_CLASS', 'NORMALMECH', '普通机械水表', 115, 'VARCHAR', true),
('METER_CLASS', 'HIGHMECH', '高精度机械水表', 125, 'VARCHAR', true),
('METER_CLASS', 'NOPOWER', '无源光电直读水表', 135, 'VARCHAR', true),
('METER_CLASS', 'NBIOT', 'NB-IOT无磁传感水表', 145, 'VARCHAR', true),
('METER_CLASS', 'SUPERSONIC', '超声波水表', 155, 'VARCHAR', true),
('METER_CLASS', 'ELECTRICMAG', '电磁水表', 165, 'VARCHAR', true)
;

INSERT INTO vc_code_value(codeId, valueId, valueName, valueOrder, valueType, preInit) VALUES
('BATCH', 'WAIT', '待检', 10, 'VARCHAR', true),
('BATCH', 'VERIFY', '在检', 20, 'VARCHAR', true),
('BATCH', 'WITHDRAW', '撤回', 30, 'VARCHAR', true),
('BATCH', 'FINISH', '完成', 40, 'VARCHAR', true),
('BATCH', 'REPORT', '已打报告', 50, 'VARCHAR', true),
('BATCH', 'ABNORMAL', '异常', 60, 'VARCHAR', true),
('BATCH', 'MIGRATED', '已迁移', 70, 'VARCHAR', true)
;

/**
('ACCURACY_GRADE', '准确精度等级', '', true),
 */
INSERT INTO vc_code_value(codeId, valueId, valueName, valueOrder, valueType, preInit) VALUES
('ACCURACY_GRADE', '1', '1级', 10, 'VARCHAR', true),
('ACCURACY_GRADE', '2', '2级', 20, 'VARCHAR', true)
;

/**
('TEMP_GRADE', '温度等级', '', true),
 */
INSERT INTO vc_code_value(codeId, valueId, valueName, valueOrder, valueType, preInit) VALUES
('TEMP_GRADE', '30', 'T30', 10, 'VARCHAR', true),
('TEMP_GRADE', '50', 'T50', 20, 'VARCHAR', true)
;

/**
('MAP_GRADE', 'MAP', '', true),
 */
INSERT INTO vc_code_value(codeId, valueId, valueName, valueOrder, valueType, preInit) VALUES
('MAP_GRADE', '1.0', '1.0', 10, 'VARCHAR', true),
('MAP_GRADE', '1.6', '1.6', 20, 'VARCHAR', true)
;

/**
('ABNORMAL_REASON', '异常原因（有疑问）', '', true),
  委托单号不匹配、水表表码不存在、其他原因
 */
INSERT INTO vc_code_value(codeId, valueId, valueName, valueOrder, valueType, preInit) VALUES
('ABNORMAL_REASON', 'NOBATCH', '委托单号不匹配', 10, 'VARCHAR', true),
('ABNORMAL_REASON', 'NOMETER', '水表表码不存在', 20, 'VARCHAR', true),
('ABNORMAL_REASON', 'OTHER', '其他原因', 30, 'VARCHAR', true)
;

-- 入库形式
DELETE FROM vc_workday_holiday
WHERE yr BETWEEN 2019 AND 2020;

INSERT INTO vc_workday_holiday(startDate, endDate, yr, holiday, weekend, workday, ruleType, createBy, createDate)
VALUES
('2018-12-29', '2018-12-29', 2019, false, false, true, 'N', 'auto', now()),
('2018-12-31', '2018-12-31', 2019, false, true, false, 'N', 'auto', now()),
('2019-2-2', '2019-2-3', 2019, false, false, true, 'N', 'auto', now()),
('2019-2-4', '2019-2-6', 2019, true, false, false, 'N', 'auto', now()),
('2019-2-7', '2019-2-8', 2019, false, true, false, 'N', 'auto', now()),
('2019-2-9', '2019-2-10', 2019, false, true, false, 'N', 'auto', now()),
('2019-4-5', '2019-4-5', 2019, true, false, false, 'N', 'auto', now()),
('2019-4-27', '2019-4-28', 2019, false, false, true, 'N', 'auto', now()),
('2019-4-29', '2019-4-30', 2019, false, true, false, 'N', 'auto', now()),
('2019-5-1', '2019-5-1', 2019, true, false, false, 'N', 'auto', now()),
('2019-6-7', '2019-6-7', 2019, true, false, false, 'N', 'auto', now()),
('2019-9-13', '2019-9-13', 2019, true, false, false, 'N', 'auto', now()),
('2019-9-29', '2019-9-29', 2019, false, false, true, 'N', 'auto', now()),
('2019-10-1', '2019-10-3', 2019, true, false, false, 'N', 'auto', now()), -- 节日
('2019-10-4', '2019-10-4', 2019, false, true, false, 'N', 'auto', now()),
('2019-10-7', '2019-10-7', 2019, false, true, false, 'N', 'auto', now()),
('2019-10-12', '2019-10-12', 2019, false, false, true, 'N', 'auto', now()), -- 调休
('2020-1-1', '2020-1-1', 2020, true, false, false, 'N', 'auto', now()),
('2020-1-19', '2020-1-19', 2020, false, false, true, 'N', 'auto', now()),
('2020-1-24', '2020-1-28', 2020, true, false, false, 'N', 'auto', now()),
('2020-1-29', '2020-1-30', 2020, false, true, false, 'N', 'auto', now()),
('2020-2-1', '2020-2-1', 2020, false, false, true, 'N', 'auto', now()),
('2020-4-6', '2020-4-6', 2020, false, true, false, 'N', 'auto', now()),
('2020-4-26', '2020-4-26', 2020, false, false, true, 'N', 'auto', now()),
('2020-5-1', '2020-5-1', 2020, true, false, false, 'N', 'auto', now()),
('2020-5-4', '2020-5-5', 2020, false, true, false, 'N', 'auto', now()),
('2020-5-9', '2020-5-9', 2020, false, false, true, 'N', 'auto', now()),
('2020-6-25', '2020-6-25', 2020, true, false, false, 'N', 'auto', now()),
('2020-6-26', '2020-6-26', 2020, false, true, false, 'N', 'auto', now()),
('2020-6-28', '2020-6-28', 2020, false, false, true, 'N', 'auto', now()),
('2020-9-27', '2020-9-27', 2020, false, false, true, 'N', 'auto', now()),
('2020-10-1', '2020-10-3', 2020, true, false, false, 'N', 'auto', now()),
('2020-10-5', '2020-10-8', 2020, false, true, false, 'N', 'auto', now()),
('2020-10-10', '2020-10-10', 2020, false, false, true, 'N', 'auto', now())
;
