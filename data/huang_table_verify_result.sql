USE [huangdb]
GO

/****** Object:  Table [dbo].[huang_table_verify_result]    Script Date: 11/12/2020 17:24:45 ******/
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[huang_table_verify_result]') AND type in (N'U'))
DROP TABLE [dbo].[huang_table_verify_result]
GO

USE [huangdb]
GO

/****** Object:  Table [dbo].[huang_table_verify_result]    Script Date: 11/12/2020 17:24:45 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[huang_table_verify_result](
	[ID] [nvarchar](15) NOT NULL,
	[委托单号] [nvarchar](50) NULL,
	[送检单位] [nvarchar](70) NULL,
	[制造单位] [nvarchar](40) NULL,
	[品牌] [nvarchar](15) NULL,
	[型号规格] [nvarchar](25) NULL,
	[等级] [nvarchar](6) NULL,
	[数量] [int] NULL,
	[常用流量Q3] [float] NULL,
	[分界流量Q2] [float] NULL,
	[最小流量Q1] [float] NULL,
	[设备编号] [nvarchar](10) NULL,
	[技术依据和规程] [nvarchar](150) NULL,
	[环境温度] [real] NULL,
	[相对湿度] [real] NULL,
	[介质水温] [real] NULL,
	[水压] [real] NULL,
	[是否首检] [bit] NOT NULL,
	[检定员] [nvarchar](4) NULL,
	[完成日期] [datetime] NULL,
	[接单日期] [datetime] NULL,
	[地址] [nvarchar](50) NULL,
	[出厂编号] [nvarchar](30) NULL,
	[委托类型] [nvarchar](10) NULL,
	[委托类别] [nvarchar](10) NULL,
	[总数] [int] NULL,
	[完成] [int] NULL,
	[带铅封] [bit] NOT NULL,
	[需重检] [bit] NOT NULL,
	[标准器名特征] [nvarchar](30) NULL,
	[标准器证书号] [nvarchar](30) NULL,
	[水表名称] [nvarchar](30) NULL,
	[水表类型] [nvarchar](20) NULL,
	[类别] [nvarchar](2) NULL,
	[备注] [nvarchar](255) NULL,
	[Q3Q1] [real] NULL,
	[Q2Q1] [real] NULL,
	[优先标签] [nvarchar](20) NULL,
	[使用单位] [nvarchar](100) NULL,
	[订单号] [nvarchar](30) NULL,
	[核验员] [nvarchar](10) NULL,
	[口径] [int] NULL,
	[有效期] [datetime] NULL
) ON [PRIMARY]

GO
USE [huangdb]
GO

/****** Object:  Table [dbo].[huang_table_verify_detail]    Script Date: 11/10/2020 19:56:59 ******/
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[huang_table_verify_detail]') AND type in (N'U'))
DROP TABLE [dbo].[huang_table_verify_detail]
GO

USE [huangdb]
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

USE [huangdb]
GO

/****** Object:  Table [dbo].[huang_view_verify_detail]    Script Date: 11/10/2020 19:57:19 ******/
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[huang_view_verify_detail]') AND type in (N'U'))
DROP TABLE [dbo].[huang_view_verify_detail]
GO

USE [huangdb]
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



