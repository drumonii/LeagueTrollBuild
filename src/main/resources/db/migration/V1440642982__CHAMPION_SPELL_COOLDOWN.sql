CREATE TABLE CHAMPION_SPELL_COOLDOWN (
  CHAMPION_SPELL_KEY VARCHAR(255),
  COOLDOWN           INT
  -- No PK here because a spell cooldown can have the same key and cooldown
);