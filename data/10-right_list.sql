-- \c eff;

-- delete role-right
DELETE FROM BwRoleRight WHERE rightName LIKE 'user%';
DELETE FROM BwRoleRight WHERE rightName LIKE 'zone%';
DELETE FROM BwRoleRight WHERE rightName LIKE 'pipe%';
DELETE FROM BwRoleRight WHERE rightName LIKE 'data%';
DELETE FROM BwRoleRight WHERE rightName LIKE 'meter%';
DELETE FROM BwRoleRight WHERE rightName LIKE '/user%';
DELETE FROM BwRoleRight WHERE rightName LIKE '/zone%';
DELETE FROM BwRoleRight WHERE rightName LIKE '/pipe%';
DELETE FROM BwRoleRight WHERE rightName LIKE '/data%';
DELETE FROM BwRoleRight WHERE rightName LIKE '/meter%';
DELETE FROM BwRoleRight WHERE rightName LIKE '/report%';
DELETE FROM BwRoleRight WHERE rightName LIKE '/stat%';
DELETE FROM BwRoleRight WHERE rightName LIKE '/verify%';
DELETE FROM BwRoleRight WHERE rightName LIKE '/study%';
DELETE FROM BwRoleRight WHERE rightName LIKE '/ware%';
DELETE FROM BwRoleRight;

-- right for user
DELETE FROM BwRight WHERE rightName LIKE 'user%';
DELETE FROM BwRight WHERE rightName LIKE 'zone%';
DELETE FROM BwRight WHERE rightName LIKE 'pipe%';
DELETE FROM BwRight WHERE rightName LIKE 'data%';
DELETE FROM BwRight WHERE rightName LIKE 'meter%';
DELETE FROM BwRight WHERE rightName LIKE '/user%';
DELETE FROM BwRight WHERE rightName LIKE '/zone%';
DELETE FROM BwRight WHERE rightName LIKE '/pipe%';
DELETE FROM BwRight WHERE rightName LIKE '/data%';
DELETE FROM BwRight WHERE rightName LIKE '/meter%';
DELETE FROM BwRight WHERE rightName LIKE '/stat%';
DELETE FROM BwRight WHERE rightName LIKE '/verify%';
DELETE FROM BwRight WHERE rightName LIKE '/report%';
DELETE FROM BwRight WHERE rightName LIKE '/study%';
DELETE FROM BwRight WHERE rightName LIKE '/ware%';
DELETE FROM BwRight;

-- delete role
DELETE FROM BwRole;


-- ----------------------------------------

INSERT INTO BwRight (rightName, rightDesc) VALUES ('LIST_USER', '列举用户资料');
INSERT INTO BwRoleRight (rightName, roleName) VALUES ('LIST_USER', 'ADMINISTRATOR');

INSERT INTO BwRight (rightName, rightDesc) VALUES ('RIGHT_CHANGE_ZONE', '更改片区资料');
INSERT INTO BwRoleRight (rightName, roleName) VALUES ('RIGHT_CHANGE_ZONE', 'POWER_USER');

INSERT INTO BwRight (rightName, rightDesc) VALUES ('RIGHT_LIST_ASSET', '查阅资产');
INSERT INTO BwRoleRight (rightName, roleName) VALUES ('RIGHT_LIST_ASSET', 'POWER_USER');

INSERT INTO BwRight (rightName, rightDesc) VALUES ('RIGHT_LIST_REALTIME', '查阅实时数据');
INSERT INTO BwRoleRight (rightName, roleName) VALUES ('RIGHT_LIST_REALTIME', 'POWER_USER');
INSERT INTO BwRight (rightName, rightDesc) VALUES ('RIGHT_LIST_TOTAL', '查阅统计数据');
INSERT INTO BwRoleRight (rightName, roleName) VALUES ('RIGHT_LIST_TOTAL', 'POWER_USER');
INSERT INTO BwRight (rightName, rightDesc) VALUES ('RIGHT_LIST_HISTORY', '查阅历史数据');
INSERT INTO BwRoleRight (rightName, roleName) VALUES ('RIGHT_LIST_HISTORY', 'POWER_USER');

INSERT INTO BwRight (rightName, rightDesc) VALUES ('RIGHT_HISTORY_UPLOAD', '上传历史数据');
INSERT INTO BwRoleRight (rightName, roleName) VALUES ('RIGHT_HISTORY_UPLOAD', 'POWER_USER');
INSERT INTO BwRight (rightName, rightDesc) VALUES ('RIGHT_HISTORY_REMOVE', '删除历史数据');
INSERT INTO BwRoleRight (rightName, roleName) VALUES ('RIGHT_HISTORY_REMOVE', 'POWER_USER');


-- 原来的权限
INSERT INTO BwRight (rightName, rightDesc) VALUES('瞬时数据上传', '瞬时数据上传权限');
INSERT INTO BwRight (rightName, rightDesc) VALUES('瞬时数据删除', '瞬时数据删除权限');
INSERT INTO BwRight (rightName, rightDesc) VALUES('累计数据上传', '累计数据上传权限');
INSERT INTO BwRight (rightName, rightDesc) VALUES('累计数据删除', '累计数据删除权限');
INSERT INTO BwRight (rightName, rightDesc) VALUES('创建用户', '创建用户权限');

