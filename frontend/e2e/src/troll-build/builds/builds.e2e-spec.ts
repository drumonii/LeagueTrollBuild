import { BuildsPage } from './builds.po';

describe('builds page', () => {
  const page = new BuildsPage();

  // ** saved build page is tested on champion spec

  describe('random build', () => {

    beforeEach(() => {
      page.navigateTo();
    });

    it('should redirect to a random build', () => {
      expect(page.getTitle()).toContain('Build');
    });

  });

});
