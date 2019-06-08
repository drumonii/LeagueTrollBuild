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
      Promise.all([expectFailedJobsComponent, expectCpuUsageComponent, expectMemoryUsageComponent, expectGlobalErrorsComponent])
        .then(() => done());
    });

    async function expectFailedJobsComponent() {
      expect(await page.getFailedJobsComponent().isPresent()).toBe(true, 'FailedJobsComponent to be present');
      expect(await page.getFailedToLoadFailedJobsAlert().isPresent()).toBe(false, 'FailedToLoadFailedJobsAlert to not be present');
    }

    async function expectCpuUsageComponent() {
      expect(await page.getCpuUsageComponent().isPresent()).toBe(true, 'CpuUsageComponent to be present');
      expect(await page.getFailedToLoadCpuUsageAlert().isPresent()).toBe(false, 'FailedToLoadCpuUsageAlert to not be present');
    }

    async function expectMemoryUsageComponent() {
      expect(await page.getMemoryUsageComponent().isPresent()).toBe(true, 'MemoryUsageComponent to be present');
      expect(await page.getFailedToLoadMemoryUsageAlert().isPresent()).toBe(false, 'FailedToLoadMemoryUsageAlert to not be present');
    }

    async function expectGlobalErrorsComponent() {
      expect(await page.getGlobalErrorsComponent().isPresent()).toBe(true, 'GlobalErrorsComponent to be present');
      expect(await page.getFailedToLoadGlobalErrorsAlert().isPresent()).toBe(false, 'FailedToLoadGlobalErrorsAlert to not be present');
    }

  });

});
