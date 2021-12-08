import { ChampionPage } from '../../pages/champion/champion.po';

describe('champion page', () => {
  const page = new ChampionPage();

  beforeEach(() => {
    page.navigateTo();

    // wait for title to update
    cy.wait(150).then(() => {
      page.getTitleContent().should('equal', page.getChampionName());
    });
  });

  it('should show the champion and the generated troll build', () => {
    page.getChampion().should('have.text', page.getChampionName());
    page.getDefaultSelectedMap().should('contain.text', `Summoner's Rift`);

    const trollBuild = page.getTrollBuild();
    trollBuild.items().should('have.length', 6)
    trollBuild.summonerSpells().should('have.length', 2);
    trollBuild.trinket().should('have.length', 1);
  });

  xit('should save the troll build', () => {
    page.saveTrollBuild();

    page.getSavedBuildInputLink().then((savedBuildLink) => {
      page.getSaveBuildBtn().should('not.exist')
      page.navigateToBuild(savedBuildLink.text());
      page.getTitle().should('contain.text', 'Build');
    })
  });

});
