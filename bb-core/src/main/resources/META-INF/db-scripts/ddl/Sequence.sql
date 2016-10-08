use [blackbox]
CREATE SEQUENCE CORREPPONDENCE_BASE_SPLIT_SEQ
  AS BIGINT
  START WITH 1
  INCREMENT BY 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  NO CYCLE
  CACHE 10;
  
  CREATE SEQUENCE CORREPPONDENCE_STAGING_SPLIT_SEQ
  AS BIGINT
  START WITH 1
  INCREMENT BY 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  NO CYCLE
  CACHE 10;
  
-- Sequence for generating family Id.
CREATE SEQUENCE "BBX_FAMILY_ID_SEQ" AS BIGINT START WITH 1 INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 NO CYCLE CACHE 10;