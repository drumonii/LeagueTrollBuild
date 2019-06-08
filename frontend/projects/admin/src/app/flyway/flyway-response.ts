export interface FlywayResponse {
  contexts: FlywayContexts;
}

interface FlywayContexts {
  application: FlywayApplication;
}

interface FlywayApplication {
  flywayBeans: FlywayBeans;
}

interface FlywayBeans {
  flyway: Flyway;
}

interface Flyway {
  migrations: FlywayMigration[];
}

export interface FlywayMigration {
  type: string;
  checksum: number;
  version: string;
  description: string;
  script: string;
  state: string;
  installedBy: string;
  installedOn: string;
  installedRank: number;
  executionTime: number;
}
