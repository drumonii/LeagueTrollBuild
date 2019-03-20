import { ChampionsPage } from './champions.po';

describe('champions page', () => {
  const page = new ChampionsPage();

  beforeEach(() => {
    page.navigateTo();

    expect(page.getTitle()).toBe('League Troll Build');
  });

  describe('on load', () => {

    it('should show the champions', () => {
      expect(page.getChampions().count()).toBeGreaterThan(1);
    });

    it('should show the name filter', () => {
      expect(page.getChampionNameFilter().isPresent()).toBe(true);
    });

    it('should show the tag filters', () => {
      expect(page.getChampionTagFilters().isPresent()).toBe(true);
    });

  });

  describe('on champion click', () => {

    it('should navigate to the champion', () => {
      const firstChampion = page.getFirstChampion();
      const firstChampionName = page.championName(firstChampion);
      firstChampion.click();

      expect(page.getTitleContent()).toBe(firstChampionName);
    });

  });

  describe('filter by name', () => {

    it('should only show filtered', () => {
      page.getChampionNameFilter().sendKeys('maokai');

      expect(page.getChampions().count()).toBe(1);
    });

  });

  describe('filter by tag', () => {

    let championsCount = 0;

    beforeEach(() => {
      page.getChampions().count().then((count) => championsCount = count);
    });

    it('should only show filtered', () => {
      const championTagFilters = page.getChampionTagFilters();
      const firstChampionTagFilter = championTagFilters.first();
      firstChampionTagFilter.click();

      expect(page.getChampions().count()).toBeLessThan(championsCount);

      // reset tag filter
      firstChampionTagFilter.click();

      expect(page.getChampions().count()).toBe(championsCount);
    });

  });

});
