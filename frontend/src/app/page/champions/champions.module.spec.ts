import { ChampionsModule } from './champions.module';

describe('ChampionsModule', () => {
  let championsModule: ChampionsModule;

  beforeEach(() => {
    championsModule = new ChampionsModule();
  });

  it('should create an instance', () => {
    expect(championsModule).toBeTruthy();
  });
});
