CREATE TABLE CHAMPION_INFO (
  CHAMPION_ID INT PRIMARY KEY,
  ATTACK      INT NOT NULL,
  DEFENSE     INT NOT NULL,
  MAGIC       INT NOT NULL,
  DIFFICULTY  INT NOT NULL,
  FOREIGN KEY (CHAMPION_ID) REFERENCES CHAMPION (ID)
);