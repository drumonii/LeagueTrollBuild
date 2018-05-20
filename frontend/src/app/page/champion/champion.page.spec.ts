import { async, ComponentFixture,  inject, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, convertToParamMap } from '@angular/router';

import { of } from 'rxjs/observable/of';

import { ChampionPage } from './champion.page';
import { BuildsService } from '@service/builds.service';
import { ChampionsService } from '@service/champions.service';
import { GameMapsService } from '@service/game-maps.service';
import { Champion } from '@model/champion';
import { GameMap } from '@model/game-map';
import { TrollBuild } from '@model/troll-build';

describe('ChampionPage', () => {
  let component: ChampionPage;
  let fixture: ComponentFixture<ChampionPage>;

  const skarner: Champion = {
    id: 72,
    key: 'Skarner',
    name: 'Skarner',
    title: 'the Crystal Vanguard',
    partype: 'Mana',
    info: {
      attack: 7,
      defense: 6,
      magic: 5,
      difficulty: 5
    },
    spells: [
      {
        key: 'SkarnerVirulentSlash',
        name: 'Crystal Slash',
        description: 'Skarner lashes out with his claws, dealing physical damage to all nearby enemies and charging ' +
        'himself with Crystal Energy for several seconds if a unit is struck. If he casts Crystal Slash again while ' +
        'powered by Crystal Energy, he deals bonus magic damage.',
        tooltip: 'Skarner deals <span class=\'colorFF8C00\'>{{ f1 }}</span> physical damage to all nearby enemies. ' +
        'If a unit is struck, he charges himself with Crystal Energy for {{ e2 }} seconds.<br /><br />While Skarner ' +
        'is charged, Crystal Slash deals <span class=\'colorFF8C00\'>{{ f1 }}</span> <span class=\'color99FF99\'> ' +
        '(+{{ a1 }})</span> bonus magic damage.<br /><br />Basic attacks against non-structures lower Crystal ' +
        'Slash\'s cooldown by 0.25 seconds (quadrupled against champions).',
        image: {
          full: 'SkarnerVirulentSlash.png',
          sprite: 'spell10.png',
          group: 'spell',
          imgSrc: [],
          x: 288,
          y: 96,
          w: 48,
          h: 48
        }
      },
      {
        key: 'SkarnerExoskeleton',
        name: 'Crystalline Exoskeleton',
        description: 'Skarner gains a shield and has increased Movement Speed while the shield persists.',
        tooltip: 'Skarner is shielded for <span class=\'colorCC3300\'>{{ f1 }}</span> ({{ e1 }}% of his maximum health) ' +
        '<span class=\'color99FF99\'>(+{{ a1 }})</span> damage for {{ e4 }} seconds. While the shield persists, ' +
        'Skarner gains movement speed that ramps up to {{ e5 }}% over 3 seconds.',
        image: {
          full: 'SkarnerExoskeleton.png',
          sprite: 'spell10.png',
          group: 'spell',
          imgSrc: [],
          x: 336,
          y: 96,
          w: 48,
          h: 48
        }
      },
      {
        key: 'SkarnerFracture',
        name: 'Fracture',
        description: 'Skarner summons a blast of crystalline energy which deals damage to enemies struck and slows them. ' +
        'Basic attacking these enemies within a short window will stun them.',
        tooltip: '<span class=\'colorFF9900\'>Passive:</span> Crystallizing enemies with Fracture and Impale grants ' +
        '<span class=\'colorFFF673\'>Crystal Charge</span> for the disable duration and reduces the cooldown of ' +
        'Fracture by the same amount.<br /><br /><span class=\'colorFF9900\'>Active:</span> Skarner summons a blast ' +
        'of crystalline energy, dealing {{ e1 }} <span class=\'color99FF99\'>(+{{ a1 }})</span> magic damage, slowing ' +
        'targets hit by {{ e8 }}% for {{ e7 }} seconds and reducing the blast\'s speed.<br /><br />Enemies hit by ' +
        'Fracture are afflicted with Crystal Venom for {{ e6 }} seconds, causing Skarner\'s next basic attack against ' +
        'them to deal {{ e2 }} additional physical damage and stun the target for {{ e3 }} second.',
        image: {
          full: 'SkarnerFracture.png',
          sprite: 'spell10.png',
          group: 'spell',
          imgSrc: [],
          x: 384,
          y: 96,
          w: 48,
          h: 48
        }
      },
      {
        key: 'SkarnerImpale',
        name: 'Impale',
        description: 'Skarner suppresses an enemy champion and deals damage to it. During this time, Skarner can move ' +
        'freely and will drag his helpless victim around with him. When the effect ends, the target will be dealt additional damage.',
        tooltip: 'Skarner suppresses an enemy champion for {{ e1 }} seconds, dealing <span class=\'colorFF8C00\'> ' +
        '{{ a2 }}</span> physical damage plus {{ e2 }} <span class=\'color99FF99\'>(+{{ a1 }})</span> magic damage. ' +
        'Skarner can move freely during this time, and will drag his helpless victim around with him. When the effect ' +
        'ends, Skarner\'s target will be dealt the same damage again.',
        image: {
          full: 'SkarnerImpale.png',
          sprite: 'spell10.png',
          group: 'spell',
          imgSrc: [],
          x: 432,
          y: 96,
          w: 48,
          h: 48
        }
      }
    ],
    passive: {
      name: 'Crystal Spires',
      description: 'Skarner\'s presence causes crystals to spawn in set locations around the map. While near crystals ' +
      'his team owns, Skarner gains tremendous movement speed, attack speed, and mana regeneration.',
      image: {
        full: 'Skarner_Passive.png',
        sprite: 'passive3.png',
        group: 'passive',
        imgSrc: [],
        x: 96,
        y: 48,
        w: 48,
        h: 48
      }
    },
    image: {
      full: 'Skarner.png',
      sprite: 'champion3.png',
      group: 'champion',
      imgSrc: [],
      x: 96,
      y: 48,
      w: 48,
      h: 48
    },
    tags: [
      'Fighter',
      'Tank'
    ]
  };

  const trollBuild = new TrollBuild();

  trollBuild.summonerSpells = [
    {
      id: 12,
      name: 'Teleport',
      description: 'After channeling for 4.5 seconds, teleports your champion to target allied structure, minion, or ward.',
      image: {
        full: 'SummonerTeleport.png',
        sprite: 'spell0.png',
        group: 'spell',
        imgSrc: [],
        x: 336,
        y: 48,
        w: 48,
        h: 48
      },
      cooldown: [
        300
      ],
      key: 'SummonerTeleport',
      modes: [
        'CLASSIC',
        'TUTORIAL'
      ]
    },
    {
      id: 4,
      name: 'Flash',
      description: 'Teleports your champion a short distance toward your cursor\'s location.',
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
    }
  ];

  trollBuild.items = [
    {
      id: 3117,
      name: 'Boots of Mobility',
      group: null,
      consumed: null,
      description: '<groupLimit>Limited to 1 pair of boots.</groupLimit><br><br><unique>UNIQUE Passive - Enhanced ' +
      'Movement:</unique> +25 Movement Speed. Increases to +115 Movement Speed when out of combat for 5 seconds.',
      from: [
        1001
      ],
      into: [ ],
      requiredChampion: null,
      requiredAlly: null,
      maps: {
        '8': true,
        '10': true,
        '11': true,
        '12': true,
        '14': true,
        '16': false,
        '18': true,
        '19': true
      },
      image: {
        full: '3117.png',
        sprite: 'item1.png',
        group: 'item',
        imgSrc: [],
        x: 48,
        y: 240,
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
      id: 3222,
      name: 'Mikael\'s Crucible',
      group: null,
      consumed: null,
      description: '<stats>+40 Magic Resist<br>+10% Cooldown Reduction<br><mana>+100% Base Mana Regen </mana></stats>' +
      '<br><br><unique>UNIQUE Passive:</unique> +20% Heal and Shield Power<br><unique>UNIQUE Passive - Harmony:' +
      '</unique> Grants bonus % Base Health Regen equal to your bonus % Base Mana Regen.<br><active>UNIQUE Active:' +
      '</active> Cleanses all stuns, roots, taunts, fears, silences, and slows on an allied champion and grants them ' +
      'slow immunity for 2 seconds (120 second cooldown). <br><br>Cleansing an effect grants the ally 40% movement ' +
      'speed for 2 seconds.',
      from: [
        3028,
        3114
      ],
      into: [ ],
      requiredChampion: null,
      requiredAlly: null,
      maps: {
        '8': true,
        '10': true,
        '11': true,
        '12': true,
        '14': true,
        '16': false,
        '18': true,
        '19': true
      },
      image: {
        full: '3222.png',
        sprite: 'item1.png',
        group: 'item',
        imgSrc: [],
        x: 96,
        y: 432,
        w: 48,
        h: 48
      },
      gold: {
        base: 500,
        total: 2100,
        sell: 1470,
        purchasable: true
      }
    },
    {
      id: 3050,
      name: 'Zeke\'s Convergence',
      group: null,
      consumed: null,
      description: '<stats>+60 Armor<br>+30 Magic Resist<br><mana>+250 Mana</mana><br>+10% Cooldown Reduction</stats>' +
      '<br><mainText><active>UNIQUE Active - Conduit:</active> Bind to an ally without an existing Conduit.<br>' +
      '<unique>UNIQUE Passive:</unique> Casting your ultimate near your ally surrounds you with a frost storm and ' +
      'ignites your ally\'s basic attacks for 10 seconds. Enemies inside your frost storm are slowed by 20% and your ' +
      'ally\'s attacks burn their target for 50% bonus magic damage over 2 seconds (45 second cooldown).<br><br>' +
      '<unlockedPassive>Frostfire Covenant:</unlockedPassive> Your frost storm ignites when it slows a burning enemy, ' +
      'dealing 40 magic damage per second and slowing by 40% instead for 3 seconds.</maintext><br><br><rules>' +
      '(Champions can only be linked by one Zeke\'s Convergence at a time.)</rules>',
      from: [
        3024,
        3105
      ],
      into: [ ],
      requiredChampion: null,
      requiredAlly: null,
      maps: {
        '8': true,
        '10': true,
        '11': true,
        '12': true,
        '14': true,
        '16': false,
        '18': true,
        '19': true
      },
      image: {
        full: '3050.png',
        sprite: 'item1.png',
        group: 'item',
        imgSrc: [],
        x: 144,
        y: 0,
        w: 48,
        h: 48
      },
      gold: {
        base: 250,
        total: 2250,
        sell: 1575,
        purchasable: true
      }
    },
    {
      id: 3003,
      name: 'Archangel\'s Staff',
      group: null,
      consumed: null,
      description: '<stats>+50 Ability Power<br><mana>+650 Mana</mana><br>+10% Cooldown Reduction</stats><br><br>' +
      '<unique>UNIQUE Passive - Haste:</unique> This item gains an additional 10% Cooldown Reduction.<br><mana>' +
      '<unique>UNIQUE Passive - Awe:</unique> Grants Ability Power equal to 3% of maximum Mana. Refunds 25% of Mana ' +
      'spent.<br><unique>UNIQUE Passive - Mana Charge:</unique> Grants +8 maximum Mana (max +750 Mana) for each Mana ' +
      'expenditure (occurs up to 3 times every 12 seconds).<br><br>Transforms into Seraph\'s Embrace at +750 Mana.</mana>',
      from: [
        1052,
        3070,
        3802
      ],
      into: [ ],
      requiredChampion: null,
      requiredAlly: null,
      maps: {
        '8': false,
        '10': true,
        '11': true,
        '12': true,
        '14': true,
        '16': false,
        '18': true,
        '19': false
      },
      image: {
        full: '3003.png',
        sprite: 'item0.png',
        group: 'item',
        imgSrc: [],
        x: 192,
        y: 336,
        w: 48,
        h: 48
      },
      gold: {
        base: 915,
        total: 3200,
        sell: 2240,
        purchasable: true
      }
    },
    {
      id: 3065,
      name: 'Spirit Visage',
      group: null,
      consumed: null,
      description: '<stats>+450 Health<br>+55 Magic Resist<br>+100% Base Health Regen <br>+10% Cooldown Reduction' +
      '</stats><br><br><unique>UNIQUE Passive:</unique> Increases all healing received by 30%.',
      from: [
        3067,
        3211
      ],
      into: [ ],
      requiredChampion: null,
      requiredAlly: null,
      maps: {
        '8': true,
        '10': true,
        '11': true,
        '12': true,
        '14': true,
        '16': false,
        '18': true,
        '19': true
      },
      image: {
        full: '3065.png',
        sprite: 'item1.png',
        group: 'item',
        imgSrc: [],
        x: 432,
        y: 0,
        w: 48,
        h: 48
      },
      gold: {
        base: 800,
        total: 2800,
        sell: 1960,
        purchasable: true
      }
    },
    {
      id: 3072,
      name: 'The Bloodthirster',
      group: null,
      consumed: null,
      description: '<stats>+80 Attack Damage</stats><br><br><unique>UNIQUE Passive:</unique> +20% Life Steal<br>' +
      '<unique>UNIQUE Passive:</unique> Your basic attacks can now overheal you. Excess life is stored as a shield ' +
      'that can block 50-350 damage, based on champion level.<br><br>This shield decays slowly if you haven\'t dealt ' +
      'or taken damage in the last 25 seconds.',
      from: [
        1036,
        1038,
        1053
      ],
      into: [ ],
      requiredChampion: null,
      requiredAlly: null,
      maps: {
        '8': true,
        '10': true,
        '11': true,
        '12': true,
        '14': true,
        '16': false,
        '18': true,
        '19': true
      },
      image: {
        full: '3072.png',
        sprite: 'item1.png',
        group: 'item',
        imgSrc: [],
        x: 240,
        y: 48,
        w: 48,
        h: 48
      },
      gold: {
        base: 1150,
        total: 3700,
        sell: 2590,
        purchasable: true
      }
    }
  ];

  trollBuild.trinket = {
    id: 3341,
    name: 'Sweeping Lens (Trinket)',
    group: null,
    consumed: null,
    description: '<groupLimit>Limited to 1 Trinket.</groupLimit><br><br><active>Active:</active> Scans an area for ' +
    '6 seconds, warning against hidden hostile units and revealing invisible traps and revealing / disabling wards ' +
    '(90 to 60 second cooldown).<br><br>Cast range and sweep radius gradually improve with level.<br><br><rules> ' +
    '(Switching to a <font color=\'#BBFFFF\'>Totem</font>-type trinket will disable <font color=\'#BBFFFF\'>Trinket ' +
    '</font> use for 120 seconds.)</rules>',
    from: [],
    into: [],
    requiredChampion: null,
    requiredAlly: null,
    maps: {
      '8': false,
      '10': false,
      '11': true,
      '12': true,
      '14': true,
      '16': false,
      '18': true,
      '19': false
    },
    image: {
      full: '3341.png',
      sprite: 'item2.png',
      group: 'item',
      imgSrc: [],
      x: 384,
      y: 0,
      w: 48,
      h: 48
    },
    gold: {
      base: 0,
      total: 0,
      sell: 0,
      purchasable: true
    }
  };

  const maps = [
    {
      mapId: 12,
      mapName: 'Howling Abyss',
      image: {
        full: 'map12.png',
        sprite: 'map0.png',
        group: 'map',
        imgSrc: [],
        x: 144,
        y: 0,
        w: 48,
        h: 48
      }
    },
    {
      mapId: 11,
      mapName: 'Summoner\'s Rift',
      image: {
        full: 'map11.png',
        sprite: 'map0.png',
        group: 'map',
        imgSrc: [],
        x: 96,
        y: 0,
        w: 48,
        h: 48
      }
    },
    {
      mapId: 10,
      mapName: 'The Twisted Treeline',
      image: {
        full: 'map10.png',
        sprite: 'map0.png',
        group: 'map',
        imgSrc: [],
        x: 48,
        y: 0,
        w: 48,
        h: 48
      }
    }
  ];

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [FormsModule, HttpClientTestingModule, RouterTestingModule],
      declarations: [ChampionPage],
      providers: [
        BuildsService,
        ChampionsService,
        GameMapsService,
        {
          provide: ActivatedRoute,
          useValue: {
            paramMap: of(convertToParamMap({'name': 'Skarner'}))
          }
        }
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ChampionPage);
    component = fixture.componentInstance;
  });

  it('should show Champion by name with a Troll Build and maps to select',
    inject([ChampionsService, GameMapsService], (championsService: ChampionsService, gameMapsService: GameMapsService) => {
    spyOn(championsService, 'getChampion').and.returnValue(of(skarner));
    spyOn(championsService, 'getTrollBuild').and.returnValue(of(trollBuild));
    spyOn(gameMapsService, 'forTrollBuild').and.returnValue(of(maps));

    fixture.detectChanges();

    expect(championsService.getChampion).toHaveBeenCalledWith('Skarner');
    expect(championsService.getTrollBuild).toHaveBeenCalledWith('Skarner', GameMap.summonersRiftId);
    expect(gameMapsService.forTrollBuild).toHaveBeenCalled();
  }));
});
