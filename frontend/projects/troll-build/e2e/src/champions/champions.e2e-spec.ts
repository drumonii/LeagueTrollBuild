import { ChampionsPage } from './champions.po';
import { Key } from 'protractor';

describe('champions page', () => {
  const page = new ChampionsPage();

  beforeEach(async () => {
    await page.navigateTo();

    expect(await page.getTitle()).toBe('League Troll Build');
  });

  describe('on load', () => {

    it('should show the champions', async () => {
      expect(await page.getChampions().count()).toBeGreaterThan(1);
    });

    it('should show the name filter', async () => {
      expect(await page.getChampionNameFilter().isPresent()).toBe(true);
    });

    it('should show the tag filters', async () => {
      expect(await page.getChampionTagFilters().isPresent()).toBe(true);
    });

  });

  describe('on champion click', () => {

    it('should navigate to the champion', async () => {
      const firstChampion = await page.getFirstChampion();
      const firstChampionName = await page.championName(firstChampion);
      await firstChampion.click();

      expect(await page.getTitleContent()).toBe(firstChampionName);
    });

  });

  describe('filters', () => {

    let championsCount = 0;

    beforeEach(async () => {
      championsCount = await page.getChampions().count();
    });

    describe('filter by name', () => {

      it('should only show filtered', async () => {
        const championToSearch = 'maokai';
        await page.getChampionNameFilter().sendKeys(championToSearch);

        expect(await page.getChampions().count()).toBe(1);

        // reset name filter (clear() nor sendKeys('') does not work)
        let spacesToGoBack = championToSearch.length;
        do {
          await page.getChampionNameFilter().sendKeys(Key.BACK_SPACE);
          spacesToGoBack--;
        } while (spacesToGoBack > 0);

        expect(await page.getChampions().count()).toBe(championsCount);
      });

    });

    describe('filter by tag', () => {

      it('should only show filtered', async () => {
        const firstChampionTagFilter = await page.getChampionTagFilters().first();
        await firstChampionTagFilter.click();

        expect(await page.getChampions().count()).toBeLessThan(championsCount);

        // reset tag filter
        await firstChampionTagFilter.click();

        expect(await page.getChampions().count()).toBe(championsCount);
      });

    });

  });

});
