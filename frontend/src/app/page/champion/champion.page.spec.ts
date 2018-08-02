import { async, ComponentFixture, inject, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { By, Title } from '@angular/platform-browser';

import { of } from 'rxjs';

import { ChampionPage } from './champion.page';
import { ChampionModule } from './champion.module';
import { BuildsService } from '@service/builds.service';
import { ChampionService } from '@service/champion.service';
import { GameMapsService } from '@service/game-maps.service';
import { Champion } from '@model/champion';
import { GameMap } from '@model/game-map';
import { TrollBuild } from '@model/troll-build';
import { Build } from '@model/build';

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
      imports: [ChampionModule, FormsModule, HttpClientTestingModule, RouterTestingModule],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({ champion: skarner })
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

  describe('show Champion by name with a Troll Build and maps to select', () => {

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

    const build = new Build();
    build.championId = skarner.id;
    build.item1Id = trollBuild.items[0].id;
    build.item2Id = trollBuild.items[1].id;
    build.item3Id = trollBuild.items[2].id;
    build.item4Id = trollBuild.items[3].id;
    build.item5Id = trollBuild.items[4].id;
    build.item6Id = trollBuild.items[5].id;
    build.summonerSpell1Id = trollBuild.summonerSpells[0].id;
    build.summonerSpell2Id = trollBuild.summonerSpells[1].id;
    build.trinketId = trollBuild.trinket.id;
    build.mapId = GameMap.summonersRiftId;

    beforeEach(inject([ChampionService, GameMapsService], (championsService: ChampionService, gameMapsService: GameMapsService) => {
      spyOn(championsService, 'getChampion').and.returnValue(of(skarner));
      spyOn(championsService, 'getTrollBuild').and.returnValue(of(trollBuild));
      spyOn(gameMapsService, 'forTrollBuild').and.returnValue(of(maps));
    }));

    afterEach(inject([ChampionService, GameMapsService], (championsService: ChampionService, gameMapsService: GameMapsService) => {
      expect(championsService.getChampion).not.toHaveBeenCalledWith('Skarner');
      expect(championsService.getTrollBuild).toHaveBeenCalledWith('Skarner', GameMap.summonersRiftId);
      expect(gameMapsService.forTrollBuild).toHaveBeenCalled();
    }));

    it('should append Champion as the content title', inject([Title], (title: Title) => {
      spyOn(title, 'getTitle').and.returnValue('League Troll Build');
      spyOn(title, 'setTitle').and.callThrough();

      fixture.detectChanges();

      expectChampionAndMapsAndTrollBuild();

      expect(title.setTitle).toHaveBeenCalledWith(`League Troll Build | Skarner`);
    }));

    it('should replace an existing Champion as the content title', inject([Title], (title: Title) => {
      spyOn(title, 'getTitle').and.returnValue('League Troll Build | Warwick');
      spyOn(title, 'setTitle').and.callThrough();

      fixture.detectChanges();

      expectChampionAndMapsAndTrollBuild();

      expect(title.setTitle).toHaveBeenCalledWith(`League Troll Build | Skarner`);
    }));

    it('should generate a new Troll Build after selecting a new map',
      inject([ChampionService], (championsService: ChampionService) => {
      fixture.detectChanges();

      expectChampionAndMapsAndTrollBuild();

      const notSummonersRift = maps.find(gameMap => gameMap.mapId !== GameMap.summonersRiftId);
      const notSummonersRifIndex = maps.findIndex(gameMap => gameMap.mapId === notSummonersRift.mapId);

      const mapSelectDe = fixture.debugElement.query(By.css('#map-select'));
      mapSelectDe.nativeElement.value = mapSelectDe.nativeElement.options[notSummonersRifIndex].value;
      mapSelectDe.nativeElement.dispatchEvent(new Event('change'));

      fixture.detectChanges();

      expect(championsService.getTrollBuild).toHaveBeenCalledWith('Skarner', notSummonersRift.mapId);
      expect(component.build).toBeNull();
    }));

    it('should reset the saved Build and generate a new Troll Build after clicking the new build button',
      inject([ChampionService], (championsService: ChampionService) => {
      fixture.detectChanges();

      expectChampionAndMapsAndTrollBuild();

      const notSummonersRift = maps.find(gameMap => gameMap.mapId !== GameMap.summonersRiftId);
      component.gameMap = notSummonersRift;
      component.build = build;

      const newBuildDe = fixture.debugElement.query(By.css('#new-build-btn'));
      newBuildDe.triggerEventHandler('click', trollBuild);

      fixture.detectChanges();

      expect(championsService.getTrollBuild).toHaveBeenCalledWith('Skarner', notSummonersRift.mapId);
      expect(component.build).toBeNull();
    }));

    it('should save a Troll Build with after clicking the save build button and then show its self link',
      inject([BuildsService], (buildsService: BuildsService) => {
      fixture.detectChanges();

      const savedBuild = new Build();
      savedBuild.championId = build.championId;
      savedBuild.item1Id = build.item1Id;
      savedBuild.item2Id = build.item2Id;
      savedBuild.item3Id = build.item3Id;
      savedBuild.item4Id = build.item4Id;
      savedBuild.item5Id = build.item5Id;
      savedBuild.item6Id = build.item6Id;
      savedBuild.summonerSpell1Id = build.summonerSpell1Id;
      savedBuild.summonerSpell2Id = build.summonerSpell2Id;
      savedBuild.trinketId = build.trinketId;
      savedBuild.mapId = build.mapId;
      const selfRef = 'http://localhost/api/build/1';

      spyOn(buildsService, 'saveBuild').and.returnValue(of(new HttpResponse<Build>({
        body: savedBuild,
        headers: new HttpHeaders({
          'Location': selfRef
        }),
        status: 201
      })));

      const newBuildBtnDe = fixture.debugElement.query(By.css('#save-build-btn'));
      newBuildBtnDe.triggerEventHandler('click', null);

      fixture.detectChanges();

      expect(buildsService.saveBuild).toHaveBeenCalledWith(build);

      const savedBuildDe = fixture.debugElement.query(By.css('#saved-build-link'));
      expect(savedBuildDe.nativeElement.textContent).toBe('http://localhost/build/1');
      expect(newBuildBtnDe.nativeElement.disabled).toBeTruthy('Expected save build button to be disabled');
    }));

    function expectChampionAndMapsAndTrollBuild() {
      // Champion
      const championsNameAndTitleDe = fixture.debugElement.query(By.css('#champion-name-and-title'));
      expect(championsNameAndTitleDe.nativeElement.textContent).toBe(`${skarner.name} - ${skarner.title}`);

      const championImgDe = fixture.debugElement.query(By.css('#champion-img'));
      expect(championImgDe.nativeElement.src).toContain(`/api/img/champions/${skarner.id}`);

      const championPartypeDe = fixture.debugElement.query(By.css('#champion-partype'));
      expect(championPartypeDe.nativeElement.textContent).toBe(`(${skarner.partype})`);

      const championTagsDe = fixture.debugElement.queryAll(By.css('.champion-tag'));
      expect(championTagsDe.map(championTagDe => championTagDe.nativeElement.textContent)).toEqual(skarner.tags);

      const championPassiveImgDe = fixture.debugElement.query(By.css('#champion-passive-img'));
      expect(championPassiveImgDe.nativeElement.src).toContain(`/api/img/champions/${skarner.id}/passive`);

      const championsPassiveDe = fixture.debugElement.query(By.css('#champion-passive-name'));
      expect(championsPassiveDe.nativeElement.textContent).toBe(skarner.passive.name);

      const championsPassiveDescDe = fixture.debugElement.query(By.css('#champion-passive-description'));
      expect(championsPassiveDescDe.nativeElement.textContent).toBe(skarner.passive.description);

      const championSpellsDe = fixture.debugElement.queryAll(By.css('.champion-spell'));
      expect(championSpellsDe.length).toBe(skarner.spells.length);
      for (let i = 0; i < championSpellsDe.length; i++) {
        const championSpellImgDe = championSpellsDe[i].query(By.css('.champion-spell-img'));
        expect(championSpellImgDe.nativeElement.src).toContain(`/api/img/champions/${skarner.id}/spell/${skarner.spells[i].key}`);
        const championSpellNameDe = championSpellsDe[i].query(By.css('.champion-spell-name'));
        expect(championSpellNameDe.nativeElement.textContent).toBe(skarner.spells[i].name);
        const championSpellDescDe = championSpellsDe[i].query(By.css('.champion-spell-description'));
        expect(championSpellDescDe.nativeElement.textContent).toBe(skarner.spells[i].description);
      }

      // Maps
      const mapsHeaderDe = fixture.debugElement.query(By.css('#troll-build-maps-header'));
      expect(mapsHeaderDe.nativeElement.textContent).toBe('Map');

      const mapsOptionDe = fixture.debugElement.queryAll(By.css('.map-option'));
      expect(mapsOptionDe.map(mapOptionDe => mapOptionDe.nativeElement.textContent.trim()))
        .toEqual(maps.map(map => map.mapName));
      const newBuildBtnDe = fixture.debugElement.query(By.css('#new-build-btn'));
      expect(newBuildBtnDe.nativeElement.textContent).toBe('New Build');

      // Troll Build
      const trollBuildItemsHeaderDe = fixture.debugElement.query(By.css('#troll-build-items-header'));
      expect(trollBuildItemsHeaderDe.nativeElement.textContent).toBe('Items');
      const trollBuildItemsDe = fixture.debugElement.queryAll(By.css('.troll-build-item'));
      for (let i = 0; i < trollBuildItemsDe.length; i++) {
        const trollBuildItemImgDe = trollBuildItemsDe[i].query(By.css('.troll-build-item-img'));
        expect(trollBuildItemImgDe.nativeElement.src).toContain(`/api/img/items/${trollBuild.items[i].id}`);
        expect(trollBuildItemsDe[i].nativeElement.textContent.trim()).toBe(trollBuild.items[i].name);
      }

      const trollBuildSummonerSpellsHeaderDe = fixture.debugElement.query(By.css('#troll-build-summoner-spells-header'));
      expect(trollBuildSummonerSpellsHeaderDe.nativeElement.textContent).toBe('Summoner Spells');
      const trollBuildSummonerSpellsDe = fixture.debugElement.queryAll(By.css('.troll-build-summoner-spell'));
      for (let i = 0; i < trollBuildSummonerSpellsDe.length; i++) {
        const trollBuildSummonerSpellsImgDe = trollBuildSummonerSpellsDe[i].query(By.css('.troll-build-summoner-spells-img'));
        expect(trollBuildSummonerSpellsImgDe.nativeElement.src).toContain(`/api/img/summoner-spells/${trollBuild.summonerSpells[i].id}`);
        expect(trollBuildSummonerSpellsDe[i].nativeElement.textContent.trim()).toBe(trollBuild.summonerSpells[i].name);
      }

      const trollBuildTrinketHeaderDe = fixture.debugElement.query(By.css('#troll-build-trinket-header'));
      expect(trollBuildTrinketHeaderDe.nativeElement.textContent).toBe('Trinket');
      const trollBuildTrinketDe = fixture.debugElement.query(By.css('.troll-build-trinket'));
      const trollBuildTrinketImgDe = fixture.debugElement.query(By.css('.troll-build-trinket-img'));
      expect(trollBuildTrinketImgDe.nativeElement.src).toContain(`/api/img/items/${trollBuild.trinket.id}`);
      expect(trollBuildTrinketDe.nativeElement.textContent.trim()).toBe(trollBuild.trinket.name);

      // Save Troll Build
      const saveBuildBtnDe = fixture.debugElement.query(By.css('#save-build-btn'));
      expect(saveBuildBtnDe.nativeElement.textContent).toBe('Save Build');
      expect(newBuildBtnDe.nativeElement.disabled).toBeFalsy('Expected save build button to be enabled');
    }

  });

  describe('show Champion by name with a loading Troll Build and maps to select', () => {

    beforeEach(inject([ChampionService, GameMapsService], (championsService: ChampionService, gameMapsService: GameMapsService) => {
      spyOn(championsService, 'getChampion').and.returnValue(of(skarner));
      spyOn(championsService, 'getTrollBuild').and.callThrough();
      spyOn(gameMapsService, 'forTrollBuild').and.returnValue(of(maps));
    }));

    afterEach(inject([ChampionService, GameMapsService], (championsService: ChampionService, gameMapsService: GameMapsService) => {
      expect(championsService.getChampion).not.toHaveBeenCalledWith('Skarner');
      expect(championsService.getTrollBuild).toHaveBeenCalledWith('Skarner', GameMap.summonersRiftId);
      expect(gameMapsService.forTrollBuild).toHaveBeenCalled();
    }));

    it('should show placeholders indicating a loading Troll Build ', () => {
      fixture.detectChanges();

      const newBuildBtnLoadingDe = fixture.debugElement.query(By.css('#new-build-btn-loading'));
      expect(newBuildBtnLoadingDe).toBeTruthy();

      const trollBuildLoadingDe = fixture.debugElement.query(By.css('#troll-build-loading'));
      expect(trollBuildLoadingDe).toBeTruthy();

      const trollBuildLoadingItemsHeaderDe = fixture.debugElement.query(By.css('#troll-build-loading-items-header'));
      expect(trollBuildLoadingItemsHeaderDe.nativeElement.textContent).toBe('Items');
      const trollBuildLoadingItemsDe = fixture.debugElement.queryAll(By.css('.troll-build-loading-item'));
      expect(trollBuildLoadingItemsDe.length).toBe(6);

      const trollBuildLoadingSummonerSpellsHeaderDe = fixture.debugElement.query(By.css('#troll-build-loading-summoner-spells-header'));
      expect(trollBuildLoadingSummonerSpellsHeaderDe.nativeElement.textContent).toBe('Summoner Spells');
      const trollBuildLoadingSummonerSpellsDe = fixture.debugElement.queryAll(By.css('.troll-build-loading-summoner-spell'));
      expect(trollBuildLoadingSummonerSpellsDe.length).toBe(2);

      const trollBuildLoadingTrinketHeaderDe = fixture.debugElement.query(By.css('#troll-build-loading-trinket-header'));
      expect(trollBuildLoadingTrinketHeaderDe.nativeElement.textContent).toBe('Trinket');
      const trollBuildLoadingTrinketDe = fixture.debugElement.query(By.css('.troll-build-loading-trinket'));
      expect(trollBuildLoadingTrinketDe).toBeTruthy();

      const saveBuildBtnDe = fixture.debugElement.query(By.css('#save-build-btn'));
      expect(saveBuildBtnDe).toBeFalsy();
    });

  });

});
