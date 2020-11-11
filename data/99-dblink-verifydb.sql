
--创建映射(在pgAdmin中执行)
CREATE USER MAPPING FOR meff
    SERVER verifydb
    OPTIONS (username 'vc', password 'calib');
CREATE USER MAPPING FOR meff
    SERVER huangdb
    OPTIONS (username 'vuser1', password '83045610');

drop foreign table huang_table_verify;
CREATE FOREIGN TABLE huang_table_verify (
    batchId varchar(20),
    serialId int8,
    meterId varchar(20),
    q3r float,
    q2r float,
    q1r float,
    verifyDate timestamptz,
    q3 float,
    q2 float,
    q1 float
    )
    SERVER huangdb
    OPTIONS (query 'SELECT [ID] AS batchId
      ,[序号] AS serialid
      ,[表号] AS meterid
      ,[Q3误差] AS q3r
      ,[Q2误差] AS q2r
      ,[Q1误差] AS q1r
      ,[检定日期] AS verifydate
      ,[常用流量Q3] AS q3
      ,[分界流量Q2] AS q2
      ,[最小流量Q1] AS q1
  FROM [水表检定系统].[dbo].[水表检定记录表明细]');

-- SET ANSI_NULLS ON; SET ANSI_WARNINGS ON;
drop foreign table huang_verify_detail ;
--创建外表
CREATE FOREIGN TABLE huang_verify_detail (
    detailId int8,
    batchId varchar(20),
    serialId int8,
    meterId varchar(20),
    verifyDate timestamptz,
    q3 float,
    q3r float,
    q2 float,
    q2r float,
    q1 float,
    q1r float
)
SERVER verifydb
OPTIONS (table '[huangdb].[dbo].[huang_verify_detail]');

drop foreign table view_batch_test;
CREATE FOREIGN TABLE view_batch_test (
    batch_id varchar(20),
    meter_id varchar(20)
    )
    SERVER verifydb
    OPTIONS (query 'select batch_id,
    meter_id
    from vc_meter_list
    where meter_status IS NULL OR meter_status NOT IN (''ABNORMAL'')');

drop foreign table hg_meter_verify_result;
create foreign table hg_meter_verify_result (
    verifyId             bigint               not null,
    meterId              varchar(45)          not null,
    batchId              varchar(45)          not null,
    tempId               varchar(32)          null,
    verifyDate           TIMESTAMP WITH TIME ZONE not null,
    stuffName            varchar(45)          null,
    verifyResult         varchar(10)          null,
    boardResult          varchar(200)         null,
    forceVerifyNo        varchar(100)         null,
    moduleNo             varchar(100)         null,
    clientName           varchar(100)         null,
    factoryName          varchar(100)         null,
    meterName            varchar(100)         null,
    meterType            varchar(100)         null,
    sizeId               varchar(20)          null,
    modelSize            varchar(100)         null,
    portType             varchar(100)         null,
    verifyRule           varchar(100)         null,
    standardInstrument   varchar(100)         null,
    standardParam        varchar(100)         null,
    indoorTemp           double precision     null,
    moisture             double precision     null,
    validDate            TIMESTAMP WITH TIME ZONE null,
    convertResult        varchar(100)         null,
    pressResult          varchar(100)         null,
    instrumentNo         varchar(100)         null,
    accurateGrade        varchar(100)         null,
    pipeTemp             double precision     null,
    pipePressure         double precision     null,
    pulse                double precision     null,
    density              double precision     null,
    displayDiff          double precision     null,
    convertDiff          double precision     null,
    q4                   double precision     null,
    q3                   double precision     null,
    q3toq1               double precision     null,
    q4toq3               double precision     null,
    q2toq1               double precision     null,
    qr1                  decimal(6,2)         null,
    qr2                  decimal(6,2)         null,
    qr3                  decimal(6,2)         null,
    qr4                  decimal(6,2)         null,
    maxFlow              double precision     null,
    minFlow              double precision     null,
    commonFlow           double precision     null,
    convertLimit         double precision     null,
    verifyAgain          varchar(100)         null,
    outerCheck           varchar(100)         null,
    dataSrc              varchar(100)         null,
    itemId               bigint               null
) server verifydb
options (table 'hg_meter_verify_result');

drop foreign table hg_meter_verify_point;
create foreign table hg_meter_verify_point (
    pointId              bigint               not null,
    pointIdx             int                  null,
    tempId               varchar(32)          not null,
    pointFlow            double precision     not null,
    pointDev             double precision     null,
    highLimit            double precision     null,
    lowLimit             double precision     null,
    exceed               int                  null,
    meterId              varchar(45)          not null,
    verifyDate           TIMESTAMP WITH TIME ZONE not null,
    stuffName            varchar(45)          null,
    boardResult          varchar(100)         null,
    pointName            varchar(20)          null,
    startReading         double precision     null,
    endReading           double precision     null,
    totalVolume          double precision     null,
    verifyDura           double precision     null,
    avgFlow              double precision     null,
    waterTemp            double precision     null,
    startMass            double precision     null,
    endMass              double precision     null,
    standardMass         double precision     null,
    density              double precision     null,
    standardVolume       double precision     null,
    standardDura         double precision     null,
    standardFlow         double precision     null,
    instrumentNo         varchar(100)         null,
    batchId              varchar(100)         null,
    boardMemo            varchar(100)         null,
    itemId               bigint               null
) server verifydb
options (table 'hg_meter_verify_point');

SELECT * FROM hg_meter_verify_result
WHERE meterId LIKE '01115190209874';

SELECT * FROM hg_meter_verify_point
WHERE meterId LIKE '01115190209874';

-- insert into vc_meter_verify
insert into vc_meter_verify_result(
      meterId
    , batchId
    , tempId
    , verifyDate
    , stuffName
    , verifyResult
    , boardResult
    , forceVerifyNo
    , moduleNo
    , clientName
    , factoryName
    , meterName
    , meterType
    , sizeId
    , modelSize
    , portType
    , verifyRule
    , standardInstrument
    , standardParam
    , indoorTemp
    , moisture
    , validDate
    , convertResult
    , pressResult
    , instrumentNo
    , accurateGrade
    , pipeTemp
    , pipePressure
    , pulse
    , density
    , displayDiff
    , convertDiff
    , q4
    , q3
    , q3toq1
    , q4toq3
    , q2toq1
    , qr1
    , qr2
    , qr3
    , qr4
    , maxFlow
    , minFlow
    , commonFlow
    , convertLimit
    , verifyAgain
    , outerCheck
    , dataSrc
    , itemId
)
select meterId
       , batchId
       , tempId
       , verifyDate
       , stuffName
       , verifyResult
       , boardResult
       , forceVerifyNo
       , moduleNo
       , clientName
       , factoryName
       , meterName
       , meterType
       , to_number(sizeId, '99999') sizeId
       , modelSize
       , portType
       , verifyRule
       , standardInstrument
       , standardParam
       , indoorTemp
       , moisture
       , validDate
       , convertResult
       , pressResult
       , instrumentNo
       , accurateGrade
       , pipeTemp
       , pipePressure
       , pulse
       , density
       , displayDiff
       , convertDiff
       , q4
       , q3
       , q3toq1
       , q4toq3
       , q2toq1
       , qr1
       , qr2
       , qr3
       , qr4
       , maxFlow
       , minFlow
       , commonFlow
       , convertLimit
       , verifyAgain
       , outerCheck
       , dataSrc
       , verifyId
from hg_meter_verify_result h
/*
join (select meterid xmeterId, max(verifyDate) xverifyDate
    from hg_meter_verify_result
    group by meterid) hm on h.meterId = hm.xmeterId and h.verifyDate = hm.xverifyDate
*/
where h.verifyId > coalesce((select max(itemid) from vc_meter_verify_result where dataSrc = 'huang'), 0)
;

insert into vc_meter_verify_point(
    pointNo,
    pointFlow,
    pointDev,
    highLimit,
    lowLimit,
    exceed,
    meterId,
    verifyDate,
    stuffName,
    boardResult,
    pointName,
    startReading,
    endReading,
    totalVolume,
    verifyDura,
    avgFlow,
    waterTemp,
    startMass,
    endMass,
    standardMass,
    density,
    standardVolume,
    standardDura,
    standardFlow,
    instrumentNo,
    batchId,
    boardMemo,
    itemId
)
select
    pointIdx,
    pointFlow,
    pointDev,
    highLimit,
    lowLimit,
    exceed,
    meterId,
    verifyDate,
    stuffName,
    boardResult,
    pointName,
    startReading,
    endReading,
    totalVolume,
    verifyDura,
    avgFlow,
    waterTemp,
    startMass,
    endMass,
    standardMass,
    density,
    standardVolume,
    standardDura,
    standardFlow,
    instrumentNo,
    batchId,
    boardMemo,
    pointId
from hg_meter_verify_point pt
where pt.pointId > coalesce((select max(itemId) from vc_meter_verify_point), 0);