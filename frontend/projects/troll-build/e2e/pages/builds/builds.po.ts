import { BaseTrollBuildPage } from '../base-troll-build.po';

export class BuildsPage extends BaseTrollBuildPage {

  navigateTo(): void {
    cy.visit('/builds/100000000');
  }

}
