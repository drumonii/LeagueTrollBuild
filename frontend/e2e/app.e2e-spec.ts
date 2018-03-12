import { AppPage } from './app.po';

describe('league-troll-build App', () => {
  const page = new AppPage();

  it('should display welcome message', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('Welcome to app!');
  });
});
