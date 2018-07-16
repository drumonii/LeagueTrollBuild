CREATE TABLE CHAMPION_SPELL_COST (
  CHAMPION_SPELL_KEY VARCHAR(255),
  COST               INT,
  -- No PK here because a spell cost can have the same key and cost
  FOREIGN KEY (CHAMPION_SPELL_KEY) REFERENCES CHAMPION_SPELL (KEY)
);