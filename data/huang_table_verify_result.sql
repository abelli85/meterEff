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
	[ί�е���] [nvarchar](50) NULL,
	[�ͼ쵥λ] [nvarchar](70) NULL,
	[���쵥λ] [nvarchar](40) NULL,
	[Ʒ��] [nvarchar](15) NULL,
	[�ͺŹ��] [nvarchar](25) NULL,
	[�ȼ�] [nvarchar](6) NULL,
	[����] [int] NULL,
	[��������Q3] [float] NULL,
	[�ֽ�����Q2] [float] NULL,
	[��С����Q1] [float] NULL,
	[�豸���] [nvarchar](10) NULL,
	[�������ݺ͹��] [nvarchar](150) NULL,
	[�����¶�] [real] NULL,
	[���ʪ��] [real] NULL,
	[����ˮ��] [real] NULL,
	[ˮѹ] [real] NULL,
	[�Ƿ��׼�] [bit] NOT NULL,
	[�춨Ա] [nvarchar](4) NULL,
	[�������] [datetime] NULL,
	[�ӵ�����] [datetime] NULL,
	[��ַ] [nvarchar](50) NULL,
	[�������] [nvarchar](30) NULL,
	[ί������] [nvarchar](10) NULL,
	[ί�����] [nvarchar](10) NULL,
	[����] [int] NULL,
	[���] [int] NULL,
	[��Ǧ��] [bit] NOT NULL,
	[���ؼ�] [bit] NOT NULL,
	[��׼��������] [nvarchar](30) NULL,
	[��׼��֤���] [nvarchar](30) NULL,
	[ˮ������] [nvarchar](30) NULL,
	[ˮ������] [nvarchar](20) NULL,
	[���] [nvarchar](2) NULL,
	[��ע] [nvarchar](255) NULL,
	[Q3Q1] [real] NULL,
	[Q2Q1] [real] NULL,
	[���ȱ�ǩ] [nvarchar](20) NULL,
	[ʹ�õ�λ] [nvarchar](100) NULL,
	[������] [nvarchar](30) NULL,
	[����Ա] [nvarchar](10) NULL,
	[�ھ�] [int] NULL,
	[��Ч��] [datetime] NULL
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
	[���] [int] NULL,
	[���ǰ׺] [nvarchar](20) NULL,
	[���] [nvarchar](20) NULL,
	[Q3ʼֵ] [float] NULL,
	[Q3ĩֵ] [float] NULL,
	[Q3Vi] [float] NULL,
	[Q3ʾֵ] [float] NULL,
	[Q3���] [float] NULL,
	[Q2ʼֵ] [float] NULL,
	[Q2ĩֵ] [float] NULL,
	[Q2Vi] [float] NULL,
	[Q2ʾֵ] [float] NULL,
	[Q2���] [float] NULL,
	[Q1ʼֵ] [float] NULL,
	[Q1ĩֵ] [float] NULL,
	[Q1Vi] [float] NULL,
	[Q1ʾֵ] [float] NULL,
	[Q1���] [float] NULL,
	[ʼ������] [real] NULL,
	[���] [nvarchar](8) NULL,
	[�ܷ���] [nvarchar](8) NULL,
	[�춨���] [nvarchar](10) NULL,
	[��ע] [nvarchar](50) NULL,
	[�춨Ա] [nvarchar](4) NULL,
	[�춨����] [datetime] NULL,
	[�豸���] [nvarchar](10) NULL,
	[ˮ��ھ�] [nvarchar](5) NULL,
	[ID�ؼ�] [nvarchar](18) NULL,
	[ʱ��] [nvarchar](10) NULL,
	[Ǧ���] [nvarchar](20) NULL,
	[���] [real] NULL,
	[�����¶�] [real] NULL,
	[���ʪ��] [real] NULL,
	[����ˮ��] [real] NULL,
	[ˮѹ] [real] NULL,
	[����ǩ��] [bit] NOT NULL,
	[N] [int] NOT NULL,
	[Q1T1] [real] NULL,
	[Q2T1] [real] NULL,
	[Q3T1] [real] NULL,
	[Q3T2] [real] NULL,
	[Q2T2] [real] NULL,
	[Q1T2] [real] NULL,
	[Q3��׼����] [real] NULL,
	[Q2��׼����] [real] NULL,
	[Q1��׼����] [real] NULL,
	[Q3�ܶ�] [real] NULL,
	[Q2�ܶ�] [real] NULL,
	[Q1�ܶ�] [real] NULL,
	[��������Q3] [float] NULL,
	[�ֽ�����Q2] [float] NULL,
	[��С����Q1] [float] NULL,
	[Vm1] [float] NULL,
	[Vm2] [float] NULL,
	[Ve1] [float] NULL,
	[Ve2] [float] NULL,
	[�������] [nvarchar](20) NULL,
	[������] [nchar](10) NULL,
	[���������] [float] NULL
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
      ,[���]
      ,[���ǰ׺]
      ,[���]
      ,[Q3ʼֵ]
      ,[Q3ĩֵ]
      ,[Q3Vi]
      ,[Q3ʾֵ]
      ,[Q3���]
      ,[Q2ʼֵ]
      ,[Q2ĩֵ]
      ,[Q2Vi]
      ,[Q2ʾֵ]
      ,[Q2���]
      ,[Q1ʼֵ]
      ,[Q1ĩֵ]
      ,[Q1Vi]
      ,[Q1ʾֵ]
      ,[Q1���]
      ,[ʼ������]
      ,[���]
      ,[�ܷ���]
      ,[�춨���]
      ,[��ע]
      ,[�춨Ա]
      ,[�춨����]
      ,[�豸���]
      ,[ˮ��ھ�]
      ,[ID�ؼ�]
      ,[ʱ��]
      ,[Ǧ���]
      ,[���]
      ,[�����¶�]
      ,[���ʪ��]
      ,[����ˮ��]
      ,[ˮѹ]
      ,[����ǩ��]
      ,[N]
      ,[Q1T1]
      ,[Q2T1]
      ,[Q3T1]
      ,[Q3T2]
      ,[Q2T2]
      ,[Q1T2]
      ,[Q3��׼����]
      ,[Q2��׼����]
      ,[Q1��׼����]
      ,[Q3�ܶ�]
      ,[Q2�ܶ�]
      ,[Q1�ܶ�]
      ,[��������Q3]
      ,[�ֽ�����Q2]
      ,[��С����Q1]
      ,[Vm1]
      ,[Vm2]
      ,[Ve1]
      ,[Ve2]
      ,[�������]
      ,[������]
      ,[���������]
	INTO huang_table_verify_detail
  FROM [huang].[ˮ��춨ϵͳ].[dbo].[ˮ��춨��¼����ϸ]
