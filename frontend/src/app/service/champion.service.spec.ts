import { TestBed, inject } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ChampionService } from './champion.service';

import { Champion } from '@model/champion';
import { TrollBuild } from '@model/troll-build';

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

    it('should get a Champion', inject([ChampionService, HttpTestingController],
      (service: ChampionService, httpMock: HttpTestingController) => {
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

      service.getChampion(mockChampion.name).subscribe(champion => {
        expect(champion).toEqual(mockChampion);
      });

      const testReq = httpMock.expectOne(`/api/champions/${mockChampion.name}`);
      expect(testReq.request.method).toEqual('GET');

      testReq.flush(mockChampion);
    }));

    it('should get a Champion with REST error', inject([ChampionService, HttpTestingController],
      (service: ChampionService, httpMock: HttpTestingController) => {
      const name = 'Graves';
      service.getChampion(name).subscribe(champion => {
        expect(champion).toBeNull();
      });

      const testReq = httpMock.expectOne(`/api/champions/${name}`);
      expect(testReq.request.method).toEqual('GET');

      testReq.error(new ErrorEvent('An unexpected error occurred'));
    }));

  });

  describe('getTrollBuild', () => {

    it('should get a Troll Build with a Map Id', inject([ChampionService, HttpTestingController],
      (service: ChampionService, httpMock: HttpTestingController) => {
      const name = 'Talon';
      const gameMapId = 11;

      const mockTrollBuild = new TrollBuild();

      mockTrollBuild.summonerSpells = [
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
      ];

      mockTrollBuild.items = [
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
      ];

      mockTrollBuild.trinket = {
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
      };

      service.getTrollBuild(name, gameMapId).subscribe(trollBuild => {
        expect(trollBuild).toEqual(mockTrollBuild);
      });

      const testReq = httpMock.expectOne(`/api/champions/${name}/troll-build?mapId=${gameMapId}`);
      expect(testReq.request.method).toEqual('GET');

      testReq.flush(mockTrollBuild);
    }));

    it('should get a Troll Build without a Map Id', inject([ChampionService, HttpTestingController],
      (service: ChampionService, httpMock: HttpTestingController) => {
      const name = 'Hecarim';

      const mockTrollBuild = new TrollBuild();

      mockTrollBuild.summonerSpells = [
        {
          id: 4,
          name: 'Flash',
          description: '',
          cooldown: [
            300
          ],
          key: 'SummonerFlash',
          modes: [
            'ARAM',
            'CLASSIC',
            'TUTORIAL'
          ]
        },
        {
          id: 1,
          name: 'Cleanse',
          description: '',
          cooldown: [
            210
          ],
          key: 'SummonerBoost',
          modes: [
            'ARAM',
            'CLASSIC',
            'TUTORIAL'
          ]
        }
      ];

      mockTrollBuild.items = [
        {
          id: 3009,
          name: 'Boots of Swiftness',
          group: null,
          consumed: null,
          description: '',
          from: [
            1001
          ],
          into: [],
          requiredChampion: null,
          requiredAlly: null,
          maps: {
            '10': true,
            '11': true,
            '12': true
          },
          gold: {
            base: 600,
            total: 900,
            sell: 630,
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
            '10': true,
            '11': true,
            '12': true
          },
          gold: {
            base: 1000,
            total: 3000,
            sell: 2100,
            purchasable: true
          }
        },
        {
          id: 3075,
          name: 'Thornmail',
          group: null,
          consumed: null,
          description: '',
          from: [
            1026,
            1028,
            3076,
            3082
          ],
          into: [],
          requiredChampion: null,
          requiredAlly: null,
          maps: {
            '10': true,
            '11': true,
            '12': true
          },
          gold: {
            base: 500,
            total: 2900,
            sell: 2030,
            purchasable: true
          }
        },
        {
          id: 3053,
          name: 'Sterak\'s Gage',
          group: null,
          consumed: null,
          description: '',
          from: [
            1028,
            1037,
            3052
          ],
          into: [],
          requiredChampion: null,
          requiredAlly: null,
          maps: {
            '10': true,
            '11': true,
            '12': true
          },
          gold: {
            base: 725,
            total: 3200,
            sell: 2240,
            purchasable: true
          }
        },
        {
          id: 3508,
          name: 'Essence Reaver',
          group: null,
          consumed: null,
          description: '',
          from: [
            1027,
            1038,
            3133
          ],
          into: [],
          requiredChampion: null,
          requiredAlly: null,
          maps: {
            '10': true,
            '11': true,
            '12': true
          },
          gold: {
            base: 450,
            total: 3200,
            sell: 2240,
            purchasable: true
          }
        },
        {
          id: 3074,
          name: 'Ravenous Hydra',
          group: null,
          consumed: null,
          description: '',
          from: [
            1037,
            1053,
            3077
          ],
          into: [],
          requiredChampion: null,
          requiredAlly: null,
          maps: {
            '10': true,
            '11': true,
            '12': true
          },
          gold: {
            base: 525,
            total: 3500,
            sell: 2450,
            purchasable: true
          }
        }
      ];

      mockTrollBuild.trinket = {
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
          '10': false,
          '11': true,
          '12': true
        },
        gold: {
          base: 0,
          total: 0,
          sell: 0,
          purchasable: true
        }
      };

      service.getTrollBuild(name).subscribe(trollBuild => {
        expect(trollBuild).toEqual(mockTrollBuild);
      });

      const testReq = httpMock.expectOne(`/api/champions/${name}/troll-build`);
      expect(testReq.request.method).toEqual('GET');

      testReq.flush(mockTrollBuild);
    }));

    it('should get a Troll Build with a Map Id and with REST error', inject([ChampionService, HttpTestingController],
      (service: ChampionService, httpMock: HttpTestingController) => {
      const name = 'Olaf';
      const gameMapId = 11;

      service.getTrollBuild(name, gameMapId).subscribe(trollBuild => {
        expect(trollBuild).toEqual(new TrollBuild());
      });

      const testReq = httpMock.expectOne(`/api/champions/${name}/troll-build?mapId=${gameMapId}`);
      expect(testReq.request.method).toEqual('GET');

      testReq.error(new ErrorEvent('An unexpected error occurred'));
    }));

    it('should get a Troll Build without a Map Id and with REST error', inject([ChampionService, HttpTestingController],
      (service: ChampionService, httpMock: HttpTestingController) => {
      const name = 'Bard';

      service.getTrollBuild(name).subscribe(trollBuild => {
        expect(trollBuild).toEqual(new TrollBuild());
      });

      const testReq = httpMock.expectOne(`/api/champions/${name}/troll-build`);
      expect(testReq.request.method).toEqual('GET');

      testReq.error(new ErrorEvent('An unexpected error occurred'));
    }));

  });

});
