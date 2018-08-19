import { TestBed, inject } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { GameMapsService } from './game-maps.service';

import { GameMap } from '@model/game-map';

describe('GameMapsService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [GameMapsService]
    });
  });

  afterEach(inject([HttpTestingController], (httpMock: HttpTestingController) => {
    httpMock.verify();
  }));

  describe('forTrollBuild', () => {

    it('should get a Game maps for Troll Build', inject([GameMapsService, HttpTestingController],
      (service: GameMapsService, httpMock: HttpTestingController) => {
      const mockGameMaps: GameMap[] = [
        {
          mapId: 11,
          mapName: 'Summoner\'s Rift'
        }
      ];

      service.forTrollBuild().subscribe(gameMaps => {
        expect(gameMaps).toEqual(mockGameMaps);
      });

      const testReq = httpMock.expectOne('/api/maps/for-troll-build');
      expect(testReq.request.method).toEqual('GET');

      testReq.flush(mockGameMaps);
    }));

    it('should get a Game maps for Troll Build with REST error', inject([GameMapsService, HttpTestingController],
      (service: GameMapsService, httpMock: HttpTestingController) => {
      service.forTrollBuild().subscribe(gameMaps => {
        expect(gameMaps).toEqual([]);
      });

      const testReq = httpMock.expectOne('/api/maps/for-troll-build');
      expect(testReq.request.method).toEqual('GET');

      testReq.error(new ErrorEvent('An unexpected error occurred'));
    }));

  });

});