GO


DROP index idx_huang_table_meter_time on huang_table_verify_detail;
create index idx_huang_table_meter_time on huang_table_verify_detail(
[���],
[�춨����]);
GO

DROP INDEX  idx_huang_table_serial on huang_table_verify_detail;
CREATE INDEX idx_huang_table_serial on huang_table_verify_detail(
[N]);
GO

-- test
select COUNT(1) from huang_table_verify_detail;
GO
SELECT * FROM huang_table_verify_detail
WHERE [���] LIKE '01115190209874'
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
FROM [huang].[ˮ��춨ϵͳ].[dbo].[view_verify_detail]
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
      ,[ί�е���]
      ,[�ͼ쵥λ]
      ,[���쵥λ]
      ,[Ʒ��]
      ,[�ͺŹ��]
      ,[�ȼ�]
      ,[����]
      ,[��������Q3]
      ,[�ֽ�����Q2]
      ,[��С����Q1]
      ,[�豸���]
      ,[�������ݺ͹��]
      ,[�����¶�]
      ,[���ʪ��]
      ,[����ˮ��]
      ,[ˮѹ]
      ,[�Ƿ��׼�]
      ,[�춨Ա]
      ,[�������]
      ,[�ӵ�����]
      ,[��ַ]
      ,[�������]
      ,[ί������]
      ,[ί�����]
      ,[����]
      ,[���]
      ,[��Ǧ��]
      ,[���ؼ�]
      ,[��׼��������]
      ,[��׼��֤���]
      ,[ˮ������]
      ,[ˮ������]
      ,[���]
      ,[��ע]
      ,[Q3Q1]
      ,[Q2Q1]
      ,[���ȱ�ǩ]
      ,[ʹ�õ�λ]
      ,[������]
      ,[����Ա]
      ,[�ھ�]
      ,[��Ч��]
into [huang_table_verify_result]
  FROM [huang].[ˮ��춨ϵͳ].[dbo].[ˮ��춨��¼����ʷ]
GO

drop index idx_huang_vr_batch on [huang_table_verify_result];
create index idx_huang_vr_batch on [huang_table_verify_result]([ί�е���], [ID]);

drop index idx_huang_vr_meter on [huang_table_verify_result];
create index idx_huang_vr_meter on [huang_table_verify_result]([ID], [�������]);

GO

select COUNT(1)
from [huang_table_verify_result];

