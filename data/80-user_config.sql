-- USE eff;

/*
-- 测试时初始化
TRUNCATE TABLE bw_user_role;
TRUNCATE TABLE bw_role_right;
TRUNCATE TABLE bw_role;
TRUNCATE TABLE bw_right;
TRUNCATE TABLE bw_config;

TRUNCATE TABLE bw_firm;


TRUNCATE TABLE bw_user_role;
TRUNCATE TABLE bw_user;

*/

DELETE FROM bw_user_role ur
WHERE userId IN ('admin', 'abel', 'scott', 'fuzhou');

DELETE FROM bw_user u
WHERE userId IN ('admin', 'abel', 'scott', 'fuzhou');

-- admin/world
INSERT INTO bw_user (userId, userName, firmId, passHash, email, mobile, smallIcon, bigIcon, signPic)
VALUES('abel', '高级用户', '27', '098f6bcd4621d373cade4e832627b4f6', 'abelli5@126.com', '13510012001'
, 'http://localhost:8080/docs/images/tomcat.png'
, 'http://localhost:8080/docs/images/asf-logo.svg'
, 'http://localhost:8080/examples/jsp/jsp2/jspx/textRotate.jpg'),
('fuzhou', '高级用户', '76', '098f6bcd4621d373cade4e832627b4f6', 'abelli5@126.com', '13510012001'
, 'http://localhost:8080/docs/images/tomcat.png'
, 'http://localhost:8080/docs/images/asf-logo.svg'
, 'http://localhost:8080/examples/jsp/jsp2/jspx/textRotate.jpg'),
('admin', '管理员', '1', '7d793037a0760186574b0282f2f435e7', 'abelli@139.com', '13510011001'
, 'http://localhost:8080/docs/images/tomcat.png'
, 'http://localhost:8080/docs/images/asf-logo.svg'
, 'http://localhost:8080/examples/jsp/jsp2/jspx/textRotate.jpg'),
('scott', 'DMA分区用户', '1', '098f6bcd4621d373cade4e832627b4f6', 'abelli5@qq.com', '13510013001'
, 'http://localhost:8080/docs/images/tomcat.png'
, 'http://localhost:8080/docs/images/asf-logo.svg'
, 'http://localhost:8080/examples/jsp/jsp2/jspx/textRotate.jpg');

INSERT INTO bw_user_role (userId, roleName) VALUES('admin', '管理员');
INSERT INTO bw_user_role (userId, roleName) VALUES('admin', 'ADMINISTRATOR');
-- abel/test
INSERT INTO bw_user_role (userId, roleName) VALUES
('abel', 'POWER_USER'),
('abel', 'BACK_USER'),
('abel', '管理员'),
('abel', 'ADMINISTRATOR');
INSERT INTO bw_user_role (userId, roleName) VALUES
('fuzhou', 'POWER_USER'),
('fuzhou', 'BACK_USER'),
('fuzhou', '管理员'),
('fuzhou', 'ADMINISTRATOR');

