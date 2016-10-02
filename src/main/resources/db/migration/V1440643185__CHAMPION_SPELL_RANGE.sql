CREATE TABLE CHAMPION_SPELL_RANGE (
  CHAMPION_SPELL_KEY VARCHAR(255),
  RANGE              INT
  -- No PK here because a spell range can have the same key and range
);