CREATE TABLE CHAMPION_SPELL_IMAGE (
  CHAMPION_SPELL_KEY VARCHAR(255) PRIMARY KEY,
  IMG_FULL           VARCHAR(255) NOT NULL,
  SPRITE             VARCHAR(255) NOT NULL,
  IMG_GROUP          VARCHAR(255) NOT NULL,
  IMG_SRC            BYTEA        NOT NULL,
  X                  INT          NOT NULL,
  Y                  INT          NOT NULL,
  W                  INT          NOT NULL,
  H                  INT          NOT NULL
);