INSERT INTO BwRight (rightName, rightDesc) VALUES('REALTIME_DATA_UPLOAD', '瞬时数据上传权限');
INSERT INTO BwRight (rightName, rightDesc) VALUES('REALTIME_DATA_REMOVE', '瞬时数据删除权限');
INSERT INTO BwRight (rightName, rightDesc) VALUES('TOTAL_DATA_UPLOAD', '累计数据上传权限');
INSERT INTO BwRight (rightName, rightDesc) VALUES('TOTAL_DATA_REMOVE', '累计数据删除权限');
INSERT INTO BwRight (rightName, rightDesc) VALUES('CREATE_USER', '创建用户权限');
INSERT INTO BwRight (rightName, rightDesc) VALUES('LOGIN', '登录系统权限');
INSERT INTO BwRight (rightName, rightDesc) VALUES('MOB_LOGIN', '手机端登录系统权限');

INSERT INTO BwRole (roleName, roleDesc) VALUES('管理员', '管理员描述');
INSERT INTO BwRoleRight (roleName, rightName) VALUES('管理员', '创建用户');

--  角色 ADMINISTRATOR
INSERT INTO BwRole (roleName, roleDesc) VALUES('ADMINISTRATOR', '管理员描述');
INSERT INTO BwRoleRight (roleName, rightName) VALUES('ADMINISTRATOR', 'LOGIN');
INSERT INTO BwRoleRight (roleName, rightName) VALUES('ADMINISTRATOR', 'MOB_LOGIN');
INSERT INTO BwRoleRight (roleName, rightName) VALUES('ADMINISTRATOR', 'CREATE_USER');
INSERT INTO BwRoleRight (roleName, rightName) VALUES('ADMINISTRATOR', 'REALTIME_DATA_UPLOAD');
INSERT INTO BwRoleRight (roleName, rightName) VALUES('ADMINISTRATOR', 'REALTIME_DATA_REMOVE');
INSERT INTO BwRoleRight (roleName, rightName) VALUES('ADMINISTRATOR', 'TOTAL_DATA_UPLOAD');
INSERT INTO BwRoleRight (roleName, rightName) VALUES('ADMINISTRATOR', 'TOTAL_DATA_REMOVE');

-- 角色 BACK_USER
INSERT INTO BwRole (roleName, roleDesc) VALUES('BACK_USER', '后台管理账号');
INSERT INTO BwRoleRight (roleName, rightName) VALUES('BACK_USER', 'LOGIN');
INSERT INTO BwRoleRight (roleName, rightName) VALUES('BACK_USER', 'MOB_LOGIN');
INSERT INTO BwRoleRight (roleName, rightName) VALUES('BACK_USER', 'REALTIME_DATA_UPLOAD');
INSERT INTO BwRoleRight (roleName, rightName) VALUES('BACK_USER', 'REALTIME_DATA_REMOVE');
INSERT INTO BwRoleRight (roleName, rightName) VALUES('BACK_USER', 'TOTAL_DATA_UPLOAD');
INSERT INTO BwRoleRight (roleName, rightName) VALUES('BACK_USER', 'TOTAL_DATA_REMOVE');

-- 角色 POWER_USER
INSERT INTO BwRole (roleName, roleDesc) VALUES('POWER_USER', '高级用户');
INSERT INTO BwRoleRight (roleName, rightName) VALUES('POWER_USER', 'LOGIN');
INSERT INTO BwRoleRight (roleName, rightName) VALUES('POWER_USER', 'MOB_LOGIN');
INSERT INTO BwRoleRight (roleName, rightName) VALUES('POWER_USER', 'REALTIME_DATA_UPLOAD');
INSERT INTO BwRoleRight (roleName, rightName) VALUES('POWER_USER', 'REALTIME_DATA_REMOVE');
INSERT INTO BwRoleRight (roleName, rightName) VALUES('POWER_USER', 'TOTAL_DATA_UPLOAD');
INSERT INTO BwRoleRight (roleName, rightName) VALUES('POWER_USER', 'TOTAL_DATA_REMOVE');

-- 角色 DMA_USER
INSERT INTO BwRole (roleName, roleDesc) VALUES('DMA_USER', 'DMA分区用户');
INSERT INTO BwRoleRight (roleName, rightName) VALUES('DMA_USER', 'LOGIN');
INSERT INTO BwRoleRight (roleName, rightName) VALUES('DMA_USER', 'MOB_LOGIN');
INSERT INTO BwRoleRight (roleName, rightName) VALUES('DMA_USER', 'TOTAL_DATA_UPLOAD');

-- 角色 MOB_USER
INSERT INTO BwRole (roleName, roleDesc) VALUES('MOB_USER', '手机用户');
INSERT INTO BwRoleRight (roleName, rightName) VALUES('MOB_USER', 'MOB_LOGIN');