-- ------------------------------------
truncate table huang_table_verify_detail;
drop table huang_table_verify_detail;
SELECT [ID]
      ,[序号]
      ,[表号前缀]
      ,[表号]
      ,[Q3始值]
      ,[Q3末值]
      ,[Q3Vi]
      ,[Q3示值]
      ,[Q3误差]
      ,[Q2始值]
      ,[Q2末值]
      ,[Q2Vi]
      ,[Q2示值]
      ,[Q2误差]
      ,[Q1始值]
      ,[Q1末值]
      ,[Q1Vi]
      ,[Q1示值]
      ,[Q1误差]
      ,[始动流量]
      ,[外观]
      ,[密封性]
      ,[检定结果]
      ,[备注]
      ,[检定员]
      ,[检定日期]
      ,[设备编号]
      ,[水表口径]
      ,[ID重检]
      ,[时间]
      ,[铅封号]
      ,[表底]
      ,[环境温度]
      ,[相对湿度]
      ,[介质水温]
      ,[水压]
      ,[电子签名]
      ,[N]
      ,[Q1T1]
      ,[Q2T1]
      ,[Q3T1]
      ,[Q3T2]
      ,[Q2T2]
      ,[Q1T2]
      ,[Q3标准质量]
      ,[Q2标准质量]
      ,[Q1标准质量]
      ,[Q3密度]
      ,[Q2密度]
      ,[Q1密度]
      ,[常用流量Q3]
      ,[分界流量Q2]
      ,[最小流量Q1]
      ,[Vm1]
      ,[Vm2]
      ,[Ve1]
      ,[Ve2]
      ,[机电误差]
      ,[机电结果]
      ,[机电误差限]
	INTO huang_table_verify_detail
  FROM [huang].[水表检定系统].[dbo].[水表检定记录表明细]
GO


DROP index idx_huang_table_meter_time on huang_table_verify_detail;
create index idx_huang_table_meter_time on huang_table_verify_detail(
[表号],
[检定日期]);
GO

DROP INDEX  idx_huang_table_serial on huang_table_verify_detail;
CREATE INDEX idx_huang_table_serial on huang_table_verify_detail(
[N]);
GO

-- test
select COUNT(1) from huang_table_verify_detail;
GO
SELECT * FROM huang_table_verify_detail
WHERE [表号] LIKE '01115190209874'
GO

-- ------------------------------------
truncate table huang_view_verify_detail;
drop table huang_view_verify_detail;
SELECT [batchId]
	,[serialId]
	,[meterId]
	,[verifyDate]
	,[q3]
	,[q3r]
	,[q2]
	,[q2r]
	,[q1]
	,[q1r]
INTO huang_view_verify_detail
FROM [huang].[水表检定系统].[dbo].[view_verify_detail]
GO

CREATE INDEX idx_huang_view_meter_time ON huang_view_verify_detail(meterId, verifyDate);
GO

select COUNT(1) from huang_view_verify_detail;
SELECT * FROM huang_view_verify_detail
where meterid like '0112519020000%';
GO

truncate table [huang_table_verify_result];
drop table [huang_table_verify_result];
SELECT [ID]
      ,[委托单号]
      ,[送检单位]
      ,[制造单位]
      ,[品牌]
      ,[型号规格]
      ,[等级]
      ,[数量]
      ,[常用流量Q3]
      ,[分界流量Q2]
      ,[最小流量Q1]
      ,[设备编号]
      ,[技术依据和规程]
      ,[环境温度]
      ,[相对湿度]
      ,[介质水温]
      ,[水压]
      ,[是否首检]
      ,[检定员]
      ,[完成日期]
      ,[接单日期]
      ,[地址]
      ,[出厂编号]
      ,[委托类型]
      ,[委托类别]
      ,[总数]
      ,[完成]
      ,[带铅封]
      ,[需重检]
      ,[标准器名特征]
      ,[标准器证书号]
      ,[水表名称]
      ,[水表类型]
      ,[类别]
      ,[备注]
      ,[Q3Q1]
      ,[Q2Q1]
      ,[优先标签]
      ,[使用单位]
      ,[订单号]
      ,[核验员]
      ,[口径]
      ,[有效期]
into [huang_table_verify_result]
  FROM [huang].[水表检定系统].[dbo].[水表检定记录表历史]
GO

drop index idx_huang_vr_batch on [huang_table_verify_result];
create index idx_huang_vr_batch on [huang_table_verify_result]([委托单号], [ID]);

drop index idx_huang_vr_meter on [huang_table_verify_result];
create index idx_huang_vr_meter on [huang_table_verify_result]([ID], [完成日期]);

GO

select COUNT(1)
from [huang_table_verify_result];