-- 库房
INSERT INTO bw_user (userId, userName, firmId, passHash, email, mobile, smallIcon, bigIcon, signPic)
VALUES('luowq', '罗文强', '27', 'e10adc3949ba59abbe56e057f20f883e', 'abelli5@126.com', '13510012001'
      , 'http://localhost:8080/docs/images/tomcat.png'
      , 'http://localhost:8080/docs/images/asf-logo.svg'
      , 'http://localhost:8080/examples/jsp/jsp2/jspx/textRotate.jpg'),
      ('wengdb', '翁达标', '27', 'e10adc3949ba59abbe56e057f20f883e', 'abelli5@126.com', '13510012001'
      , 'http://localhost:8080/docs/images/tomcat.png'
      , 'http://localhost:8080/docs/images/asf-logo.svg'
      , 'http://localhost:8080/examples/jsp/jsp2/jspx/textRotate.jpg'),
      ('linzh', '林增浩', '27', 'e10adc3949ba59abbe56e057f20f883e', 'abelli5@126.com', '13510012001'
      , 'http://localhost:8080/docs/images/tomcat.png'
      , 'http://localhost:8080/docs/images/asf-logo.svg'
      , 'http://localhost:8080/examples/jsp/jsp2/jspx/textRotate.jpg'),
      ('zhangwx', '张伟霞', '27', 'e10adc3949ba59abbe56e057f20f883e', 'abelli5@126.com', '13510012001'
      , 'http://localhost:8080/docs/images/tomcat.png'
      , 'http://localhost:8080/docs/images/asf-logo.svg'
      , 'http://localhost:8080/examples/jsp/jsp2/jspx/textRotate.jpg'),
      ('chenym', '陈炎明', '27', 'e10adc3949ba59abbe56e057f20f883e', 'abelli5@126.com', '13510012001'
      , 'http://localhost:8080/docs/images/tomcat.png'
      , 'http://localhost:8080/docs/images/asf-logo.svg'
      , 'http://localhost:8080/examples/jsp/jsp2/jspx/textRotate.jpg'),
      ('liangqa', '梁清安', '27', 'e10adc3949ba59abbe56e057f20f883e', 'abelli5@126.com', '13510012001'
      , 'http://localhost:8080/docs/images/tomcat.png'
      , 'http://localhost:8080/docs/images/asf-logo.svg'
      , 'http://localhost:8080/examples/jsp/jsp2/jspx/textRotate.jpg'),
      ('luoq', '罗权', '27', 'e10adc3949ba59abbe56e057f20f883e', 'abelli5@126.com', '13510012001'
      , 'http://localhost:8080/docs/images/tomcat.png'
      , 'http://localhost:8080/docs/images/asf-logo.svg'
      , 'http://localhost:8080/examples/jsp/jsp2/jspx/textRotate.jpg'),
      ('k08', '检定员08', '27', 'e10adc3949ba59abbe56e057f20f883e', 'abelli5@126.com', '13510012001'
      , 'http://localhost:8080/docs/images/tomcat.png'
      , 'http://localhost:8080/docs/images/asf-logo.svg'
      , 'http://localhost:8080/examples/jsp/jsp2/jspx/textRotate.jpg');

INSERT INTO bw_user (userId, userName, firmId, passHash, email, mobile, smallIcon, bigIcon, signPic)
VALUES('huangjw', '黄俊威', '27', 'e10adc3949ba59abbe56e057f20f883e', 'abelli5@126.com', '13510012001'
       , 'http://localhost:8080/docs/images/tomcat.png'
       , 'http://localhost:8080/docs/images/asf-logo.svg'
       , 'http://localhost:8080/examples/jsp/jsp2/jspx/textRotate.jpg'),
	   ('zhongqg', '钟启光', '27', 'e10adc3949ba59abbe56e057f20f883e', 'abelli5@126.com', '13510012001'
       , 'http://localhost:8080/docs/images/tomcat.png'
       , 'http://localhost:8080/docs/images/asf-logo.svg'
       , 'http://localhost:8080/examples/jsp/jsp2/jspx/textRotate.jpg'),
	   ('fuxh', '付晓晖', '27', 'e10adc3949ba59abbe56e057f20f883e', 'abelli5@126.com', '13510012001'
       , 'http://localhost:8080/docs/images/tomcat.png'
       , 'http://localhost:8080/docs/images/asf-logo.svg'
       , 'http://localhost:8080/examples/jsp/jsp2/jspx/textRotate.jpg'),
	   ('jianghz', '蒋惠忠', '27', 'e10adc3949ba59abbe56e057f20f883e', 'abelli5@126.com', '13510012001'
       , 'http://localhost:8080/docs/images/tomcat.png'
       , 'http://localhost:8080/docs/images/asf-logo.svg'
       , 'http://localhost:8080/examples/jsp/jsp2/jspx/textRotate.jpg'),
	   ('zhaohy', '赵红艳', '27', 'e10adc3949ba59abbe56e057f20f883e', 'abelli5@126.com', '13510012001'
       , 'http://localhost:8080/docs/images/tomcat.png'
       , 'http://localhost:8080/docs/images/asf-logo.svg'
       , 'http://localhost:8080/examples/jsp/jsp2/jspx/textRotate.jpg'),
	   ('jiangsb', '姜世博', '27', 'e10adc3949ba59abbe56e057f20f883e', 'abelli5@126.com', '13510012001'
       , 'http://localhost:8080/docs/images/tomcat.png'
       , 'http://localhost:8080/docs/images/asf-logo.svg'
       , 'http://localhost:8080/examples/jsp/jsp2/jspx/textRotate.jpg'),
	   ('liangbz', '梁碧舟', '27', 'e10adc3949ba59abbe56e057f20f883e', 'abelli5@126.com', '13510012001'
       , 'http://localhost:8080/docs/images/tomcat.png'
       , 'http://localhost:8080/docs/images/asf-logo.svg'
       , 'http://localhost:8080/examples/jsp/jsp2/jspx/textRotate.jpg');

