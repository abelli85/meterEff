CREATE TEXT SEARCH CONFIGURATION parser_name (PARSER = zhparser); -- 添加配置
ALTER TEXT SEARCH CONFIGURATION parser_name ADD MAPPING FOR n,v,a,i,e,l,j WITH simple; -- 设置分词规则 （n 名词 v 动词等，详情阅读下面的文档）
-- 给某一列的分词结果添加 gin 索引
drop index idx_meter_name_text;
create index idx_meter_name_text on bw_meter using gin(to_tsvector('parser_name', meterName));
drop index idx_meter_loc_text;
create index idx_meter_loc_text on bw_meter using gin(to_tsvector('parser_name', location));

-- 在命令行中使用上一节中介绍的 scws 命令测试分词配置，如我认为复合等级为 7 时分词结果最好，则我在 postgresql.conf添加配置

-- zhparser.multi_short = true    #短词复合: 1
-- zhparser.multi_duality = true  #散字二元复合: 2
-- zhparser.multi_zmain = true    #重要单字复合: 4
-- zhparser.multi_zall = false    #全部单字复合: 8

-- 查询中我们可以使用最简单的 SELECT * FROM table WHERE to_tsvector('parser_name', field) @@ 'word' 来查询 field 字段分词中带有 word 一词的数据；
select meterId, meterName, location from bw_meter where to_tsvector('parser_name', meterName) @@ '维也纳|龙华'
and to_tsvector('parser_name', location) @@ '维也纳|龙华';

select meterId, meterName, location from bw_meter where to_tsvector('parser_name', meterName) @@ to_tsquery('parser_name', '维也纳 龙华')
and to_tsvector('parser_name', location) @@ to_tsquery('parser_name', '维也纳龙华');
select meterId, meterName, location from bw_meter where to_tsvector('parser_name', meterName) @@ to_tsquery('parser_name', '中国福州');
select meterId, meterName, location from bw_meter where to_tsvector('parser_name', meterName) @@ to_tsquery('parser_name', '福州|中国|花园');
select meterId, meterName, location from bw_meter where to_tsvector('parser_name', meterName) @@ to_tsquery('parser_name', '福州&公司');

-- 使用 to_tsquery() 方法将句子解析成各个词的组合向量，如 国家大剧院 的返回结果为 '国家' & '大剧院' & '大剧' & '剧院' ，
-- 当然我们也可以使用 & | 符号拼接自己需要的向量；在查询 长句 时，可以使用
-- SELECT * FROM table WHERE to_tsvector('parser_name', field) @@ to_tsquery('parser_name','words')；

-- 有时候我们想像 MySQL 的 SQL_CALC_FOUND_ROWS 语句一样同步返回结果条数，
-- 则可以使用 SELECT COUNT(*) OVER() AS score FROM table WHERE ...，
-- PgSQL 会在每一行数据添加 score 字段存储查询到的总结果条数；
