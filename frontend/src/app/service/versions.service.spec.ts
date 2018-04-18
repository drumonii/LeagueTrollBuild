import { TestBed, inject } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { VersionsService } from './versions.service';

import { Version } from '@model/version';

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

  it('should get the latest saved Version', inject([VersionsService, HttpTestingController],
    (service: VersionsService, httpMock: HttpTestingController) => {
    const mockLatestVersion = new Version('8.7.1');

    service.getLatestVersion().subscribe(latestVersion => {
      expect(latestVersion).toEqual(mockLatestVersion);
    });

    const testReq = httpMock.expectOne('/api/versions/latest');
    expect(testReq.request.method).toEqual('GET');

    testReq.flush(mockLatestVersion);
  }));

  it('should get the latest saved Version with REST error', inject([VersionsService, HttpTestingController],
    (service: VersionsService, httpMock: HttpTestingController) => {
    service.getLatestVersion().subscribe(latestVersion => {
      expect(latestVersion).toBeNull();
    });

    const testReq = httpMock.expectOne('/api/versions/latest');
    expect(testReq.request.method).toEqual('GET');

    testReq.error(new ErrorEvent('An unexpected error occurred'));
  }));
});
