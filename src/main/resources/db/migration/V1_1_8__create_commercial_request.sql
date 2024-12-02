CREATE TABLE IF NOT EXISTS COMMERCIAL_CLEANING (
                                     ID BIGSERIAL PRIMARY KEY,
                                     ADDRESS VARCHAR(255) NOT NULL,
                                     DATE_TIME TIMESTAMP NOT NULL,
                                     FLOOR INT NOT NULL,
                                     SQUARE_FT FLOAT NOT NULL,
                                     COMPANY_NAME VARCHAR(255) NOT NULL,
                                     PHONE_NUMBER VARCHAR(50) NOT NULL,
                                     CONTACT_PERSON_NAME VARCHAR(255) NOT NULL,
                                     ADDITIONAL_INSTRUCTIONS VARCHAR(255),
                                     PRICE DECIMAL NOT NULL,
                                     EMAIL VARCHAR(255) NOT NULL,
                                     STATUS VARCHAR(50) NOT NULL,
                                     CREATED_AT TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                     USER_ID BIGINT,
                                     CONSTRAINT FK_COMMERCIAL_CLEANING_USER FOREIGN KEY (USER_ID)
                                         REFERENCES USERS(ID)
);
