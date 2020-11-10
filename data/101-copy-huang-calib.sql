USE [calib]
GO

/****** Object:  Table [dbo].[huang_table_verify_detail]    Script Date: 11/10/2020 19:56:59 ******/
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[huang_table_verify_detail]') AND type in (N'U'))
DROP TABLE [dbo].[huang_table_verify_detail]
GO

USE [calib]
GO

/****** Object:  Table [dbo].[huang_table_verify_detail]    Script Date: 11/10/2020 19:56:59 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[huang_table_verify_detail](
	[ID] [nvarchar](15) NOT NULL,
	[序号] [int] NULL,
	[表号前缀] [nvarchar](20) NULL,
	[表号] [nvarchar](20) NULL,
	[Q3始值] [float] NULL,
	[Q3末值] [float] NULL,
	[Q3Vi] [float] NULL,
	[Q3示值] [float] NULL,
	[Q3误差] [float] NULL,
	[Q2始值] [float] NULL,
	[Q2末值] [float] NULL,
	[Q2Vi] [float] NULL,
	[Q2示值] [float] NULL,
	[Q2误差] [float] NULL,
	[Q1始值] [float] NULL,
	[Q1末值] [float] NULL,
	[Q1Vi] [float] NULL,
	[Q1示值] [float] NULL,
	[Q1误差] [float] NULL,
	[始动流量] [real] NULL,
	[外观] [nvarchar](8) NULL,
	[密封性] [nvarchar](8) NULL,
	[检定结果] [nvarchar](10) NULL,
	[备注] [nvarchar](50) NULL,
	[检定员] [nvarchar](4) NULL,
	[检定日期] [datetime] NULL,
	[设备编号] [nvarchar](10) NULL,
	[水表口径] [nvarchar](5) NULL,
	[ID重检] [nvarchar](18) NULL,
	[时间] [nvarchar](10) NULL,
	[铅封号] [nvarchar](20) NULL,
	[表底] [real] NULL,
	[环境温度] [real] NULL,
	[相对湿度] [real] NULL,
	[介质水温] [real] NULL,
	[水压] [real] NULL,
	[电子签名] [bit] NOT NULL,
	[N] [int] NOT NULL,
	[Q1T1] [real] NULL,
	[Q2T1] [real] NULL,
	[Q3T1] [real] NULL,
	[Q3T2] [real] NULL,
	[Q2T2] [real] NULL,
	[Q1T2] [real] NULL,
	[Q3标准质量] [real] NULL,
	[Q2标准质量] [real] NULL,
	[Q1标准质量] [real] NULL,
	[Q3密度] [real] NULL,
	[Q2密度] [real] NULL,
	[Q1密度] [real] NULL,
	[常用流量Q3] [float] NULL,
	[分界流量Q2] [float] NULL,
	[最小流量Q1] [float] NULL,
	[Vm1] [float] NULL,
	[Vm2] [float] NULL,
	[Ve1] [float] NULL,
	[Ve2] [float] NULL,
	[机电误差] [nvarchar](20) NULL,
	[机电结果] [nchar](10) NULL,
	[机电误差限] [float] NULL
) ON [PRIMARY]

GO

USE [calib]
GO

/****** Object:  Table [dbo].[huang_view_verify_detail]    Script Date: 11/10/2020 19:57:19 ******/
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[huang_view_verify_detail]') AND type in (N'U'))
DROP TABLE [dbo].[huang_view_verify_detail]
GO

USE [calib]
GO

/****** Object:  Table [dbo].[huang_view_verify_detail]    Script Date: 11/10/2020 19:57:19 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[huang_view_verify_detail](
	[batchId] [nvarchar](15) NOT NULL,
	[serialId] [int] NULL,
	[meterId] [nvarchar](20) NULL,
	[verifyDate] [datetime] NULL,
	[q3] [float] NULL,
	[q3r] [float] NULL,
	[q2] [float] NULL,
	[q2r] [float] NULL,
	[q1] [float] NULL,
	[q1r] [float] NULL
) ON [PRIMARY]

GO




-- fetch range
select MAX([序号]), MIN([序号]), MAX(N), MIN(N)
FROM huang_table_verify_detail;
GO

INSERT INTO [calib].[dbo].[hg_meter_verify_point]
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
WHERE [N] BETWEEN 1 AND 1105270
GO

-- fetch range
select MAX([序号]), MIN([序号]), MAX(N), MIN(N)
FROM huang_table_verify_detail;
GO

INSERT INTO [calib].[dbo].[hg_meter_verify_point]
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
WHERE [N] BETWEEN 1 AND 1105270
GO



-- fetch range for verify result
select MAX([序号]), MIN([序号]), MAX(N), MIN(N)
FROM huang_table_verify_detail;
GO

INSERT INTO [calib].[dbo].[hg_meter_verify_point]
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
WHERE [N] BETWEEN 1 AND 1105270
GO

INSERT INTO [calib].[dbo].[hg_meter_verify_result]
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
GO







SELECT COUNT(1) FROM hg_meter_verify_point;

SELECT * FROM hg_meter_verify_point
WHERE meterId LIKE '01115190209874'
GO
