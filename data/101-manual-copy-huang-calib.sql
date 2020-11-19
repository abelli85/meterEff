
update huang_table_verify_detail
set [最小流量Q1] = vr.[最小流量Q1],
[常用流量Q3] = vr.[常用流量Q3],
[分界流量Q2] = vr.[分界流量Q2]
from huang_table_verify_result vr,
huang_table_verify_detail vd
where vr.[出厂编号] = vd.[表号]
and vr.[常用流量Q3] is not null
and vd.[常用流量Q3] is null
GO

-- ------------------------------------



-- fetch range
select MAX([序号]), MIN([序号]), MAX(N), MIN(N)
FROM huang_table_verify_detail;
GO

truncate table hg_meter_verify_point;
truncate table hg_meter_verify_result;
GO

-- Q3
INSERT INTO [huangdb].[dbo].[hg_meter_verify_point]
           ([pointIdx]
           ,[tempId]
           ,[pointFlow]
           ,[pointDev]
           ,[highLimit]
           ,[lowLimit]
           ,[exceed]
           ,[meterId]
           ,[verifyDate]
           ,[stuffName]
           ,[boardResult]
           ,[pointName]
           ,[startReading]
           ,[endReading]
           ,[totalVolume]
           ,[verifyDura]
           ,[avgFlow]
           ,[waterTemp]
           ,[startMass]
           ,[endMass]
           ,[standardMass]
           ,[density]
           ,[standardVolume]
           ,[standardDura]
           ,[standardFlow]
           ,[instrumentNo]
           ,[batchId]
           ,[boardMemo]
           ,[itemId])
     SELECT
           3 AS pointIdx
           ,'huang' tempId
           ,ISNULL([常用流量Q3], 0) pointFlow
           ,[Q3误差] pointDev
           ,NULL highLimit
           ,NULL lowLimit
           ,0 exceed
           ,ISNULL([表号], '0') meterId
           ,ISNULL([检定日期], '2000-1-1') verifyDate
           ,[检定员] stuffName
           ,[检定结果] boardResult
           ,'Q3' pointName
           ,[Q3始值] startReading
           ,[Q3末值] endReading
           ,[Q3示值] totalVolume
           ,NULL verifyDura
           ,NULL avgFlow
           ,[环境温度] waterTemp
           ,NULL startMass
           ,NULL endMass
           ,[Q3标准质量] standardMass
           ,[Q3密度] density
           ,NULL standardVolume
           ,NULL standardDura
           ,NULL standardFlow
           ,[设备编号] instrumentNo
           ,[ID] batchId
           ,ISNULL([检定结果], '') + '|' + ISNULL([水表口径], '') + '|' + ISNULL([外观],'') + '|' + ISNULL([密封性], '') + '|' + ISNULL([机电结果], '') boardMemo
           ,[N] itemId
FROM huang_table_verify_detail
WHERE [N] BETWEEN 1 AND 1107714
GO

-- fetch range
select MAX([序号]), MIN([序号]), MAX(N), MIN(N)
FROM huang_table_verify_detail;
GO

-- Q2
INSERT INTO [huangdb].[dbo].[hg_meter_verify_point]
           ([pointIdx]
           ,[tempId]
           ,[pointFlow]
           ,[pointDev]
           ,[highLimit]
           ,[lowLimit]
           ,[exceed]
           ,[meterId]
           ,[verifyDate]
           ,[stuffName]
           ,[boardResult]
           ,[pointName]
           ,[startReading]
           ,[endReading]
           ,[totalVolume]
           ,[verifyDura]
           ,[avgFlow]
           ,[waterTemp]
           ,[startMass]
           ,[endMass]
           ,[standardMass]
           ,[density]
           ,[standardVolume]
           ,[standardDura]
           ,[standardFlow]
           ,[instrumentNo]
           ,[batchId]
           ,[boardMemo]
           ,[itemId])
     SELECT
           2 AS pointIdx
           ,'huang' tempId
           ,ISNULL([分界流量Q2], 0) pointFlow
           ,[Q2误差] pointDev
           ,NULL highLimit
           ,NULL lowLimit
           ,0 exceed
           ,ISNULL([表号], '0') meterId
           ,ISNULL([检定日期], '2000-1-1') verifyDate
           ,[检定员] stuffName
           ,[检定结果] boardResult
           ,'Q2' pointName
           ,[Q2始值] startReading
           ,[Q2末值] endReading
           ,[Q2示值] totalVolume
           ,NULL verifyDura
           ,NULL avgFlow
           ,[环境温度] waterTemp
           ,NULL startMass
           ,NULL endMass
           ,[Q2标准质量] standardMass
           ,[Q2密度] density
           ,NULL standardVolume
           ,NULL standardDura
           ,NULL standardFlow
           ,[设备编号] instrumentNo
           ,[ID] batchId
           ,ISNULL([检定结果], '') + '|' + ISNULL([水表口径], '') + '|' + ISNULL([外观],'') + '|' + ISNULL([密封性], '') + '|' + ISNULL([机电结果], '') boardMemo
           ,[N] itemId
FROM huang_table_verify_detail
WHERE [N] BETWEEN 1 AND 1107714
GO



-- fetch range for verify result
select MAX([序号]), MIN([序号]), MAX(N), MIN(N)
FROM huang_table_verify_detail;
GO

