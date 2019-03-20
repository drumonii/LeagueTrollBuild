import { ChampionPage } from './champion.po';

describe('champion page', () => {
  const page = new ChampionPage();

  beforeEach(() => {
    page.navigateTo();

    expect(page.getTitleContent()).toBe(page.getChampionName());
  });

  it('should show the champion and the generated troll build', () => {
    const championName = page.getChampion().getText();
    expect(championName).toBe(page.getChampionName());

    const defaultSelectedMap = page.getDefaultSelectedMap().getText();
    expect(defaultSelectedMap).toContain(`Summoner's Rift`);

    const trollBuild = page.getTrollBuild();
    expect(trollBuild.items().count()).toBe(6);
    expect(trollBuild.summonerSpells().count()).toBe(2);
    expect(trollBuild.trinket().count()).toBe(1);
  });

  it('should save the troll build', () => {
    page.saveTrollBuild();

    page.getSavedBuild().then((savedBuildLink) => {
      page.navigateToBuild(savedBuildLink);
      expect(page.getTitle()).toContain('Build');
    });
  });

});
