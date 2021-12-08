import { AdminAppPage } from '../pages/admin-app.po';

describe('admin-app', () => {
  const page = new AdminAppPage();

  beforeEach(() => {
    page.navigateTo();
  });

  it('show show the admin header', () => {
    page.getHeaderText().should('equal', 'League Troll Build Admin');
  });

  it('show show the admin footer', () => {
    page.getFooter().should('exist');
  });

});