----------------
-- right list --
----------------
-- <editor-fold desc="right list">
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/user', '用户详情');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/user/login', '登录');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/user/logout', '登出');

INSERT INTO BwRight(rightName, rightDesc) VALUES ('/user/firmList', '获取水司列表');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/user/updateFirm', '更新机构信息');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/user/addFirm', '增加机构信息');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/user/deleteFirm', '删除机构信息');

INSERT INTO BwRight(rightName, rightDesc) VALUES ('/user/userList', '获取用户列表');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/user/createUser', '创建用户');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/user/updateUser', '修改用户');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/user/deleteUser', '删除用户');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/user/info', '用户详情');

INSERT INTO BwRight(rightName, rightDesc) VALUES ('/user/rightList', '权限列表');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/user/roleList', '角色列表');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/user/role/{roleName}', '角色详情');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/user/createRole', '创建角色');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/user/deleteRole', '删除角色');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/user/updateRole', '修改角色');

INSERT INTO BwRight(rightName, rightDesc) VALUES ('/user/operList', '列举操作日志');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/user/operStat', '用户操作统计');

INSERT INTO BwRight(rightName, rightDesc) VALUES ('/user/loginList', '列举会话');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/user/kickLogin', '踢出会话');

INSERT INTO BwRight(rightName, rightDesc) VALUES ('/user/listStdAuth', '获取计量标准列表');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/user/addStdAuth', '插入计量标准');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/user/deleteStdAuth', '删除计量标准');

-- right for zone
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/zone/save', '创建片区');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/zone/delete', '删除片区');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/zone/update', '更改片区');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/zone/updateZoneLoc', '更改片区坐标及边界');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/zone/list', '片区列表');

INSERT INTO BwRight(rightName, rightDesc) VALUES ('/zone/saveZoneMeter', '创建片区大表');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/zone/deleteZoneMeter', '删除片区大表');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/zone/zoneMeterList', '片区大表列表');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/zone/zoneMeterCount', '片区大表数量');

INSERT INTO BwRight(rightName, rightDesc) VALUES ('/zone/deleteHourFlow', '删除小时水量');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/zone/hourFlowRange', '小时水量范围');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/zone/hourFlowList', '片区列表');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/zone/deleteHourDayFlow', '删除统计水量');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/zone/deleteDayFlow', '删除日水量');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/zone/dayFlowRange', '日水量范围');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/zone/dayFlowList', '水量范围列表');

INSERT INTO BwRight(rightName, rightDesc) VALUES ('/zone/saveZoneTemp', '创建片区模板');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/zone/deleteZoneTemp', '删除片区模板');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/zone/updateZoneTemp', '修改片区模板');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/zone/zoneTempList', '片区模板列表');

INSERT INTO BwRight(rightName, rightDesc) VALUES ('/zone/statMeterFlow', '统计水表水量');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/zone/zoneStatFirm', '统计片区水量');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/zone/listMeterDayFlow', '查询水表日水量');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/zone/listMeterHourFlow', '查询水表小时水量');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/zone/meterDayFlowRange', '查询水表日水量范围');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/zone/meterHourFlowRange', '查询水表小时水量范围');


INSERT INTO BwRight(rightName, rightDesc) VALUES ('/zone/listMeterWeekFlow', '查询水表周水量');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/zone/listMeterMonthFlow', '查询水表月水量');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/zone/listMeterDayFlowToday', '查询水司大表的今日水量');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/zone/listMeterDayFlowYest', '查询水司大表的昨日水量');

INSERT INTO BwRight(rightName, rightDesc) VALUES ('/zone/searchAllText', '全站搜索');

INSERT INTO BwRight(rightName, rightDesc) VALUES ('/meter/insert', '创建水表');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/meter/delete', '删除水表');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/meter/update', '修改水表');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/meter/updateMeterLoc', '修改水表坐标');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/meter/list', '列举水表');

INSERT INTO BwRight(rightName, rightDesc) VALUES ('/meter/listDma', '列举DMA');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/meter/insertDma', '创建DMA');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/meter/updateDma', '修改DMA');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/meter/updateDmaLoc', '修改DMA坐标及边界');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/meter/listDmaLoc', '查询漏损地图');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/meter/deleteDma', '删除DMA');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/meter/detachDmaUser', '分离DMA和账户');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/meter/attachDmaUser', '连接DMA和账户');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/meter/linkMeterDma', '连接DMA和水表');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/meter/detachMeterDma', '分离DMA和水表');

-- data
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/data/listRealtime', '列举数据');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/data/realtimeDateRange', '列举数据范围');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/data/realtimeListStat', '列举数据统计');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/data/addRealtime', '增加数据');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/data/addRealtimeUser', '增加数据验证');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/data/deleteRealtime', '删除数据');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/data/listRtuLog', '列举RTU日志');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/data/addRtuLog', '增加RTU日志');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/data/addRtuLogUser', '增加RTU日志验证');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/data/deleteRtuLog', '删除RTU日志');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/data/listRtu', '列举RTU');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/data/deleteRtu', '删除RTU');

