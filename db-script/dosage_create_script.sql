CREATE TABLE DOSAGE_PROFILE
(
PATIENT_ID VARCHAR NOT NULL,
PRODUCT_ID VARCHAR NOT NULL,
REMINDER_TIME VARCHAR,
DOSAGE jsonb NOT NULL,
PRIMARY KEY (PATIENT_ID, PRODUCT_ID)
);