import { BuildsPage } from '../../pages/builds/builds.po';

xdescribe('builds page', () => {
  const page = new BuildsPage();

  // ** saved build page is tested on champion spec

  describe('random build', () => {

    beforeEach(() => {
      page.navigateTo();
    });

    it('should redirect to a random build', () => {
      page.getTitle().should('have.text', 'Build');
    });

  });

});
