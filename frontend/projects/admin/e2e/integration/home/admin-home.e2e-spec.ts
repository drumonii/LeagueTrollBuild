import { AdminHomePage } from '../../pages/home/admin-home.po';

describe('admin home page', () => {
  const page = new AdminHomePage();

  describe('unauthenticated user', () => {

    beforeEach(() => {
      page.navigateTo();
    });

    it('should redirect', () => {
      // wait for page to redirect
      cy.wait(150).then(() => {
        page.getCurrentUrl().should('equal', page.getRedirectedUrl());
      });
    });

  });

  describe('authenticated admin', () => {

    beforeEach(() => {
      page.loginAdmin();
      page.navigateTo();

      // wait for page to redirect
      cy.wait(150).then(() => {
        page.getTitle().should('equal', 'League Troll Build Admin');
      });
    });

    it('should show home components', () => {
      expectAppHealthComponent();
      expectEnvironmentComponent();
      expectSystemResourcesComponent();
      expectHttpStatsComponent();
    });

    function expectAppHealthComponent() {
      page.getAppHealthComponent().as('App health card to be present').should('exist');
      page.getAppHealthErrorAlert().as('Failed to load app health error alert to not be present').should('not.exist')
    }

    function expectEnvironmentComponent() {
      page.getEnvComponent().as('Env card to be present').should('exist');
      page.getEnvErrorAlert().as('Failed to load env error alert to not be present').should('not.exist')
    }

    function expectSystemResourcesComponent() {
      page.getResourcesComponent().as('Resources card to be present').should('exist');
      page.getResourcesErrorAlert().as('Failed to load resources error alert to not be present').should('not.exist')
    }

    function expectHttpStatsComponent() {
      page.getHttpStatsComponent().as('Http stats card to be present').should('exist');
      page.getHttpStatsErrorAlert().as('Failed to load http stats error alert to not be present').should('not.exist')
    }

  });

});
