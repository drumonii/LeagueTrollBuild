CREATE TABLE ITEM_FROM (
  ITEM_ID   INT,
  ITEM_FROM INT,
  -- No PK here because an item can be created from the same item. See Warden's Mail for example.
  FOREIGN KEY (ITEM_ID) REFERENCES ITEM (ID)
);