INSERT INTO BwRight(rightName, rightDesc) VALUES ('/data/scadaMeterList', '大表的SCADA数据');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/data/scadaMeterListZone', '片区大表的SCADA数据');

-- mnf.
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/stat/listDmaLoc', '查询漏损地图');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/stat/listLoss', '获取漏损列表');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/stat/lossDataRange', '漏损结果的日期范围');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/stat/deleteLoss', '删除漏损结果');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/stat/lossNow', '计算漏损');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/stat/listStat', '获取最小流量的统计结果');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/stat/statDataRange', '最小流量的统计结果的日期范围');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/stat/deleteStat', '删除最小流量的统计结果');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/stat/statNow', '计算最小流量');

INSERT INTO BwRight(rightName, rightDesc) VALUES ('/stat/latest', '获取最新统计信息');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/stat/fetchStuffScore', '获取检定员的记分卡');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/stat/deleteSizeStat', '移除口径统计记录');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/stat/deleteStuffScore', '移除检定员工分卡');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/stat/deleteFactoryStat', '移除厂家-口径统计');

INSERT INTO BwRight(rightName, rightDesc) VALUES ('/code/listCode', '列出代码类');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/code/listValue', '列出代码值');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/code/createValue', '创建代码值');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/code/updateValue', '修改代码值');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/code/deleteValue', '删除代码值');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/code/listFactory', '列出水表厂商');

INSERT INTO BwRight(rightName, rightDesc) VALUES ('/code/listVerifyTemp',	'检定点模板');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/code/createVerifyTemp',	'创建检定点模板');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/code/removeVerifyTemp',	'移除检定点模板');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/code/updateVerifyTemp',	'修改检定点模板');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/code/configList',		'列举配置项');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/code/updateConfig',		'更改系统配置项');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/code/insertWorkdayHoliday',		'插入工作日/节假日');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/code/deleteWorkdayHoliday',		'删除工作日/节假日');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/code/selectWorkdayHoliday',		'列出工作日/节假日');

INSERT INTO BwRight(rightName, rightDesc) VALUES ('/verify/createBatch'		, '创建批次');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/verify/listBatch'		, '列出批次');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/verify/fetchBatch'		, '列出批次');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/verify/addBatchMeter'	, '为指定批次添加水表');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/verify/removeBatchMeter'	, '从批次移除水表');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/verify/listBatchMeter'	, '列出批次详情');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/verify/cancelBatch'	, '取消批次');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/verify/finishBatch'	, '完成批次');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/verify/reportBatch'	, '获取批次报告');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/verify/updateBatch'	, '修改批次，批次号、状态不可修改。');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/verify/updateMeter'	, '修改水表，水表ID、状态不可修改。');


INSERT INTO BwRight(rightName, rightDesc) VALUES ('/verify/migrateAbnormalBatch'	, '迁移异常委托单');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/verify/migrateAbnormalMeter'	, '迁移部分水表');


INSERT INTO BwRight(rightName, rightDesc) VALUES ('/verify/listBatchVerify'	, '取得批次的检定数据');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/verify/listBatchVerifyPoint'	, '取得批次的检定点数据');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/verify/addMeterVerify'	, '添加水表检定数据');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/verify/addMeterVerifyPoint'	, '添加水表检定点');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/verify/removeMeterVerify'	, '删除水表检定数据');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/verify/removeMeterVerifyPoint'	, '删除水表检定点');


INSERT INTO BwRight(rightName, rightDesc) VALUES ('/ware/createBox'	, '编箱，必须将编箱的表号及批次号 一次传入');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/ware/removeBox'	, '解散编箱，只需传入编箱号。');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/ware/listBox'	, '取得批次的全部编箱');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/ware/fetchBox'	, '取得批次的全部编箱');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/ware/changeBox'	, '重新编箱');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/ware/auditIn'		, '编箱入库审核');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/ware/outBox'		, '编箱出库');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/ware/auditOut'		, '审核编箱出库');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/ware/rejectApply'	, '退回上级流程');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/ware/listMeter'	, '列出编箱的水表');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/ware/statWare'	, '统计水表库存');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/ware/listStat'	, '列出水表库存');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/ware/rejectMeter'	, '退回不合格水表');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/ware/listRejectedMeter'	, '列出不合格水表');


INSERT INTO BwRight(rightName, rightDesc) VALUES ('/report/listBatchResult'	, '批次检定结果');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/report/listBatchReport'	, '批次检定报告');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/report/fetchReport'	, '批次检定报告');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/report/createReport'	,     '创建批次检定报告');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/report/auditBatchReport'	, '审核报告');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/report/printBatchReport'	, '打印报告');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/report/addOnceToBaseReport'	, '添加一次 检定合格/不合格水表到 基础检定报告');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/report/addMoreToBaseReport'	, '添加复检 合格/不合格水表到 基础检定报告');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/report/fetchBaseReport'	, '获取 基础检定报告 包含 未报告/已报告 检定结果');

