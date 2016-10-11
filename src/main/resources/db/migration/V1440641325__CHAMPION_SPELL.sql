CREATE TABLE CHAMPION_SPELL (
  KEY         VARCHAR(255) PRIMARY KEY,
  CHAMPION_ID INT          NOT NULL,
  NAME        VARCHAR(255) NOT NULL,
  DESCRIPTION TEXT         NOT NULL,
  TOOLTIP     TEXT         NOT NULL,
  FOREIGN KEY (CHAMPION_ID) REFERENCES CHAMPION (ID)
);