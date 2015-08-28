CREATE TABLE ITEM_FROM (
  ITEM_ID   INT,
  ITEM_FROM VARCHAR(255)
  -- No PK here because an item can be created from the same item. See Warden's Mail for example.
);