INSERT INTO BwRight(rightName, rightDesc) VALUES ('/report/listBatchCert'	, '批次检定证书');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/report/fetchCert'	, '批次检定证书');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/report/createCert'	, '创建批次检定证书');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/report/auditBatchCert'	, '审核证书');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/report/printBatchCert'	, '打印证书');

INSERT INTO BwRight(rightName, rightDesc) VALUES ('/study/listStudy',			'列出研究项目');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/study/fetchStudy',	    	'列出单个研究项目的详情');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/study/createStudy',  		'启动研究项目');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/study/deleteStudy',  		'删除未做报告的研究项目');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/study/buildStudy',   		'计算研究结果');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/study/addMeterToStudy',		'添加研究项目的水表');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/study/removeMeterFromStudy', '移除研究项目的水表');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/study/listStudyModel',   	'列出研究项目使用的消费模式');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/study/fetchStudyModel',  	'列出单个消费模式的详情');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/study/addStudyModel',		'添加研究项目的消费模式');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/study/removeStudyModel', 	'移除研究项目的消费模式');

INSERT INTO BwRight(rightName, rightDesc) VALUES ('/life/listLifePiece'	, '生命周期事件');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/life/addLifePiece'	, '录入生命周期');

-- 备案
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/reg/listReg'	    , '列出备案记录');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/reg/fetchReg'	, '获取单次的备案详单');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/reg/fetchBatchReg'	, '获取委托单的备案详单');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/reg/listMeter'	, '列出备案详单');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/reg/listUnregMeter'	, '列出待备案详单');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/reg/createReg'	, '备案一批水表');
INSERT INTO BwRight(rightName, rightDesc) VALUES ('/reg/removeReg'	, '取消一次备案');

-- </editor-fold>

-- ===========================================================================

-- 角色 WARE_JIANDING 库房检定员
-- 角色 WARE_AUDIT 库房审核
INSERT INTO BwRole (roleName, roleDesc) VALUES('WARE_JIANDING', '库房检定员');
INSERT INTO BwRole (roleName, roleDesc) VALUES('WARE_AUDIT', '库房审核');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/user/login');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/user/logout');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/user/login');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/user/logout');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/user/info');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_JIANDING', '/user/login');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_JIANDING', '/user/logout');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_JIANDING', '/user/info');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_AUDIT', '/user/login');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_AUDIT', '/user/logout');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_AUDIT', '/user/info');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('DMA_USER', '/user/login');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('DMA_USER', '/user/logout');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('DMA_USER', '/user/info');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('DMA_USER', '/zone/searchAllText');

-- mnf
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('DMA_USER', '/stat/listDmaLoc');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('DMA_USER', '/stat/listLoss');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('DMA_USER', '/stat/lossDataRange');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('DMA_USER', '/stat/listStat');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('DMA_USER', '/stat/statDataRange');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('MOB_USER', '/user/login');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('MOB_USER', '/user/logout');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('MOB_USER', '/user/info');

-- mnf
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('MOB_USER', '/stat/listDmaLoc');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('MOB_USER', '/stat/listLoss');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('MOB_USER', '/stat/lossDataRange');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('MOB_USER', '/stat/listStat');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('MOB_USER', '/stat/statDataRange');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('MOB_USER', '/zone/searchAllText');

-- admin

-- <editor-fold desc="rights for ADMINISTRATOR">
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/user/firmList');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/user/updateFirm');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/user/addFirm');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/user/deleteFirm');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/user/userList');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/user/createUser');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/user/updateUser');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/user/deleteUser');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/user/info');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/user/rightList');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/user/roleList');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/user/role');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/user/createRole');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/user/deleteRole');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/user/updateRole');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/user/operList');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/user/operStat');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/user/loginList');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/user/kickLogin');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/user/listStdAuth');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/user/addStdAuth');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/user/deleteStdAuth');

-- zone
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/zone/save');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/zone/delete');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/zone/update');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/zone/updateZoneLoc');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/zone/list');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/zone/saveZoneMeter');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/zone/deleteZoneMeter');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/zone/zoneMeterList');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/zone/zoneMeterCount');


INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/zone/deleteHourFlow');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/zone/hourFlowRange');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/zone/hourFlowList');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/zone/deleteHourDayFlow');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/zone/deleteDayFlow');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/zone/dayFlowRange');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/zone/dayFlowList');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/zone/saveZoneTemp');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/zone/deleteZoneTemp');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/zone/updateZoneTemp');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/zone/zoneTempList');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/zone/statMeterFlow');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/zone/zoneStatFirm');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/zone/listMeterDayFlow');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/zone/listMeterHourFlow');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/zone/meterDayFlowRange');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/zone/meterHourFlowRange');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/zone/listMeterWeekFlow');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/zone/listMeterMonthFlow');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/zone/listMeterDayFlowToday');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/zone/listMeterDayFlowYest');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/zone/searchAllText');

-- meter
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/meter/insert');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/meter/delete');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/meter/update');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/meter/updateMeterLoc');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/meter/list');

