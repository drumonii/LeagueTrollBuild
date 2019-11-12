import { AdminHomePage } from './admin-home.po';

describe('admin home page', () => {
  const page = new AdminHomePage();

  describe('unauthenticated user', () => {

    beforeEach(async () => {
      await page.navigateTo();
    });

    it('should redirect', async () => {
      expect(await page.getCurrentUrl()).toBe(page.getRedirectedUrl());
    });

  });

  describe('authenticated admin', () => {

    beforeEach(async () => {
      await page.loginAdmin();
      await page.navigateTo();

      expect(await page.getTitle()).toBe('League Troll Build Admin');
    });

    afterEach(async () => {
      await page.logoutAdmin();
    });

    it('should show home components', async (done) => {
      Promise.all([expectAppHealthComponent, expectEnvironmentComponent, expectSystemResourcesComponent, expectHttpStatsComponent])
        .then(() => done());
    });

    async function expectAppHealthComponent() {
      expect(await page.getAppHealthComponent().isPresent())
        .toBe(true, 'App health card to be present');
      expect(await page.getAppHealthErrorAlert().isPresent())
        .toBe(false, 'Failed to load app health error alert to not be present');
    }

    async function expectEnvironmentComponent() {
      expect(await page.getEnvComponent().isPresent())
        .toBe(true, 'Env card to be present');
      expect(await page.getEnvErrorAlert().isPresent())
        .toBe(false, 'Failed to load env error alert to not be present');
    }

    async function expectSystemResourcesComponent() {
      expect(await page.getResourcesComponent().isPresent())
        .toBe(true, 'Resources card to be present');
      expect(await page.getResourcesErrorAlert().isPresent())
        .toBe(false, 'Failed to load resources error alert to not be present');
    }

    async function expectHttpStatsComponent() {
      expect(await page.getHttpStatsComponent().isPresent())
        .toBe(true, 'Http stats card to be present');
      expect(await page.getHttpStatsErrorAlert().isPresent())
        .toBe(false, 'Failed to load http stats error alert to not be present');
    }

  });

});
