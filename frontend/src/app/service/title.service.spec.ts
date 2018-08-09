import { TestBed, inject } from '@angular/core/testing';
import { Title } from '@angular/platform-browser';

import { TitleService } from './title.service';

describe('TitleService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [TitleService]
    });
  });

  describe('reset title', () => {

    afterEach(inject([TitleService, Title], (service: TitleService, title: Title) => {
      expect(title.getTitle()).toBe(service.baseTitle);
    }));

    it('without existing content title', inject([TitleService, Title], (service: TitleService, title: Title) => {
      title.setTitle(service.baseTitle);

      service.resetTitle();
    }));

    it('with existing content title', inject([TitleService, Title], (service: TitleService, title: Title) => {
      title.setTitle(`${service.baseTitle} | Existing Content`);

      service.resetTitle();
    }));

  });

  describe('set title', () => {

    const contentTitle = 'New Content';

    afterEach(inject([TitleService, Title], (service: TitleService, title: Title) => {
      expect(title.getTitle()).toBe(`${service.baseTitle} | ${contentTitle}`);
    }));

    it('without existing content title', inject([TitleService, Title], (service: TitleService, title: Title) => {
      title.setTitle(service.baseTitle);

      service.setTitle(contentTitle);
    }));

    it('with existing content title', inject([TitleService, Title], (service: TitleService, title: Title) => {
      title.setTitle(`${service.baseTitle} | Existing Content`);

      service.setTitle(contentTitle);
    }));

  });

});
