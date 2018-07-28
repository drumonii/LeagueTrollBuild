CREATE TABLE ITEM_MAP (
  ITEM_ID  INT,
  MAPS_KEY INT,
  MAP      BOOLEAN,
  PRIMARY KEY (ITEM_ID, MAPS_KEY, MAP),
  FOREIGN KEY (ITEM_ID) REFERENCES ITEM (ID)
);