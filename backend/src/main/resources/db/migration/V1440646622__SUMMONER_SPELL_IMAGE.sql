CREATE TABLE SUMMONER_SPELL_IMAGE (
  SUMMONER_SPELL_ID INT PRIMARY KEY,
  IMG_FULL          VARCHAR(255) NOT NULL,
  SPRITE            VARCHAR(255) NOT NULL,
  IMG_GROUP         VARCHAR(255) NOT NULL,
  IMG_SRC           BYTEA        NOT NULL,
  X                 INT          NOT NULL,
  Y                 INT          NOT NULL,
  W                 INT          NOT NULL,
  H                 INT          NOT NULL,
  FOREIGN KEY (SUMMONER_SPELL_ID) REFERENCES SUMMONER_SPELL (ID)
);