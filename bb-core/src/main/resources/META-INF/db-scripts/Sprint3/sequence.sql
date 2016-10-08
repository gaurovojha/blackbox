-- Drops for IDS Sequences
DROP SEQUENCE "BBX_IDS_BUILD_ID_SEQ";
DROP SEQUENCE "BBX_IDS_INTERNAL_FINAL_ID_SEQ";
DROP SEQUENCE "BBX_IDS_EXTERNAL_FINAL_ID_SEQ";


-- Sequence for IDS and Filing Info instances.
CREATE SEQUENCE "BBX_IDS_BUILD_ID_SEQ" 			AS BIGINT START WITH 1 INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 NO CYCLE CACHE 10;
CREATE SEQUENCE "BBX_IDS_INTERNAL_FINAL_ID_SEQ" AS BIGINT START WITH 1 INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 NO CYCLE CACHE 10;
CREATE SEQUENCE "BBX_IDS_EXTERNAL_FINAL_ID_SEQ" AS BIGINT START WITH 1 INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 NO CYCLE CACHE 10;
