
-- \c eff;

-- TRUNCATE TABLE bw_firm;

DELETE FROM bw_firm WHERE firmId = '1';
INSERT INTO bw_firm(firmId, firmName, firmCity) VALUES(1, '智能水务', '深圳');

UPDATE bw_firm SET firmName = '深圳水投' WHERE firmId = '21';
/*if @@ROWCOUNT < 1 begin
	INSERT INTO bw_firm(firmId, firmName) VALUES('21', '深圳水投');
end;
*/
DELETE FROM bw_firm WHERE firmId LIKE '210_';
INSERT INTO bw_firm(firmId, firmName, firmCity) VALUES('2103', '焦作水司', '焦作');
INSERT INTO bw_firm(firmId, firmName, firmCity) VALUES('2105', '池州水司', '池州');
INSERT INTO bw_firm(firmId, firmName, firmCity) VALUES('2106', '坪地水司', '坪地');
INSERT INTO bw_firm(firmId, firmName, firmCity) VALUES('2107', '宣城水司', '宣城');
INSERT INTO bw_firm(firmId, firmName, firmCity) VALUES('2108', '安吉水司', '安吉');
INSERT INTO bw_firm(firmId, firmName, firmCity) VALUES('2109', '宁国水司', '宁国');

DELETE FROM bw_firm WHERE firmId LIKE '26';
INSERT INTO bw_firm(firmId, firmName, firmCity) VALUES('26', '大工业区水务有限公司', '深圳');

-- szwg
DELETE FROM bw_firm WHERE firmId LIKE '27%';
INSERT INTO bw_firm(firmId, firmName, firmCity,
                    firmLoc,
                    smallIcon, largeIcon,
                    addr, contact, phone, phone2, fax, email, memo)
VALUES('27', '深圳水务集团', '深圳',
       'POINT (114.1025009 22.5397596)'::geometry,
       'https://bwlogo.oss-cn-beijing.aliyuncs.com/szwc/szwc-small.png', 'https://bwlogo.oss-cn-beijing.aliyuncs.com/szwc/szwc-large.jpg',
       '深圳市万德大厦', '黄先生', '82137777', '82137777-0', '82137777-1', 'service@waterchina.com',
       '深圳市水务（集团）有限公司（以下简称深圳水务集团）是深圳市国资委、通用首创投资有限公司和法国威立雅水务共同持股经营的中外合资企业，主要经营供排水业务、水务投资业务、水务产业链业务、污泥及废水处理业务和河流生态修复业务。');

/**
  "01-深圳水务集团
02-深水宝安
03-深水龙岗
04-深水龙华
05-深水莲塘
06-大工业区水务"
 */
INSERT INTO bw_firm(firmId, firmName, firmCity, firmLoc, grade)
VALUES
-- ('27',  '深圳水务集团', '深圳', 'POINT(114.1025009 22.5397596)', 0),  '0'),
('2703',  '深水-沙井水司', '深圳', 'POINT(113.834827 22.724587)'::geometry,  '0'),
('2705',  '深水-南山分公司', '深圳', 'POINT(113.9145889 22.5119138)'::geometry, '0'),
('270502',  '南头水务所', '深圳', 'POINT(113.9145889 22.5119138)'::geometry, '0'),
('270504',  '蛇口水务所', '深圳', 'POINT(113.9145889 22.5119138)'::geometry, '0'),
('270506',  '西丽水务所', '深圳', 'POINT(113.9145889 22.5119138)'::geometry, '0'),
('270508',  '粤海水务所', '深圳', 'POINT(113.9145889 22.5119138)'::geometry, '0'),
('270510',  '前海水务所', '深圳', NULL,  '0'),
('2707',  '深水-盐田分公司', '深圳', 'POINT(114.2422791 22.5625711)'::geometry, '0'),
('2709',  '深水-罗湖分公司', '深圳', NULL,  '0'),
('270902',  '笋岗水务所',    '深圳', NULL,  '0'),
('270904',  '清水河水务所',  '深圳', NULL,  '0'),
('270906',  '黄贝水务所', '深圳', NULL,  '0'),
('2711',  '深水-福田分公司', '深圳', NULL,  '0'),
('271102',  '梅林水务所', '深圳', NULL,  '0'),
('271104',  '香蜜水务所', '深圳', NULL,  '0'),
('271106',  '福中水务所', '深圳', NULL,  '0'),
('271108',  '福东水务所', '深圳', NULL,  '0'),
('2713',  '深水-龙岗分公司', '深圳', NULL,  '0')
;

DELETE FROM bw_firm WHERE firmId LIKE '29%';
INSERT INTO bw_firm(firmId, firmName, firmLoc)
VALUES('29', '东莞市水务集团', 'POINT(113.6983740000 22.9912870000)'::geometry);
INSERT INTO bw_firm(firmId, firmName, firmLoc)
VALUES('2901', '东江自来水公司', 'POINT(113.7563000000 23.0276300000)'::geometry);
INSERT INTO bw_firm(firmId, firmName, firmLoc)
VALUES('290101', '东江-万江分公司', 'POINT(113.7350600000 23.0576800000)'::geometry);

DELETE FROM bw_firm WHERE firmId LIKE '71';
INSERT INTO bw_firm
(firmId,
firmName,
firmLoc,
smallIcon,
largeIcon)
VALUES
('71',
'开平水务有限公司',
'POINT (112.6985100000 22.3763800000)'::geometry,
'https://bwlogo.oss-cn-beijing.aliyuncs.com/szwc/szwc-small.png',
'https://bwlogo.oss-cn-beijing.aliyuncs.com/szwc/szwc-large.jpg');

/*

TRUNCATE TABLE bw_firm;

SELECT * FROM bw_firm;

SELECT firmId, firmName, firmLoc.STAsText() firmLoc FROM bw_firm
where firmId like '27%';

*/

DELETE FROM bw_firm WHERE firmId LIKE '76%';
INSERT INTO bw_firm(firmId, firmName, firmCity, firmLoc)
VALUES('76', '福州市自来水公司', '福州', 'POINT(119.31145882543946 26.07921305191548)'::geometry);
INSERT INTO bw_firm(firmId, firmName, firmCity, firmLoc) VALUES
('7620', '供水服务一公司（鼓楼-20）', '福州', 'POINT(119.31145882543946 26.07921305191548)'::geometry),
('7621', '供水服务三公司（晋安-21）', '福州', 'POINT(119.31145882543946 26.07921305191548)'::geometry),
('7622', '供水服务四公司（仓山-22）', '福州', 'POINT(119.31145882543946 26.07921305191548)'::geometry),
('7623', '供水服务五公司（金山-23）', '福州', 'POINT(119.31145882543946 26.07921305191548)'::geometry),
('7625', '供水服务二公司（台江-25）', '福州', 'POINT(119.31145882543946 26.07921305191548)'::geometry);
