import { AdminHomePage } from './admin-home.po';

describe('admin home page', () => {
  const page = new AdminHomePage();

  describe('unauthenticated user', () => {

    beforeEach(() => {
      page.navigateTo();
    });

    it('should redirect', () => {
      expect(page.getCurrentUrl()).toBe(page.getRedirectedUrl());
    });

  });

  describe('authenticated admin', () => {

    beforeEach(() => {
      page.loginAdmin();
      page.navigateTo();

      expect(page.getTitle()).toBe('League Troll Build Admin');
    });

    afterEach(() => {
      page.logoutAdmin();
    });

    it('should show home components', () => {
      expectFailedJobsComponent();
      expectCpuUsageComponent();
      expectMemoryUsageComponent();
      expectGlobalErrorsComponent();
    });

    function expectFailedJobsComponent() {
      expect(page.getFailedJobsComponent().isPresent()).toBe(true);
      expect(page.getFailedToLoadFailedJobsAlert().isPresent()).toBe(false);
    }

    function expectCpuUsageComponent() {
      expect(page.getCpuUsageComponent().isPresent()).toBe(true);
      expect(page.getFailedToLoadCpuUsageAlert().isPresent()).toBe(false);
    }

    function expectMemoryUsageComponent() {
      expect(page.getMemoryUsageComponent().isPresent()).toBe(true);
      expect(page.getFailedToLoadMemoryUsageAlert().isPresent()).toBe(false);
    }

    function expectGlobalErrorsComponent() {
      expect(page.getGlobalErrorsComponent().isPresent()).toBe(true);
      expect(page.getFailedToLoadGlobalErrorsAlert().isPresent()).toBe(false);
    }

  });

});
