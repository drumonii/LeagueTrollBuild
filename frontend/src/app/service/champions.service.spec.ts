import { TestBed, inject } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ChampionsService } from './champions.service';

import { Champion } from '@model/champion';
import { Item } from '@model/item';
import { SummonerSpell } from '@model/summoner-spell';

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

  it('should get a Troll Build with a Map Id', inject([ChampionsService, HttpTestingController],
    (service: ChampionsService, httpMock: HttpTestingController) => {
    const name = 'Talon';
    const gameMapId = 11;

    const mockTrollBuild = new Map<String, Item[] | SummonerSpell[]>();

    mockTrollBuild.set('summoner-spells', [
      {
        id: 7,
        name: 'Heal',
        description: '',
        image: {
          full: 'SummonerHeal.png',
          sprite: 'spell0.png',
          group: 'spell',
          imgSrc: [],
          x: 384,
          y: 0,
          w: 48,
          h: 48
        },
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
        image: {
          full: 'SummonerSmite.png',
          sprite: 'spell0.png',
          group: 'spell',
          imgSrc: [],
          x: 192,
          y: 48,
          w: 48,
          h: 48
        },
        cooldown: [
          15
        ],
        key: 'SummonerSmite',
        modes: [
          'CLASSIC',
          'TUTORIAL'
        ]
      }
    ]);

    mockTrollBuild.set('items', [
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
        image: {
          full: '3111.png',
          sprite: 'item1.png',
          group: 'item',
          imgSrc: [],
          x: 192,
          y: 192,
          w: 48,
          h: 48
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
        image: {
          full: '3401.png',
          sprite: 'item2.png',
          group: 'item',
          imgSrc: [],
          x: 192,
          y: 96,
          w: 48,
          h: 48
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
        image: {
          full: '3116.png',
          sprite: 'item1.png',
          group: 'item',
          imgSrc: [],
          x: 432,
          y: 192,
          w: 48,
          h: 48
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
        image: {
          full: '3174.png',
          sprite: 'item1.png',
          group: 'item',
          imgSrc: [],
          x: 288,
          y: 336,
          w: 48,
          h: 48
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
        image: {
          full: '3156.png',
          sprite: 'item1.png',
          group: 'item',
          imgSrc: [],
          x: 48,
          y: 336,
          w: 48,
          h: 48
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
        image: {
          full: '3115.png',
          sprite: 'item1.png',
          group: 'item',
          imgSrc: [],
          x: 384,
          y: 192,
          w: 48,
          h: 48
        },
        gold: {
          base: 1000,
          total: 3000,
          sell: 2100,
          purchasable: true
        }
      }
    ]);

    mockTrollBuild.set('trinket', [
      {
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
        image: {
          full: '3363.png',
          sprite: 'item2.png',
          group: 'item',
          imgSrc: [],
          x: 96,
          y: 48,
          w: 48,
          h: 48
        },
        gold: {
          base: 0,
          total: 0,
          sell: 0,
          purchasable: true
        }
      }
    ]);

    service.getTrollBuild(name, gameMapId).subscribe(trollBuild => {
      expect(trollBuild).toEqual(mockTrollBuild);
    });

    const testReq = httpMock.expectOne(`/api/champions/${name}/troll-build?mapId=${gameMapId}`);
    expect(testReq.request.method).toEqual('GET');

    testReq.flush(mockTrollBuild);
  }));

  it('should get a Troll Build without a Map Id', inject([ChampionsService, HttpTestingController],
    (service: ChampionsService, httpMock: HttpTestingController) => {
    const name = 'Hecarim';

    const mockTrollBuild = new Map<String, Item[] | SummonerSpell[]>();

    mockTrollBuild.set('summoner-spells', [
      {
        id: 4,
        name: 'Flash',
        description: '',
        image: {
          full: 'SummonerFlash.png',
          sprite: 'spell0.png',
          group: 'spell',
          imgSrc: [],
          x: 288,
          y: 0,
          w: 48,
          h: 48
        },
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
        image: {
          full: 'SummonerBoost.png',
          sprite: 'spell0.png',
          group: 'spell',
          imgSrc: [],
          x: 48,
          y: 0,
          w: 48,
          h: 48
        },
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
    ]);

    mockTrollBuild.set('items', [
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
        image: {
          full: '3009.png',
          sprite: 'item0.png',
          group: 'item',
          imgSrc: [],
          x: 384,
          y: 336,
          w: 48,
          h: 48
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
        image: {
          full: '3115.png',
          sprite: 'item1.png',
          group: 'item',
          imgSrc: [],
          x: 384,
          y: 192,
          w: 48,
          h: 48
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
        image: {
          full: '3075.png',
          sprite: 'item1.png',
          group: 'item',
          imgSrc: [],
          x: 288,
          y: 48,
          w: 48,
          h: 48
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
        image: {
          full: '3053.png',
          sprite: 'item1.png',
          group: 'item',
          imgSrc: [],
          x: 144,
          y: 0,
          w: 48,
          h: 48
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
        image: {
          full: '3508.png',
          sprite: 'item2.png',
          group: 'item',
          imgSrc: [],
          x: 96,
          y: 144,
          w: 48,
          h: 48
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
        image: {
          full: '3074.png',
          sprite: 'item1.png',
          group: 'item',
          imgSrc: [],
          x: 240,
          y: 48,
          w: 48,
          h: 48
        },
        gold: {
          base: 525,
          total: 3500,
          sell: 2450,
          purchasable: true
        }
      }
    ]);

    mockTrollBuild.set('trinket', [
      {
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
        image: {
          full: '3363.png',
          sprite: 'item2.png',
          group: 'item',
          imgSrc: [],
          x: 0,
          y: 48,
          w: 48,
          h: 48
        },
        gold: {
          base: 0,
          total: 0,
          sell: 0,
          purchasable: true
        }
      }
    ]);

    service.getTrollBuild(name).subscribe(trollBuild => {
      expect(trollBuild).toEqual(mockTrollBuild);
    });

    const testReq = httpMock.expectOne(`/api/champions/${name}/troll-build`);
    expect(testReq.request.method).toEqual('GET');

    testReq.flush(mockTrollBuild);
  }));

  it('should get a Troll Build with a Map Id and with REST error', inject([ChampionsService, HttpTestingController],
    (service: ChampionsService, httpMock: HttpTestingController) => {
    const name = 'Olaf';
    const gameMapId = 11;

    service.getTrollBuild(name, gameMapId).subscribe(trollBuild => {
      expect(trollBuild).toEqual(new Map());
    });

    const testReq = httpMock.expectOne(`/api/champions/${name}/troll-build?mapId=${gameMapId}`);
    expect(testReq.request.method).toEqual('GET');

    testReq.error(new ErrorEvent('An unexpected error occurred'));
  }));

  it('should get a Troll Build without a Map Id and with REST error', inject([ChampionsService, HttpTestingController],
    (service: ChampionsService, httpMock: HttpTestingController) => {
    const name = 'Bard';

    service.getTrollBuild(name).subscribe(trollBuild => {
      expect(trollBuild).toEqual(new Map());
    });

    const testReq = httpMock.expectOne(`/api/champions/${name}/troll-build`);
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
