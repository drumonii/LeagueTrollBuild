import { TestBed, inject } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController, RequestMatch } from '@angular/common/http/testing';

import { ChampionService } from './champion.service';

import { Champion } from '@model/champion';
import { GameMap } from '@model/game-map';
import { TrollBuild } from '@model/troll-build';
import { Build } from '@model/build';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

describe('ChampionService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ChampionService]
    });
  });

  afterEach(inject([HttpTestingController], (httpMock: HttpTestingController) => {
    httpMock.verify();
  }));

  describe('getChampion', () => {

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
          'after Fiddlesticks starts moving.'
      },
      tags: [
        'Mage',
        'Support'
      ]
    };

    const requestMatch: RequestMatch = { method: 'GET', url: `/champions/${mockChampion.name}` };

    it('should get a Champion', inject([ChampionService, HttpTestingController],
      (service: ChampionService, httpMock: HttpTestingController) => {
      service.getChampion(mockChampion.name).subscribe(champion => {
        expect(champion).toEqual(mockChampion);
      });

      const testReq = httpMock.expectOne(requestMatch);

      testReq.flush(mockChampion);
    }));

    it('should get a Champion with REST error', inject([ChampionService, HttpTestingController],
      (service: ChampionService, httpMock: HttpTestingController) => {
      service.getChampion(mockChampion.name).subscribe(champion => {
        expect(champion).toBeNull();
      });

      const testReq = httpMock.expectOne(requestMatch);

      testReq.error(new ErrorEvent('An unexpected error occurred'));
    }));

  });

  describe('getTrollBuild', () => {

    const championName = 'Talon';
    const gameMapId = GameMap.summonersRiftId;

    const mockTrollBuild: TrollBuild = {
      summonerSpells: [
        {
          id: 7,
          name: 'Heal',
          description: '',
          cooldown: [
            240
          ],
          key: 'SummonerHeal',
          modes: [
            'ARAM',
            'CLASSIC',
            'TUTORIAL'
          ]
        },
        {
          id: 11,
          name: 'Smite',
          description: '',
          cooldown: [
            15
          ],
          key: 'SummonerSmite',
          modes: [
            'CLASSIC',
            'TUTORIAL'
          ]
        }
      ],
      items: [
        {
          id: 3111,
          name: 'Mercury\'s Treads',
          group: null,
          consumed: null,
          description: '',
          from: [
            1001,
            1033
          ],
          into: [],
          requiredChampion: null,
          requiredAlly: null,
          maps: {
            '11': true
          },
          gold: {
            base: 350,
            total: 1100,
            sell: 770,
            purchasable: true
          }
        },
        {
          id: 3401,
          name: 'Remnant of the Aspect',
          group: null,
          consumed: null,
          description: '',
          from: [
            1028,
            3097
          ],
          into: [],
          requiredChampion: null,
          requiredAlly: null,
          maps: {
            '11': true
          },
          gold: {
            base: 550,
            total: 1800,
            sell: 720,
            purchasable: true
          }
        },
        {
          id: 3116,
          name: 'Rylai\'s Crystal Scepter',
          group: null,
          consumed: null,
          description: '',
          from: [
            1026,
            1028,
            1052
          ],
          into: [],
          requiredChampion: null,
          requiredAlly: null,
          maps: {
            '11': true
          },
          gold: {
            base: 915,
            total: 2600,
            sell: 1820,
            purchasable: true
          }
        },
        {
          id: 3174,
          name: 'Athene\'s Unholy Grail',
          group: null,
          consumed: null,
          description: '',
          from: [
            3028,
            3108
          ],
          into: [],
          requiredChampion: null,
          requiredAlly: null,
          maps: {
            '11': true
          },
          gold: {
            base: 400,
            total: 2100,
            sell: 1470,
            purchasable: true
          }
        },
        {
          id: 3156,
          name: 'Maw of Malmortius',
          group: null,
          consumed: null,
          description: '',
          from: [
            3133,
            3155
          ],
          into: [],
          requiredChampion: null,
          requiredAlly: null,
          maps: {
            '11': true
          },
          gold: {
            base: 850,
            total: 3250,
            sell: 2275,
            purchasable: true
          }
        },
        {
          id: 3115,
          name: 'Nashor\'s Tooth',
          group: null,
          consumed: null,
          description: '',
          from: [
            3101,
            3108
          ],
          into: [],
          requiredChampion: null,
          requiredAlly: null,
          maps: {
            '11': true
          },
          gold: {
            base: 1000,
            total: 3000,
            sell: 2100,
            purchasable: true
          }
        }
      ],
      trinket: {
        id: 3363,
        name: 'Farsight Alteration',
        group: null,
        consumed: null,
        description: '',
        from: [],
        into: [],
        requiredChampion: null,
        requiredAlly: null,
        maps: {
          '11': true
        },
        gold: {
          base: 0,
          total: 0,
          sell: 0,
          purchasable: true
        }
      }
    };

    const requestMatchWithMapId: RequestMatch = { method: 'GET', url: `/champions/${championName}/troll-build?mapId=${gameMapId}` };
    const requestMatchWithoutMapId: RequestMatch = { method: 'GET', url: `/champions/${championName}/troll-build` };

    it('should get a Troll Build with a Map Id', inject([ChampionService, HttpTestingController],
      (service: ChampionService, httpMock: HttpTestingController) => {
      service.getTrollBuild(championName, gameMapId).subscribe(trollBuild => {
        expect(trollBuild).toEqual(mockTrollBuild);
      });

      const testReq = httpMock.expectOne(requestMatchWithMapId);

      testReq.flush(mockTrollBuild);
    }));

    it('should get a Troll Build without a Map Id', inject([ChampionService, HttpTestingController],
      (service: ChampionService, httpMock: HttpTestingController) => {
      service.getTrollBuild(championName).subscribe(trollBuild => {
        expect(trollBuild).toEqual(mockTrollBuild);
      });

      const testReq = httpMock.expectOne(requestMatchWithoutMapId);

      testReq.flush(mockTrollBuild);
    }));

    it('should get a Troll Build with a Map Id and with REST error', inject([ChampionService, HttpTestingController],
      (service: ChampionService, httpMock: HttpTestingController) => {
      service.getTrollBuild(championName, gameMapId).subscribe(trollBuild => {
        expect(trollBuild).toEqual(new TrollBuild());
      });

      const testReq = httpMock.expectOne(requestMatchWithMapId);

      testReq.error(new ErrorEvent('An unexpected error occurred'));
    }));

    it('should get a Troll Build without a Map Id and with REST error', inject([ChampionService, HttpTestingController],
      (service: ChampionService, httpMock: HttpTestingController) => {
      service.getTrollBuild(championName).subscribe(trollBuild => {
        expect(trollBuild).toEqual(new TrollBuild());
      });

      const testReq = httpMock.expectOne(requestMatchWithoutMapId);

      testReq.error(new ErrorEvent('An unexpected error occurred'));
    }));

  });

  describe('mapsForTrollBuild', () => {

    const mockGameMaps: GameMap[] = [
      {
        mapId: 11,
        mapName: 'Summoner\'s Rift'
      }
    ];

    const requestMatch: RequestMatch = { method: 'GET', url: '/maps/for-troll-build' };

    it('should get a Game maps for Troll Build', inject([ChampionService, HttpTestingController],
      (service: ChampionService, httpMock: HttpTestingController) => {
      service.mapsForTrollBuild().subscribe(gameMaps => {
        expect(gameMaps).toEqual(mockGameMaps);
      });

      const testReq = httpMock.expectOne(requestMatch);

      testReq.flush(mockGameMaps);
    }));

    it('should get a Game maps for Troll Build with REST error', inject([ChampionService, HttpTestingController],
      (service: ChampionService, httpMock: HttpTestingController) => {
      service.mapsForTrollBuild().subscribe(gameMaps => {
        expect(gameMaps).toEqual([]);
      });

      const testReq = httpMock.expectOne(requestMatch);

      testReq.error(new ErrorEvent('An unexpected error occurred'));
    }));

  });

  describe('saveBuild', () => {

    const requestMatch: RequestMatch = { method: 'POST', url: '/builds' };

    it('should POST a build', inject([ChampionService, HttpTestingController],
      (service: ChampionService, httpMock: HttpTestingController) => {
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

      const testReq = httpMock.expectOne(requestMatch);
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

    it('should POST a build with REST error', inject([ChampionService, HttpTestingController],
      (service: ChampionService, httpMock: HttpTestingController) => {
      service.saveBuild(new Build()).subscribe(res => {
        expect(res).toBeNull();
      });

      const testReq = httpMock.expectOne(requestMatch);
      expect(testReq.request.detectContentTypeHeader()).toBe('application/json');

      testReq.error(new ErrorEvent('An unexpected error occurred'));
    }));

  });

});
