--WEBCRAWLER APPLICATIONLISTVALIDATIONTEST DATA


SET IDENTITY_INSERT [dbo].[BB_WEB_CRAWLER_JOB_STATUS] ON 

INSERT INTO [dbo].[BB_WEB_CRAWLER_JOB_STATUS] ([ID],[JOB_NAME],[MAX_HOST_NOT_FOUND_RETRIES],[MAX_USPTO_UPDATING_RETRIES],[MAX_XML_CORRUPT_COUNT],[MAX_XML_UI_MISMATCH_COUNT],[HOST_NOT_FOUND_RETRIES],[USPTO_UPDATING_RETRIES],[XML_CORRUPT_COUNT],[XML_UI_MISMATCH_COUNT],[MAX_RECORD_RETRY_COUNT])VALUES(6,N'ApplicationListValidationJob',1,1,2,0,0,0,0,0,0)
       
SET IDENTITY_INSERT [dbo].[BB_WEB_CRAWLER_JOB_STATUS] OFF
   
-- BB_MSTR_CUSTOMER DATA INSERT - taken care in insert-data.sql

-- BB_APPLICATION_BASE DATA INSERT

SET IDENTITY_INSERT [dbo].[BB_APPLICATION_BASE] ON 

--INSERT INTO [dbo].[BB_APPLICATION_BASE] ([BB_APPLICATION_ID] ,[CREATED_BY] ,[CREATED_ON] ,[MODIFIED_BY] ,[MODIFIED_ON] ,[APPLICATION_NUMBER_RAW] ,[CHILD_APPLICATION_TYPE] ,[CONFIRMATION_NUMBER] ,[FILING_DATE] ,[APPLICATION_NUMBER] ,[DESCRIPTION] ,[FAMILY_ID] ,[ENTITY] ,[EXPORT_CONTROL] ,[IDS_RELEVANT_STATUS] ,[PROSECUTION_STATUS] ,[ART_UNIT] ,[EXAMINER] ,[FIRST_NAME_INVENTOR] ,[ISSUED_ON] ,[PATENT_NUMBER] ,[PATENT_NUMBER_RAW] ,[TITLE] ,[PUBLICATION_NUMBER] ,[PUBLICATION_NUMBER_RAW] ,[PUBLISHED_ON] ,[SOURCE] ,[SUB_SOURCE] ,[BB_NEW_RECORD_STATUS] ,[RECORD_STATUS] ,[SCENARIO] ,[BB_COMMENT] ,[VERSION] ,[BB_ASSIGNEE] ,[BB_ATTORNEY_DOCKET] ,[BB_CUSTOMER] ,[BB_JURISDICTION] ,[BB_ORGANIZATION] ,[BB_TECHNOLOGY_GROUP])      VALUES(46,1,N'2015-09-15 18:22:58.060',1,N'2015-10-01 18:22:58.060',N'11110000',N'FIRST_FILING',null,N'2015-09-15 18:22:58.060',N'11110000',N'ApplicationListValidatinStatusUpdate Unit Test' ,null ,null ,0 ,null ,null ,null ,null ,null ,null ,null ,null ,N'status update' ,null ,null ,null ,N'MANUAL' ,N'MANUAL' ,N'BLANK' ,N'ALLOWED_TO_ABANDON' ,N'US_FIRST_FILING' ,N'Notification approval' ,0 ,1 ,1 ,5 ,1 ,-1 ,-1)

INSERT INTO [dbo].[BB_APPLICATION_BASE] ([BB_APPLICATION_ID] ,[CREATED_BY] ,[CREATED_ON] ,[MODIFIED_BY] ,[MODIFIED_ON] ,[APPLICATION_NUMBER_RAW] ,[CHILD_APPLICATION_TYPE] ,[CONFIRMATION_NUMBER] ,[FILING_DATE] ,[APPLICATION_NUMBER] ,[DESCRIPTION] ,[FAMILY_ID] ,[ENTITY] ,[EXPORT_CONTROL] ,[IDS_RELEVANT_STATUS] ,[PROSECUTION_STATUS] ,[ART_UNIT] ,[EXAMINER] ,[FIRST_NAME_INVENTOR] ,[ISSUED_ON] ,[PATENT_NUMBER_RAW] ,[TITLE] ,[PUBLICATION_NUMBER] ,[PUBLICATION_NUMBER_RAW] ,[PUBLISHED_ON] ,[SOURCE] ,[SUB_SOURCE] ,[BB_NEW_RECORD_STATUS] ,[RECORD_STATUS] ,[SCENARIO] ,[BB_COMMENT] ,[VERSION] ,[BB_ASSIGNEE] ,[BB_ATTORNEY_DOCKET] ,[BB_CUSTOMER] ,[BB_JURISDICTION] ,[BB_ORGANIZATION] ,[BB_TECHNOLOGY_GROUP]) VALUES(46,1,N'2015-09-15 18:22:58.060',1,N'2015-10-01 18:22:58.060',N'11110000',N'FIRST_FILING',null,N'2015-09-15 18:22:58.060',N'11110000',N'ApplicationListValidatinStatusUpdate Unit Test' ,null ,null ,0 ,null ,null ,null ,null ,null ,null ,null ,N'status update' ,null ,null ,null ,N'MANUAL' ,N'MANUAL' ,N'BLANK' ,N'ALLOWED_TO_ABANDON' ,N'US_FIRST_FILING' ,N'Notification approval' ,0 ,1 ,1 ,5 ,1 ,-1 ,-1)

INSERT INTO [dbo].[BB_APPLICATION_BASE]([BB_APPLICATION_ID],[CREATED_BY],[CREATED_ON],[MODIFIED_BY],[MODIFIED_ON],[APPLICATION_NUMBER_RAW],[CHILD_APPLICATION_TYPE],[CONFIRMATION_NUMBER],[FILING_DATE],[APPLICATION_NUMBER],[DESCRIPTION],[FAMILY_ID],[ENTITY],[EXPORT_CONTROL],[IDS_RELEVANT_STATUS],[PROSECUTION_STATUS],[ART_UNIT],[EXAMINER],[FIRST_NAME_INVENTOR],[ISSUED_ON],[PATENT_NUMBER],[PATENT_NUMBER_RAW],[TITLE],[PUBLICATION_NUMBER],[PUBLICATION_NUMBER_RAW],[PUBLISHED_ON],[SOURCE],[SUB_SOURCE],[BB_NEW_RECORD_STATUS],[RECORD_STATUS],[SCENARIO],[BB_COMMENT],[VERSION],[BB_ASSIGNEE],[BB_ATTORNEY_DOCKET],[BB_CUSTOMER],[BB_JURISDICTION],[BB_ORGANIZATION],[BB_TECHNOLOGY_GROUP])VALUES(47,1,N'2015-09-15 18:22:58.060',1,N'2015-10-01 18:22:58.060',N'11223344',N'FIRST_FILING',null,N'2015-09-15 18:22:58.060',N'11223344',N'ApplicationListValidatinStatusUpdate Unit Test',null,null,0,null,null,null,null,null,null,null,null,N'status update',null,null,null,N'MANUAL',N'MANUAL',N'BLANK',N'TRANSFERRED',N'US_FIRST_FILING',N'Notification approval',0,1,1,5,1,-1,-1)
           
SET IDENTITY_INSERT [dbo].[BB_APPLICATION_BASE] OFF 

COMMIT