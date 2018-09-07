import { AppPage } from './app.po';

describe('league-troll-build App', () => {
  const page = new AppPage();

  it('should display header title', () => {
    page.navigateTo();
    expect(page.getHeaderText()).toEqual('League Troll Build');
  });
});