INSERT INTO bw_user_role (userId, roleName)
VALUES('luowq',		'WARE_JIANDING'),
      ('wengdb',	'WARE_JIANDING'),
      ('linzh',		'WARE_JIANDING'),
      ('zhangwx',	'WARE_JIANDING'),
      ('chenym',	'WARE_JIANDING'),
      ('liangqa',	'WARE_JIANDING'),
      ('luoq',		'WARE_JIANDING'),
      ('k08',		'WARE_JIANDING');

INSERT INTO bw_user_role (userId, roleName)
VALUES
  ('huangjw', 'WARE_AUDIT')
, ('huangjw', 'POWER_USER')
, ('huangjw', 'ADMINISTRATOR')

, ('zhongqg', 'POWER_USER')

, ('fuxh', 'POWER_USER')
, ('fuxh', 'ADMINISTRATOR')

, ('jianghz', 'POWER_USER')
, ('jianghz', 'ADMINISTRATOR')

, ('zhaohy', 'POWER_USER')
, ('zhaohy', 'ADMINISTRATOR')

, ('jiangsb', 'POWER_USER')
, ('jiangsb', 'ADMINISTRATOR')

, ('liangbz', 'POWER_USER')
, ('liangbz', 'ADMINISTRATOR')
;

-- scott/test
-- INSERT INTO bw_user_role (userId, roleName) VALUES('scott', 'DMA_USER');


DELETE FROM bw_config c
WHERE configId IN ('LOSS_R', 'LOSS_LAST_DATE', 'RTU_STATUS_MAIL_LIST', 'LOSS_MINQ_START_HOUR', 'LOSS_MINQ_END_HOUR'
, 'START_WORK_TIME', 'END_WORK_TIME');

INSERT INTO bw_config (configId, groupId, firmId, value, createBy, createDate) VALUES('LOSS_R', 'DATA', '1', '7', 'sys', current_timestamp);
INSERT INTO bw_config (configId, groupId, firmId, value, createBy, createDate) VALUES('LOSS_LAST_DATE', 'DATA', '1', '2014-12-29', 'sys', current_timestamp);

-- RTU status notify users.
-- DELETE FROM bw_config WHERE configId = 'RTU_STATUS_MAIL_LIST' AND groupId = 'DATA';
-- INSERT INTO bw_config (configId, groupId, value) VALUES('RTU_STATUS_MAIL_LIST', 'DATA', 'abelli5@126.com;abelli@139.com;')
INSERT INTO bw_config (configId, groupId, firmId, value, createBy, createDate) VALUES('RTU_STATUS_MAIL_LIST', 'DATA', '1', 'abelli5@126.com;8963095@qq.com;334862512@qq.com;', 'sys', current_timestamp);
-- for loss calculating.
INSERT INTO bw_config (configId, groupId, firmId, value, createBy, createDate) VALUES('LOSS_MINQ_START_HOUR', 'DATA', '1', '2', 'sys', current_timestamp);
INSERT INTO bw_config (configId, groupId, firmId, value, createBy, createDate) VALUES('LOSS_MINQ_END_HOUR', 'DATA', '1', '5', 'sys', current_timestamp);

INSERT INTO bw_config (configId, groupId, firmId, value, configName, configType, createBy, createDate)
VALUES('START_WORK_TIME', 'VERIFY', '27', '9:00', '上班时间', '时间', 'sys', current_timestamp),
('END_WORK_TIME', 'VERIFY', '27', '18:00', '下班时间', '时间', 'sys', current_timestamp);
