import { TestBed, inject } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController, RequestMatch } from '@angular/common/http/testing';

import { VersionsService } from './versions.service';

import { Version } from '@ltb-model/version';

describe('VersionsService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [VersionsService]
    });
  });

  afterEach(inject([HttpTestingController], (httpMock: HttpTestingController) => {
    httpMock.verify();
  }));

  describe('getLatestVersion', () => {

    const requestMatch: RequestMatch = { method: 'GET', url: '/versions/latest' };

    it('should get the latest saved Version', inject([VersionsService, HttpTestingController],
      (service: VersionsService, httpMock: HttpTestingController) => {
      const mockLatestVersion: Version = {
        patch: '8.7.1',
        major: 8,
        minor: 7,
        revision: 1
      };

      service.getLatestVersion().subscribe(latestVersion => {
        expect(latestVersion).toEqual(mockLatestVersion);
      });

      const testReq = httpMock.expectOne(requestMatch);

      testReq.flush(mockLatestVersion);
    }));

    it('should get the latest saved Version with REST error', inject([VersionsService, HttpTestingController],
      (service: VersionsService, httpMock: HttpTestingController) => {
      service.getLatestVersion().subscribe(latestVersion => {
        expect(latestVersion).toBeNull();
      });

      const testReq = httpMock.expectOne(requestMatch);

      testReq.error(new ErrorEvent('An unexpected error occurred'));
    }));

  });

});