-- dma
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/meter/listDma');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/meter/insertDma');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/meter/updateDma');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/meter/updateDmaLoc');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/meter/listDmaLoc');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/meter/deleteDma');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/meter/detachDmaUser');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/meter/attachDmaUser');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/meter/linkMeterDma');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/meter/detachMeterDma');

-- data
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/data/listRealtime');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/data/realtimeDateRange');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/data/realtimeListStat');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/data/addRealtime');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/data/addRealtimeUser');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/data/deleteRealtime');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/data/listRtuLog');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/data/addRtuLog');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/data/addRtuLogUser');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/data/deleteRtuLog');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/data/listRtu');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/data/deleteRtu');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/data/scadaMeterList');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/data/scadaMeterListZone');

-- mnf
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/stat/listDmaLoc');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/stat/listLoss');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/stat/lossDataRange');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/stat/deleteLoss');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/stat/lossNow');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/stat/listStat');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/stat/statDataRange');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/stat/deleteStat');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/stat/statNow');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/stat/latest');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/stat/fetchStuffScore');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/stat/deleteSizeStat');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/stat/deleteStuffScore');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/stat/deleteFactoryStat');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/code/listFactory');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/code/createValue');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/code/updateValue');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/code/deleteValue');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/code/listVerifyTemp');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/code/createVerifyTemp');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/code/removeVerifyTemp');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/code/updateVerifyTemp');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/code/configList');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/code/updateConfig');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/code/insertWorkdayHoliday');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/code/deleteWorkdayHoliday');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/code/selectWorkdayHoliday');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/verify/createBatch'		);
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/verify/listBatch'		);
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/verify/fetchBatch'		);
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/verify/addBatchMeter'	);
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/verify/removeBatchMeter'	);
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/verify/listBatchMeter'	);
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/verify/cancelBatch');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/verify/finishBatch');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/verify/reportBatch');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/verify/updateBatch');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/verify/updateMeter');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/verify/migrateAbnormalBatch');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/verify/migrateAbnormalMeter');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/verify/listBatchVerify'	);
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/verify/listBatchVerifyPoint'	);

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/ware/listBox'  );
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/ware/fetchBox');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/ware/createBox');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/ware/removeBox');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/ware/changeBox');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/ware/auditIn'   );
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/ware/outBox'    );
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/ware/auditOut'  );
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/ware/rejectApply');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/ware/listMeter');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/ware/statWare');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/ware/listStat');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/ware/rejectMeter');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/ware/listRejectedMeter');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/report/listBatchResult');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/report/listBatchReport');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/report/fetchReport');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/report/createReport');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/report/auditBatchReport');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/report/printBatchReport');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/report/addOnceToBaseReport');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/report/addMoreToBaseReport');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/report/fetchBaseReport');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/report/listBatchCert');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/report/fetchCert');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/report/createCert');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/report/auditBatchCert');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/report/printBatchCert');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/study/listStudy');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/study/fetchStudy');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/study/createStudy');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/study/deleteStudy');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/study/buildStudy');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/study/addMeterToStudy');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/study/removeMeterFromStudy');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/study/listStudyModel');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/study/fetchStudyModel');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/study/addStudyModel');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/study/removeStudyModel');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/life/listLifePiece');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('ADMINISTRATOR', '/life/addLifePiece');

-- </editor-fold>

-- ------------
-- BACK_USER --
-- ------------
-- <editor-fold desc="rights for BACK_USER">
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('BACK_USER', '/user/firmList');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('BACK_USER', '/user/updateFirm');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('BACK_USER', '/user/addFirm');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('BACK_USER', '/user/deleteFirm');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('BACK_USER', '/user/userList');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('BACK_USER', '/user/info');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('BACK_USER', '/user/rightList');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('BACK_USER', '/user/roleList');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('BACK_USER', '/user/role');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('BACK_USER', '/user/operList');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('BACK_USER', '/user/operStat');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('BACK_USER', '/user/loginList');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('BACK_USER', '/user/kickLogin');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('BACK_USER', '/user/listStdAuth');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('BACK_USER', '/user/addStdAuth');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('BACK_USER', '/user/deleteStdAuth');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('BACK_USER', '/code/listFactory');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('BACK_USER', '/code/createValue');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('BACK_USER', '/code/updateValue');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('BACK_USER', '/code/deleteValue');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('BACK_USER', '/code/configList');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('BACK_USER', '/code/updateConfig');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('BACK_USER', '/code/listVerifyTemp');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('BACK_USER', '/code/createVerifyTemp');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('BACK_USER', '/code/removeVerifyTemp');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('BACK_USER', '/code/updateVerifyTemp');

-- </editor-fold>