-- Q1
INSERT INTO [huangdb].[dbo].[hg_meter_verify_point]
           ([pointIdx]
           ,[tempId]
           ,[pointFlow]
           ,[pointDev]
           ,[highLimit]
           ,[lowLimit]
           ,[exceed]
           ,[meterId]
           ,[verifyDate]
           ,[stuffName]
           ,[boardResult]
           ,[pointName]
           ,[startReading]
           ,[endReading]
           ,[totalVolume]
           ,[verifyDura]
           ,[avgFlow]
           ,[waterTemp]
           ,[startMass]
           ,[endMass]
           ,[standardMass]
           ,[density]
           ,[standardVolume]
           ,[standardDura]
           ,[standardFlow]
           ,[instrumentNo]
           ,[batchId]
           ,[boardMemo]
           ,[itemId])
     SELECT
           1 AS pointIdx
           ,'huang' tempId
           ,ISNULL([最小流量Q1], 0) pointFlow
           ,[Q1误差] pointDev
           ,NULL highLimit
           ,NULL lowLimit
           ,0 exceed
           ,ISNULL([表号], '0') meterId
           ,ISNULL([检定日期], '2000-1-1') verifyDate
           ,[检定员] stuffName
           ,[检定结果] boardResult
           ,'Q1' pointName
           ,[Q1始值] startReading
           ,[Q1末值] endReading
           ,[Q1示值] totalVolume
           ,NULL verifyDura
           ,NULL avgFlow
           ,[环境温度] waterTemp
           ,NULL startMass
           ,NULL endMass
           ,[Q1标准质量] standardMass
           ,[Q1密度] density
           ,NULL standardVolume
           ,NULL standardDura
           ,NULL standardFlow
           ,[设备编号] instrumentNo
           ,[ID] batchId
           ,ISNULL([检定结果], '') + '|' + ISNULL([水表口径], '') + '|' + ISNULL([外观],'') + '|' + ISNULL([密封性], '') + '|' + ISNULL([机电结果], '') boardMemo
           ,[N] itemId
FROM huang_table_verify_detail
WHERE [N] BETWEEN 1 AND 1107714
GO

-- summary of verify.
INSERT INTO [huangdb].[dbo].[hg_meter_verify_result]
           ([meterId]
           ,[batchId]
           ,[tempId]
           ,[verifyDate]
           ,[stuffName]
           ,[verifyResult]
           ,[boardResult]
           ,[forceVerifyNo]
           ,[moduleNo]
           ,[clientName]
           ,[factoryName]
           ,[meterName]
           ,[meterType]
           ,[sizeId]
           ,[modelSize]
           ,[portType]
           ,[verifyRule]
           ,[standardInstrument]
           ,[standardParam]
           ,[indoorTemp]
           ,[moisture]
           ,[validDate]
           ,[convertResult]
           ,[pressResult]
           ,[instrumentNo]
           ,[accurateGrade]
           ,[pipeTemp]
           ,[pipePressure]
           ,[pulse]
           ,[density]
           ,[displayDiff]
           ,[convertDiff]
           ,[q4]
           ,[q3]
           ,[q3toq1]
           ,[q4toq3]
           ,[q2toq1]
           ,[qr1]
           ,[qr2]
           ,[qr3]
           ,[qr4]
           ,[maxFlow]
           ,[minFlow]
           ,[commonFlow]
           ,[convertLimit]
           ,[verifyAgain]
           ,[outerCheck]
           ,[dataSrc]
           ,[itemId])
     SELECT
           ISNULL([表号], '0') meterId
           ,ISNULL([ID], '0') batchId
           ,'huang' tempId
           ,ISNULL([检定日期], '2000-1-1') verifyDate
           ,[检定员] stuffName
           ,[检定结果] verifyResult
           ,ISNULL([检定结果], '') + '|' + ISNULL([水表口径], '') + '|' + ISNULL([外观],'') + '|' + ISNULL([密封性], '') + '|' + ISNULL([机电结果], '') boardResult
           ,[铅封号] forceVerifyNo
           ,[电子签名] moduleNo
           ,NULL clientName
           ,NULL factoryName
           ,[表底] meterName
           ,NULL meterType
           ,[水表口径] sizeId
           ,NULL modelSize
           ,NULL portType
           ,NULL verifyRule
           ,NULL standardInstrument
           ,NULL standardParam
           ,[环境温度] indoorTemp
           ,[相对湿度] moisture
           ,NULL validDate
           ,[机电结果] convertResult
           ,[密封性] pressResult
           ,[设备编号] instrumentNo
           ,NULL accurateGrade
           ,NULL pipeTemp
           ,NULL pipePressure
           ,[水压] pulse
           ,[Q3密度] density
           ,[Q3误差] displayDiff
           ,(CASE WHEN ISNUMERIC([机电误差]) = 1 THEN [机电误差] ELSE NULL END) convertDiff
           ,NULL q4
           ,[常用流量Q3] q3
           ,NULL q3toq1
           ,NULL q4toq3
           ,NULL q2toq1
           ,(CASE WHEN [Q1误差] > 999 THEN 999 WHEN [Q1误差] < -999 THEN -999 ELSE [Q1误差] END) qr1
           ,(CASE WHEN [Q2误差] > 999 THEN 999 WHEN [Q2误差] < -999 THEN -999 ELSE [Q2误差] END) qr2
           ,(CASE WHEN [Q3误差] > 999 THEN 999 WHEN [Q3误差] < -999 THEN -999 ELSE [Q3误差] END) qr3
           ,NULL qr4
           ,NULL maxFlow
           ,NULL minFlow
           ,NULL commonFlow
           ,[机电误差限] convertLimit
           ,[ID重检] verifyAgain
           ,[外观] outerCheck
           ,'huang' dataSrc
           ,[N] itemId
FROM huang_table_verify_detail
WHERE [N] BETWEEN 1 AND 1107714
GO






SELECT COUNT(1) FROM hg_meter_verify_result;

SELECT * FROM hg_meter_verify_result
WHERE meterId LIKE '01115190209874'
GO


SELECT COUNT(1) FROM hg_meter_verify_point;

SELECT * FROM hg_meter_verify_point
WHERE meterId LIKE '01115190209874'
GO
