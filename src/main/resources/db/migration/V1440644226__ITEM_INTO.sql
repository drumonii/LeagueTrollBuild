CREATE TABLE ITEM_INTO (
  ITEM_ID   INT,
  ITEM_INTO INT,
  PRIMARY KEY (ITEM_ID, ITEM_INTO),
  FOREIGN KEY (ITEM_ID) REFERENCES ITEM (ID)
);