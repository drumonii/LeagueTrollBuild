import { AdminBatchPage } from './admin-batch.po';
import { browser, by, element, ExpectedConditions } from 'protractor';

describe('admin batch page', () => {
  const page = new AdminBatchPage();

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

      expect(await page.getTitleContent()).toBe('Batch Jobs');
    });

    afterEach(async () => {
      await page.logoutAdmin();
    });

    it('should show the batch jobs datatable', async () => {
      expect(await page.getBatchJobsDatatable().isPresent()).toBeTruthy();

      await element(by.css('#clr-dg-row1 .datagrid-expandable-caret button.datagrid-expandable-caret-button')).click();

      const stepExecutionsTable = await page.getStepExecutionsDetail();
      await browser.wait(ExpectedConditions.presenceOf(stepExecutionsTable));
    });

  });

});
