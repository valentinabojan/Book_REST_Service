CREATE TABLE BOOK (
  BOOK_ID NUMBER(4) PRIMARY KEY,
  TITLE VARCHAR2(255) NOT NULL,
  RELEASE_DATE DATE,
  PRICE NUMBER(7,2),
  ISBN VARCHAR2(32) UNIQUE NOT NULL,
  DESCRIPTION VARCHAR2(1000),
  COVER_PATH VARCHAR2(255),
  PAGE_NUMBER NUMBER(5),
  LANGUAGE VARCHAR2(255),
  STARS NUMBER(2,1)
);

CREATE TABLE REVIEW (
  REVIEW_ID NUMBER(4) PRIMARY KEY,
  USERNAME VARCHAR2(255) NOT NULL,
  TITLE VARCHAR2(255) NOT NULL,
  CONTENT VARCHAR2(1000),
  REVIEW_DATE DATE DEFAULT SYSDATE,
  BOOK_ID NUMBER(4) REFERENCES BOOK(BOOK_ID)
);

CREATE TABLE BOOK_AUTHOR (
  BOOK_ID NUMBER(4) NOT NULL REFERENCES BOOK(BOOK_ID),
  AUTHOR_NAME VARCHAR2(255) NOT NULL,
  CONSTRAINT PK_BOOK_AUTHOR PRIMARY KEY (BOOK_ID, AUTHOR_NAME)
);

CREATE TABLE BOOK_CATEGORY (
  BOOK_ID NUMBER(4) NOT NULL REFERENCES BOOK(BOOK_ID),
  CATEGORY VARCHAR2(255) NOT NULL,
  CONSTRAINT PK_BOOK_CATEGORY PRIMARY KEY (BOOK_ID, CATEGORY)
);