-- -------------
-- POWER_USER --
-- -------------
-- <editor-fold desc="rights for POWER_USER">
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/user/firmList');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/user/updateFirm');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/user/addFirm');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/user/deleteFirm');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/user/listStdAuth');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/user/addStdAuth');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/user/deleteStdAuth');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/code/configList');
-- zone
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/zone/save');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/zone/delete');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/zone/update');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/zone/updateZoneLoc');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/zone/list');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/zone/saveZoneMeter');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/zone/deleteZoneMeter');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/zone/zoneMeterList');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/zone/zoneMeterCount');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/zone/deleteHourFlow');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/zone/hourFlowRange');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/zone/hourFlowList');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/zone/deleteHourDayFlow');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/zone/deleteDayFlow');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/zone/dayFlowRange');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/zone/dayFlowList');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/zone/saveZoneTemp');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/zone/deleteZoneTemp');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/zone/updateZoneTemp');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/zone/zoneTempList');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/zone/statMeterFlow');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/zone/zoneStatFirm');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/zone/listMeterDayFlow');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/zone/listMeterHourFlow');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/zone/meterDayFlowRange');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/zone/meterHourFlowRange');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/zone/listMeterWeekFlow');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/zone/listMeterMonthFlow');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/zone/listMeterDayFlowToday');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/zone/listMeterDayFlowYest');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/zone/searchAllText');

-- meter
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/meter/insert');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/meter/delete');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/meter/update');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/meter/updateMeterLoc');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/meter/list');

-- dma
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/meter/listDma');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/meter/insertDma');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/meter/updateDma');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/meter/updateDmaLoc');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/meter/listDmaLoc');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/meter/deleteDma');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/meter/detachDmaUser');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/meter/attachDmaUser');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/meter/linkMeterDma');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/meter/detachMeterDma');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/data/listRealtime');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/data/realtimeDateRange');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/data/realtimeListStat');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/data/addRealtime');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/data/addRealtimeUser');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/data/deleteRealtime');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/data/listRtuLog');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/data/addRtuLog');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/data/addRtuLogUser');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/data/deleteRtuLog');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/data/listRtu');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/data/deleteRtu');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/data/scadaMeterList');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/data/scadaMeterListZone');

-- mnf
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/stat/listDmaLoc');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/stat/listLoss');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/stat/lossDataRange');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/stat/deleteLoss');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/stat/lossNow');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/stat/listStat');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/stat/statDataRange');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/stat/deleteStat');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/stat/statNow');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/stat/latest');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/stat/fetchStuffScore');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/stat/deleteSizeStat');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/stat/deleteStuffScore');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/stat/deleteFactoryStat');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/verify/createBatch'	);
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/verify/listBatch'		);
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/verify/fetchBatch'		);
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/verify/addBatchMeter'	);
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/verify/removeBatchMeter'	);
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/verify/listBatchMeter'	);
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/verify/cancelBatch');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/verify/finishBatch');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/verify/reportBatch');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/verify/updateBatch');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/verify/updateMeter');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/verify/migrateAbnormalBatch');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/verify/migrateAbnormalMeter');


INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/verify/listBatchVerify'	);
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/verify/listBatchVerifyPoint'	);
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/verify/addMeterVerify'	);
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/verify/addMeterVerifyPoint'	);
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/verify/removeMeterVerify'	);
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/verify/removeMeterVerifyPoint'	);

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/ware/listBox'  );
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/ware/fetchBox');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/ware/createBox');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/ware/removeBox');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/ware/changeBox');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/ware/outBox'    );
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/ware/auditIn'   );
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/ware/auditOut'  );
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/ware/rejectApply');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/ware/listMeter');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/ware/statWare');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/ware/listStat');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/ware/rejectMeter');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/ware/listRejectedMeter');


INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/report/listBatchResult');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/report/listBatchReport');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/report/fetchReport');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/report/createReport');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/report/auditBatchReport');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/report/printBatchReport');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/report/addOnceToBaseReport');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/report/addMoreToBaseReport');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/report/fetchBaseReport');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/report/listBatchCert');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/report/fetchCert');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/report/createCert');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/report/auditBatchCert');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/report/printBatchCert');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/study/listStudy');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/study/fetchStudy');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/study/createStudy');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/study/deleteStudy');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/study/buildStudy');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/study/addMeterToStudy');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/study/removeMeterFromStudy');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/study/listStudyModel');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/study/fetchStudyModel');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/study/addStudyModel');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/study/removeStudyModel');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/life/listLifePiece');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/life/addLifePiece');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER',  '/reg/listReg'	);
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER',  '/reg/fetchReg'	);
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER',  '/reg/fetchBatchReg'	);
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER',  '/reg/listMeter');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER',  '/reg/listUnregMeter');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER',  '/reg/createReg');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER',  '/reg/removeReg');

-- </editor-fold>

