import { TestBed, inject } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { BuildsService } from './builds.service';

import { Build } from '@model/build';

describe('BuildsService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [BuildsService]
    });
  });

  afterEach(inject([HttpTestingController], (httpMock: HttpTestingController) => {
    httpMock.verify();
  }));

  it('should POST a build', inject([BuildsService, HttpTestingController],
    (service: BuildsService, httpMock: HttpTestingController) => {
    const mockBuild: Build = {
      championId: 1,
      item1Id: 1,
      item2Id: 2,
      item3Id: 3,
      item4Id: 4,
      item5Id: 5,
      item6Id: 6,
      summonerSpell1Id: 1,
      summonerSpell2Id: 2,
      trinketId: 1,
      mapId: 1
    };

    service.saveBuild(mockBuild).subscribe(res => {
      expect(res).toBeTruthy();
    });

    const testReq = httpMock.expectOne('/api/builds');
    expect(testReq.request.method).toBe('POST');
    expect(testReq.request.detectContentTypeHeader()).toBe('application/json');
    expect(testReq.request.body).toEqual(mockBuild);

    testReq.flush(new HttpResponse<Build>({
      body: mockBuild,
      headers: new HttpHeaders({
        'Location': 'http://localhost:8080/api/builds/1'
      }),
      status: 201
    }));
  }));

  it('should POST a build with REST error', inject([BuildsService, HttpTestingController],
    (service: BuildsService, httpMock: HttpTestingController) => {
    service.saveBuild(new Build()).subscribe(res => {
      expect(res).toBeNull();
    });

    const testReq = httpMock.expectOne('/api/builds');
    expect(testReq.request.method).toBe('POST');
    expect(testReq.request.detectContentTypeHeader()).toBe('application/json');

    testReq.error(new ErrorEvent('An unexpected error occurred'));
  }));

  it('should GET a build', inject([BuildsService, HttpTestingController],
    (service: BuildsService, httpMock: HttpTestingController) => {
    const mockBuild: Build = {
      id: 1,
      championId: 1,
      item1Id: 1,
      item2Id: 2,
      item3Id: 3,
      item4Id: 4,
      item5Id: 5,
      item6Id: 6,
      summonerSpell1Id: 1,
      summonerSpell2Id: 2,
      trinketId: 1,
      mapId: 1
    };

    service.getBuild(mockBuild.id).subscribe(build => {
      expect(build).toEqual(mockBuild);
    });

    const testReq = httpMock.expectOne(`/api/builds/${mockBuild.id}`);
    expect(testReq.request.method).toEqual('GET');

    testReq.flush(mockBuild);
  }));

  it('should GET a build with REST error of 404 status', inject([BuildsService, HttpTestingController],
    (service: BuildsService, httpMock: HttpTestingController) => {
    const buildId = 1;

    service.getBuild(buildId).subscribe(build => {
      expect(build).toBeNull();
    });

    const testReq = httpMock.expectOne(`/api/builds/${buildId}`);
    expect(testReq.request.method).toEqual('GET');

    testReq.error(new ErrorEvent(`Unable to find a Build with Id: ${buildId}`), { status: 404 });
  }));

  it('should GET a build with REST error of 400 status', inject([BuildsService, HttpTestingController],
    (service: BuildsService, httpMock: HttpTestingController) => {
    const buildId = 1;

    service.getBuild(buildId).subscribe(build => {
      expect(build).toEqual(new Build());
    });

    const testReq = httpMock.expectOne(`/api/builds/${buildId}`);
    expect(testReq.request.method).toEqual('GET');

    testReq.error(new ErrorEvent('champion: null'), { status: 400 });
  }));
});
