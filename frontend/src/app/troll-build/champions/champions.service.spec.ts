import { TestBed, inject } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController, RequestMatch } from '@angular/common/http/testing';

import { ChampionsService } from './champions.service';

import { Champion } from '@ltb-model/champion';

describe('ChampionsService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ChampionsService]
    });
  });

  afterEach(inject([HttpTestingController], (httpMock: HttpTestingController) => {
    httpMock.verify();
  }));

  describe('getChampions', () => {

    const mockChampions: Champion[] = [
      {
        id: 67,
        key: 'Vayne',
        name: 'Vayne',
        title: 'the Night Hunter',
        partype: 'Mana',
        info: {
          attack: 10,
          defense: 1,
          magic: 1,
          difficulty: 8
        },
        spells: [], // omitted for brevity
        passive: {
          name: 'Night Hunter',
          description: 'Vayne ruthlessly hunts evil-doers, gaining 30 movement speed when moving toward nearby enemy ' +
            'champions.'
        },
        tags: [
          'Assassin',
          'Marksman'
        ]
      }
    ];

    const requestMatch: RequestMatch = { method: 'GET', url: '/champions' };

    it('should get Champions', inject([ChampionsService, HttpTestingController],
      (service: ChampionsService, httpMock: HttpTestingController) => {

      service.getChampions().subscribe(champions => {
        expect(champions).toEqual(mockChampions);
      });

      const testReq = httpMock.expectOne(requestMatch);

      testReq.flush(mockChampions);
    }));

    it('should get Champions with REST error', inject([ChampionsService, HttpTestingController],
      (service: ChampionsService, httpMock: HttpTestingController) => {
      service.getChampions().subscribe(champions => {
        expect(champions).toEqual([]);
      });

      const testReq = httpMock.expectOne(requestMatch);

      const errorEvent = document.createEvent('Event');
      errorEvent.initEvent('ErrorEvent', false, false);
      testReq.error(errorEvent as ErrorEvent);
    }));

  });

  describe('getChampionTags', () => {

    const mockTags: string[] = [ 'Assassin', 'Fighter', 'Mage', 'Marksman', 'Support', 'Tank' ];

    const requestMatch: RequestMatch = { method: 'GET', url: '/champions/tags' };

    it('should get Champion tags', inject([ChampionsService, HttpTestingController],
      (service: ChampionsService, httpMock: HttpTestingController) => {

      service.getChampionTags().subscribe(tags => {
        expect(tags).toEqual(mockTags);
      });

      const testReq = httpMock.expectOne(requestMatch);

      testReq.flush(mockTags);
    }));

    it('should get Champion tags with REST error', inject([ChampionsService, HttpTestingController],
      (service: ChampionsService, httpMock: HttpTestingController) => {
      service.getChampionTags().subscribe(tags => {
        expect(tags).toEqual([]);
      });

      const testReq = httpMock.expectOne(requestMatch);

      const errorEvent = document.createEvent('Event');
      errorEvent.initEvent('ErrorEvent', false, false);
      testReq.error(errorEvent as ErrorEvent);
    }));

  });

});
