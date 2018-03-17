import { TestBed, inject } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ChampionsService } from './champions.service';

import { Champion } from '@model/champion';

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

  it('should get Champions', inject([ChampionsService, HttpTestingController],
    (service: ChampionsService, httpMock: HttpTestingController) => {
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
            'champions.',
          image: {
            full: 'Vayne_NightHunter.png',
            sprite: 'passive4.png',
            group: 'passive',
            imgSrc: [],
            x: 0,
            y: 0,
            w: 48,
            h: 48
          }
        },
        image: {
          full: 'Vayne.png',
          sprite: 'champion4.png',
          group: 'champion',
          imgSrc: [],
          x: 0,
          y: 0,
          w: 48,
          h: 48
        },
        tags: [
          'Assassin',
          'Marksman'
        ]
      }
    ];

    service.getChampions().subscribe(champions => {
      expect(champions).toEqual(mockChampions);
    });

    const testReq = httpMock.expectOne('/api/champions');
    expect(testReq.request.method).toEqual('GET');

    testReq.flush(mockChampions);
  }));

  it('should get Champions with REST error', inject([ChampionsService, HttpTestingController],
    (service: ChampionsService, httpMock: HttpTestingController) => {
    service.getChampions().subscribe(champions => {
      expect(champions).toEqual([]);
    });

    const testReq = httpMock.expectOne('/api/champions');
    expect(testReq.request.method).toEqual('GET');

    testReq.error(new ErrorEvent('An unexpected error occurred'));
  }));
});
