import { ChampionModule } from './champion.module';

describe('ChampionModule', () => {
  let championModule: ChampionModule;

  beforeEach(() => {
    championModule = new ChampionModule();
  });

  it('should create an instance', () => {
    expect(championModule).toBeTruthy();
  });
});
