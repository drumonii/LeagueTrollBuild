import { AdminFlywayPage } from './admin-flyway.po';

describe('admin flyway page', () => {
  const page = new AdminFlywayPage();

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

      expect(page.getTitleContent()).toBe('Flyway Migrations');
    });

    afterEach(() => {
      page.logoutAdmin();
    });

    it('should show the Flyway datatable', () => {
      expect(page.getFlywayDatatable().isPresent()).toBe(true);
    });

  });

});
