CREATE TABLE BUILD (
  ID                  INT PRIMARY KEY,
  CHAMPION_ID         INT       NOT NULL,
  ITEM_1_ID           INT       NOT NULL,
  ITEM_2_ID           INT       NOT NULL,
  ITEM_3_ID           INT       NOT NULL,
  ITEM_4_ID           INT       NOT NULL,
  ITEM_5_ID           INT       NOT NULL,
  ITEM_6_ID           INT       NOT NULL,
  SUMMONER_SPELL_1_ID INT       NOT NULL,
  SUMMONER_SPELL_2_ID INT       NOT NULL,
  TRINKET_ID          INT       NOT NULL,
  MAP_ID              INT       NOT NULL,
  CREATED_DATE        TIMESTAMP NOT NULL
);

CREATE SEQUENCE BUILD_SEQ INCREMENT BY 1 START WITH 1;