-- -------------
-- WARE_JIANDING --
-- -------------
-- <editor-fold desc="rights for WARE_JIANDING">
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_JIANDING', '/user/firmList');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_JIANDING', '/user/listStdAuth');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_JIANDING', '/code/configList');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_JIANDING', '/stat/listStat');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_JIANDING', '/stat/statDataRange');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_JIANDING', '/stat/deleteStat');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_JIANDING', '/stat/statNow');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_JIANDING', '/stat/latest');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_JIANDING', '/stat/fetchStuffScore');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_JIANDING', '/stat/deleteSizeStat');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_JIANDING', '/stat/deleteStuffScore');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_JIANDING', '/stat/deleteFactoryStat');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_JIANDING', '/verify/createBatch'	);
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_JIANDING', '/verify/listBatch'		);
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_JIANDING', '/verify/fetchBatch'		);
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_JIANDING', '/verify/addBatchMeter'	);
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_JIANDING', '/verify/removeBatchMeter'	);
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_JIANDING', '/verify/listBatchMeter'	);
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_JIANDING', '/verify/cancelBatch');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_JIANDING', '/verify/finishBatch');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_JIANDING', '/verify/reportBatch');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_JIANDING', '/verify/updateBatch');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_JIANDING', '/verify/updateMeter');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_JIANDING', '/verify/migrateAbnormalBatch');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_JIANDING', '/verify/migrateAbnormalMeter');


INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_JIANDING', '/verify/listBatchVerify'	);
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_JIANDING', '/verify/listBatchVerifyPoint'	);
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_JIANDING', '/verify/addMeterVerify'	);
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_JIANDING', '/verify/addMeterVerifyPoint'	);
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_JIANDING', '/verify/removeMeterVerify'	);
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_JIANDING', '/verify/removeMeterVerifyPoint'	);

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_JIANDING', '/ware/listBox'  );
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_JIANDING', '/ware/fetchBox');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_JIANDING', '/ware/createBox');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_JIANDING', '/ware/removeBox');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_JIANDING', '/ware/changeBox');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_JIANDING', '/ware/outBox'    );
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_JIANDING', '/ware/auditIn'   );
-- INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/ware/auditOut'  );
-- INSERT INTO BwRoleRight(roleName, rightName) VALUES ('POWER_USER', '/ware/rejectApply');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_JIANDING', '/ware/listMeter');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_JIANDING', '/ware/statWare');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_JIANDING', '/ware/listStat');

-- </editor-fold>

-- -------------
-- WARE_AUDIT --
-- -------------
-- <editor-fold desc="rights for WARE_AUDIT">
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_AUDIT', '/user/firmList');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_AUDIT', '/user/listStdAuth');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_AUDIT', '/code/configList');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_AUDIT', '/stat/listStat');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_AUDIT', '/stat/statDataRange');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_AUDIT', '/stat/deleteStat');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_AUDIT', '/stat/statNow');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_AUDIT', '/stat/latest');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_AUDIT', '/stat/fetchStuffScore');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_AUDIT', '/stat/deleteSizeStat');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_AUDIT', '/stat/deleteStuffScore');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_AUDIT', '/stat/deleteFactoryStat');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_AUDIT', '/verify/createBatch'	);
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_AUDIT', '/verify/listBatch'		);
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_AUDIT', '/verify/fetchBatch'		);
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_AUDIT', '/verify/addBatchMeter'	);
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_AUDIT', '/verify/removeBatchMeter'	);
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_AUDIT', '/verify/listBatchMeter'	);
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_AUDIT', '/verify/cancelBatch');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_AUDIT', '/verify/finishBatch');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_AUDIT', '/verify/reportBatch');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_AUDIT', '/verify/updateBatch');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_AUDIT', '/verify/updateMeter');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_AUDIT', '/verify/migrateAbnormalBatch');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_AUDIT', '/verify/migrateAbnormalMeter');


INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_AUDIT', '/verify/listBatchVerify'	);
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_AUDIT', '/verify/listBatchVerifyPoint'	);
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_AUDIT', '/verify/addMeterVerify'	);
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_AUDIT', '/verify/addMeterVerifyPoint'	);
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_AUDIT', '/verify/removeMeterVerify'	);
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_AUDIT', '/verify/removeMeterVerifyPoint'	);

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_AUDIT', '/ware/listBox'  );
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_AUDIT', '/ware/fetchBox');
-- INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_JIANDING', '/ware/createBox');
-- INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_JIANDING', '/ware/removeBox');
-- INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_JIANDING', '/ware/changeBox');
-- INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_JIANDING', '/ware/outBox'    );
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_AUDIT', '/ware/auditIn'   );
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_AUDIT', '/ware/auditOut'  );
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_AUDIT', '/ware/rejectApply');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_AUDIT', '/ware/listMeter');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_AUDIT', '/ware/statWare');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_AUDIT', '/ware/listStat');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_AUDIT', '/ware/rejectMeter');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_AUDIT', '/ware/listRejectedMeter');

INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_AUDIT',  '/reg/listReg'	);
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_AUDIT',  '/reg/fetchReg'	);
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_AUDIT',  '/reg/fetchBatchReg'	);
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_AUDIT',  '/reg/listMeter');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_AUDIT',  '/reg/listUnregMeter');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_AUDIT',  '/reg/createReg');
INSERT INTO BwRoleRight(roleName, rightName) VALUES ('WARE_AUDIT',  '/reg/removeReg');

-- </editor-fold>

/*
SELECT *
FROM BwRight;
*/
-- GO