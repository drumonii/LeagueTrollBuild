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

  it('should get a Champion', inject([ChampionsService, HttpTestingController],
    (service: ChampionsService, httpMock: HttpTestingController) => {
    const mockChampion: Champion = {
      id: 9,
      key: 'Fiddlesticks',
      name: 'Fiddlesticks',
      title: 'the Harbinger of Doom',
      partype: 'Mana',
      info: {
        attack: 2,
        defense: 3,
        magic: 9,
        difficulty: 9
      },
      spells: [], // omitted for brevity
      passive: {
        name: 'Dread',
        description: 'Standing still or channeling abilities for 1.5 seconds empowers Fiddlesticks with Dread. ' +
          'Immobilizing crowd control resets this timer.<br><br>Dread grants Movement Speed, but only lasts for 1.5s ' +
          'after Fiddlesticks starts moving.',
        image: {
          full: 'Fiddlesticks_Passive.png',
          sprite: 'passive0.png',
          group: 'passive',
          imgSrc: [],
          x: 336,
          y: 96,
          w: 48,
          h: 48
        }
      },
      image: {
        full: 'Fiddlesticks.png',
        sprite: 'champion0.png',
        group: 'champion',
        imgSrc: [],
        x: 336,
        y: 96,
        w: 48,
        h: 48
      },
      tags: [
        'Mage',
        'Support'
      ]
    };

    service.getChampion(mockChampion.name).subscribe(champion => {
      expect(champion).toEqual(mockChampion);
    });

    const testReq = httpMock.expectOne(`/api/champions/${mockChampion.name}`);
    expect(testReq.request.method).toEqual('GET');

    testReq.flush(mockChampion);
  }));

  it('should get a Champion with REST error', inject([ChampionsService, HttpTestingController],
    (service: ChampionsService, httpMock: HttpTestingController) => {
    const name = 'Graves';
    service.getChampion(name).subscribe(champion => {
      expect(champion).toBeNull();
    });

    const testReq = httpMock.expectOne(`/api/champions/${name}`);
    expect(testReq.request.method).toEqual('GET');

    testReq.error(new ErrorEvent('An unexpected error occurred'));
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

  it('should get Champion tags', inject([ChampionsService, HttpTestingController],
    (service: ChampionsService, httpMock: HttpTestingController) => {
    const mockTags: string[] = [ 'Assassin', 'Fighter', 'Mage', 'Marksman', 'Support', 'Tank' ];

    service.getChampionTags().subscribe(tags => {
      expect(tags).toEqual(mockTags);
    });

    const testReq = httpMock.expectOne('/api/champions/tags');
    expect(testReq.request.method).toEqual('GET');

    testReq.flush(mockTags);
  }));

  it('should get Champion tags with REST error', inject([ChampionsService, HttpTestingController],
    (service: ChampionsService, httpMock: HttpTestingController) => {
    service.getChampionTags().subscribe(tags => {
      expect(tags).toEqual([]);
    });

    const testReq = httpMock.expectOne('/api/champions/tags', 'GET Champion Tags');
    expect(testReq.request.method).toEqual('GET');

    testReq.error(new ErrorEvent('An unexpected error occurred'));
  }));
});
