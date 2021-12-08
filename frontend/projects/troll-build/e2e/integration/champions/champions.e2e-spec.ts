import { ChampionsPage } from '../../pages/champions/champions.po';

describe('champions page', () => {
  const page = new ChampionsPage();

  beforeEach(() => {
    page.navigateTo();

    page.getTitle().should('equal', 'League Troll Build');
  });

  describe('on load', () => {

    it('should show the champions', () => {
      page.getChampions().should('have.length.greaterThan', 1);
    });

    it('should show the name filter', () => {
      page.getChampionNameFilter().should('exist')
    });

    it('should show the tag filters', () => {
      page.getChampionTagFilters().should('exist')
    });

  });

  describe('on champion click', () => {

    beforeEach(function() {
      page.getFirstChampion().as('firstChampion');
    });

    it('should navigate to the champion', () => {
      page.championName(cy.get('@firstChampion')).then((firstChampion) => {
        const firstChampionName = firstChampion.text();

        cy.get('@firstChampion').click();

        cy.wait(150).then(() => {
          page.getTitleContent().should('equal', firstChampionName);
        });
      })
    });

  });

  describe('filters', () => {

    beforeEach(() => {
      page.getChampionsLength().as('championsCount');
      page.getChampionTagFilters().first().as('firstChampionTagFilter');
    });

    describe('filter by name', () => {

      it('should only show filtered', () => {
        const championToSearch = 'maokai';
        page.getChampionNameFilter().type(championToSearch);

        page.getChampions().should('have.length', 1);

        // reset name filter (clear() nor sendKeys('') does not work)
        let spacesToGoBack = championToSearch.length;
        do {
          page.getChampionNameFilter().type('{backspace}');
          spacesToGoBack--;
        } while (spacesToGoBack > 0);

        cy.get('@championsCount').then((championsCount) => {
          page.getChampions().should('have.length', championsCount);
        });
      });

    });

    describe('filter by tag', () => {

      it('should only show filtered', () => {
        cy.get('@firstChampionTagFilter').click();

        cy.get('@championsCount').then((championsCount) => {
          page.getChampions().should('have.length.lessThan', championsCount);
        });

        // reset tag filter
        cy.get('@firstChampionTagFilter').click();

        cy.get('@championsCount').then((championsCount) => {
          page.getChampions().should('have.length', championsCount);
        });
      });

    });

  });

});
