import { TestBed, inject } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController, RequestMatch } from '@angular/common/http/testing';

import { SavedBuildsService } from './saved-builds.service';

import { Build } from '@ltb-model/build';

describe('BuildsService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [SavedBuildsService]
    });
  });

  afterEach(inject([HttpTestingController], (httpMock: HttpTestingController) => {
    httpMock.verify();
  }));

  describe('getBuild', () => {

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

    const requestMatch: RequestMatch = { method: 'GET', url: `/builds/${mockBuild.id}` };

    it('should GET a build', inject([SavedBuildsService, HttpTestingController],
      (service: SavedBuildsService, httpMock: HttpTestingController) => {

      service.getBuild(mockBuild.id).subscribe(build => {
        expect(build).toEqual(mockBuild);
      });

      const testReq = httpMock.expectOne(requestMatch);

      testReq.flush(mockBuild);
    }));

    it('should GET a build with REST error of 404 status', inject([SavedBuildsService, HttpTestingController],
      (service: SavedBuildsService, httpMock: HttpTestingController) => {
      service.getBuild(mockBuild.id).subscribe(build => {
        expect(build).toBeNull();
      });

      const testReq = httpMock.expectOne(requestMatch);

      testReq.error(new ErrorEvent('An unexpected error occurred'), { status: 404 });
    }));

    it('should GET a build with REST error of 400 status', inject([SavedBuildsService, HttpTestingController],
      (service: SavedBuildsService, httpMock: HttpTestingController) => {
      service.getBuild(mockBuild.id).subscribe(build => {
        expect(build).toEqual(new Build());
      });

      const testReq = httpMock.expectOne(requestMatch);

      testReq.error(new ErrorEvent('An unexpected error occurred'), { status: 400 });
    }));

  });

  describe('countBuilds', () => {

    const requestMatch: RequestMatch = { method: 'GET', url: '/builds/count' };

    it('should GET count of builds', inject([SavedBuildsService, HttpTestingController],
      (service: SavedBuildsService, httpMock: HttpTestingController) => {
      const numberOfBuilds = 1;

      service.countBuilds().subscribe(count => {
        expect(count).toBe(numberOfBuilds);
      });

      const testReq = httpMock.expectOne(requestMatch);

      testReq.flush(numberOfBuilds);
    }));

    it('should GET count of builds with REST error', inject([SavedBuildsService, HttpTestingController],
      (service: SavedBuildsService, httpMock: HttpTestingController) => {
      service.countBuilds().subscribe(count => {
        expect(count).toBe(0);
      });

      const testReq = httpMock.expectOne(requestMatch);

      testReq.error(new ErrorEvent('An unexpected error occurred'));
    }));

  });

});
