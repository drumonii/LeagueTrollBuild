import { ChampionPage } from './champion.po';

describe('champion page', () => {
  const page = new ChampionPage();

  beforeEach(async () => {
    await page.navigateTo();

    expect(await page.getTitleContent()).toBe(page.getChampionName());
  });

  it('should show the champion and the generated troll build', async () => {
    expect(await page.getChampion().getText()).toBe(page.getChampionName());
    expect(await page.getDefaultSelectedMap().getText()).toContain(`Summoner's Rift`);

    const trollBuild = page.getTrollBuild();
    expect(await trollBuild.items().count()).toBe(6);
    expect(await trollBuild.summonerSpells().count()).toBe(2);
    expect(await trollBuild.trinket().count()).toBe(1);
  });

  it('should save the troll build', async () => {
    await page.saveTrollBuild();

    const savedBuildLink = await page.getSavedBuild();
    await page.navigateToBuild(savedBuildLink);
    expect(await page.getTitle()).toContain('Build');
  });

});
