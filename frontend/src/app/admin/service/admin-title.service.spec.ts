import { TestBed, inject } from '@angular/core/testing';
import { Title } from '@angular/platform-browser';

import { AdminTitleService } from './admin-title.service';

describe('AdminTitleService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [AdminTitleService]
    });
  });

  describe('reset title', () => {

    afterEach(inject([AdminTitleService, Title], (service: AdminTitleService, title: Title) => {
      expect(title.getTitle()).toBe(service.baseTitle);
    }));

    it('without existing content title', inject([AdminTitleService, Title], (service: AdminTitleService, title: Title) => {
      title.setTitle(service.baseTitle);

      service.resetTitle();
    }));

    it('with existing content title', inject([AdminTitleService, Title], (service: AdminTitleService, title: Title) => {
      title.setTitle(`${service.baseTitle} | Existing Content`);

      service.resetTitle();
    }));

  });

  describe('set title', () => {

    const contentTitle = 'New Content';

    afterEach(inject([AdminTitleService, Title], (service: AdminTitleService, title: Title) => {
      expect(title.getTitle()).toBe(`${service.baseTitle} | ${contentTitle}`);
    }));

    it('without existing content title', inject([AdminTitleService, Title], (service: AdminTitleService, title: Title) => {
      title.setTitle(service.baseTitle);

      service.setTitle(contentTitle);
    }));

    it('with existing content title', inject([AdminTitleService, Title], (service: AdminTitleService, title: Title) => {
      title.setTitle(`${service.baseTitle} | Existing Content`);

      service.setTitle(contentTitle);
    }));

  });

});
