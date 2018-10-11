import { async, ComponentFixture, inject, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { ActivatedRoute, Router, RouterLinkWithHref } from '@angular/router';
import { By } from '@angular/platform-browser';

import { of } from 'rxjs';

import { BuildsPage } from './builds.page';
import { BuildsModule } from './builds.module';
import { TitleService } from '@service/title.service';
import { BuildsService } from './builds.service';
import { Build } from '@model/build';
import { Item } from '@model/item';
import { SummonerSpell } from '@model/summoner-spell';

describe('BuildsPage', () => {
  let component: BuildsPage;
  let fixture: ComponentFixture<BuildsPage>;

  const build: Build = {
    id: 3,
    championId: 16,
    item1Id: 3111,
    item2Id: 3748,
    item3Id: 3194,
    item4Id: 3135,
    item5Id: 3115,
    item6Id: 3401,
    summonerSpell1Id: 14,
    summonerSpell2Id: 11,
    trinketId: 3363,
    mapId: 11,
    champion: {
      id: 16,
      key: 'Soraka',
      name: 'Soraka',
      title: 'the Starchild',
      partype: 'Mana',
      info: {
        attack: 2,
        defense: 5,
        magic: 7,
        difficulty: 3
      },
      spells: [
        {
          key: 'SorakaQ',
          name: 'Starcall',
          description: 'A star falls from the sky at the target location dealing magic damage and slowing enemies. ' +
          'If an enemy champion is hit by Starcall, Soraka recovers health and gains movement speed when moving away ' +
          'from enemy champions.',
          tooltip: 'Calls down a star from Soraka to a target location. Enemies standing in the explosion radius take ' +
          '{{ e1 }} <span class="color99FF99">(+{{ a1 }})</span> magic damage and are slowed by {{ e2 }}% for 2 ' +
          'seconds.<br /><br />If Starcall hits a champion Soraka gains <span class="colorDDDDDD">Rejuvenation' +
          '</span> for {{ e5 }} seconds, which restores {{ e3 }} <span class="color99FF99">(+{{ f1 }})</span> ' +
          'health per second and grants {{ e9 }}% movement speed when not moving toward enemy champions.'
        },
        {
          key: 'SorakaW',
          name: 'Astral Infusion',
          description: 'Soraka sacrifices a portion of her own health to heal another friendly champion.',
          tooltip: 'Restores {{ e1 }} <span class="color99FF99">(+{{ a1 }})</span> health to another champion ally.' +
          '<br /><br />If cast while affected by <span class="colorDDDDDD">Rejuvenation</span>, Soraka will grant ' +
          'her target its benefits for {{ f1 }} seconds.<br /><br /><span class="color919191"><i>Cannot be cast if ' +
          'Soraka is below 5% Health.</i></span>'
        },
        {
          key: 'SorakaE',
          name: 'Equinox',
          description: 'Creates a zone at a location that silences all enemies inside. When the zone expires, all ' +
          'enemies still inside are rooted.',
          tooltip: 'Creates a zone at target location for 1.5 seconds, dealing {{ e2 }} <span class="color99FF99">' +
          '(+{{ a1 }})</span> magic damage to enemy Champions in the cast radius. Enemy Champions standing in the ' +
          'zone are silenced until they leave. <br /><br />When the zone disappears, all enemy Champions still ' +
          'standing in the zone are rooted for {{ e1 }} second(s) and are dealt {{ e2 }} <span class="color99FF99">' +
          '(+{{ a1 }}) </span>magic damage.'
        },
        {
          key: 'SorakaR',
          name: 'Wish',
          description: 'Soraka fills her allies with hope, instantly restoring health to herself and all friendly ' +
          'champions.',
          tooltip: 'Calls upon divine powers to restore {{ e1 }} <span class="color99FF99">(+{{ a1 }})</span> Health ' +
          'to all allied Champions. Wish\'s power is increased by 50% on each Champion below 40% Health.'
        }
      ],
      passive: {
        name: 'Salvation',
        description: 'Soraka runs faster towards nearby low health allies.'
      },
      tags: [
        'Mage',
        'Support'
      ]
    },
    item1: {
      id: 3111,
      name: 'Mercury\'s Treads',
      group: null,
      consumed: null,
      description: '<groupLimit>Limited to 1 pair of boots.</groupLimit><br><br><stats>+25 Magic Resist</stats><br>' +
      '<br><unique>UNIQUE Passive - Enhanced Movement:</unique> +45 Movement Speed<br><unique>UNIQUE Passive - ' +
      'Tenacity:</unique> Reduces the duration of stuns, slows, taunts, fears, silences, blinds, polymorphs, and ' +
      'immobilizes by 30%.',
      from: [
        1001,
        1033
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
        base: 350,
        total: 1100,
        sell: 770,
        purchasable: true
      }
    },
    item2: {
      id: 3748,
      name: 'Titanic Hydra',
      group: null,
      consumed: null,
      description: '<stats>+450 Health<br>+40 Attack Damage<br>+100% Base Health Regen </stats><br><br><unique>' +
      'UNIQUE Passive - Cleave:</unique> Basic attacks deal 5 + 1% of your maximum health as bonus physical damage ' +
      'to your target and 40 + 2.5% of your maximum health as physical damage  to other enemies in a cone on hit.' +
      '<br><active>UNIQUE Active - Crescent:</active> Cleave damage to all targets is increased to 40 + 10% of ' +
      'your maximum health as bonus physical damage  in a larger cone for your next basic attack (20 second cooldown).' +
      '<br><br><rules>(Unique Passives with the same name don\'t stack.)</rules>',
      from: [
        1028,
        3052,
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
        base: 700,
        total: 3500,
        sell: 2450,
        purchasable: true
      }
    },
    item3: {
      id: 3194,
      name: 'Adaptive Helm',
      group: null,
      consumed: null,
      description: '<stats>+350 Health<br>+55 Magic Resist<br>+100% Base Health Regeneration <br>+10% Cooldown ' +
      'Reduction</stats><br><br><unique>UNIQUE Passive:</unique> Taking magic damage from a spell or effect reduces ' +
      'all subsequent magic damage from that same spell or effect by 20% for 4 seconds.',
      from: [
        1006,
        1033,
        3211
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
        total: 2800,
        sell: 1960,
        purchasable: true
      }
    },
    item4: {
      id: 3135,
      name: 'Void Staff',
      group: null,
      consumed: null,
      description: '<stats>+70 Ability Power</stats><br><br><unique>UNIQUE Passive:</unique> +40% <a href=\'TotalMagicPen\'>' +
      'Magic Penetration</a>.',
      from: [
        1026,
        1052
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
        base: 1365,
        total: 2650,
        sell: 1855,
        purchasable: true
      }
    },
    item5: {
      id: 3115,
      name: 'Nashor\'s Tooth',
      group: null,
      consumed: null,
      description: '<stats>+50% Attack Speed<br>+80 Ability Power</stats><br><br><unique>UNIQUE Passive:</unique>' +
      ' +20% Cooldown Reduction<br><unique>UNIQUE Passive:</unique> Basic attacks deal 15 (+15% of Ability Power) ' +
      'bonus magic damage on hit.<br>',
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
    item6: {
      id: 3401,
      name: 'Remnant of the Aspect',
      group: null,
      consumed: null,
      description: '<stats>+350 Health<br>+200% Base Health Regen <br>+10% Cooldown Reduction<br>+1 Gold per 10 ' +
      'seconds </stats><br><br><unique>UNIQUE Passive - Spoils of War:</unique> Melee basic attacks execute minions ' +
      'below 320 (+20 per level) Health. Killing a minion heals the owner and the nearest allied champion for 50 ' +
      'Health (+2% missing health) and grants them kill Gold. 50% healing if the owner is ranged. These effects ' +
      'require a nearby ally. Recharges every 20 seconds. Max 4 charges.<hr><passive>QUEST:</passive> Earn 500 ' +
      'gold using this item.<br><passive>REWARD:</passive> Gain <active>UNIQUE Active - Warding:</active> Consumes ' +
      'a charge to place a <font color=\'#BBFFFF\'>Stealth Ward</font> that reveals the surrounding area for 150 ' +
      'seconds.  Holds up to 4 charges which refill upon visiting the shop.<br><br><groupLimit>Limited to 1 Gold ' +
      'Income or Jungle item.</groupLimit>',
      from: [
        1028,
        3097
      ],
      into: [],
      requiredChampion: null,
      requiredAlly: null,
      maps: {
        '10': false,
        '11': true,
        '12': false
      },
      gold: {
        base: 550,
        total: 1800,
        sell: 720,
        purchasable: true
      }
    },
    summonerSpell1: {
      id: 14,
      name: 'Ignite',
      description: 'Ignites target enemy champion, dealing 80-505 true damage (depending on champion level) over 5 ' +
      'seconds, grants you vision of the target, and reduces healing effects on them for the duration.',
      cooldown: [
        210
      ],
      key: 'SummonerDot',
      modes: [
        'ARAM',
        'CLASSIC',
        'TUTORIAL'
      ]
    },
    summonerSpell2: {
      id: 11,
      name: 'Smite',
      description: 'Deals 390-1000 true damage (depending on champion level) to target epic, large, or medium ' +
      'monster or enemy minion. Restores Health based on your maximum life when used against monsters.',
      cooldown: [
        15
      ],
      key: 'SummonerSmite',
      modes: [
        'CLASSIC',
        'TUTORIAL'
      ]
    },
    trinket: {
      id: 3363,
      name: 'Farsight Alteration',
      group: null,
      consumed: null,
      description: '<levelLimit>Level 9+ required to upgrade.</levelLimit><br><groupLimit>Limited to 1 Trinket.' +
      '</groupLimit><br><br>Alters the <font color=\'#FFFFFF\'>Warding Totem</font> Trinket:<br><br><stats><font ' +
      'color=\'#00FF00\'>+</font> Massively increased cast range (+650%)<br><font color=\'#00FF00\'>+</font> ' +
      'Infinite duration and does not count towards ward limit<br><font color=\'#FF0000\'>-</font> <font color=\'#FF6600\'>' +
      '10% increased cooldown</font><br><font color=\'#FF0000\'>-</font> <font color=\'#FF6600\'>Ward is visible, ' +
      'fragile, untargetable by allies</font><br><font color=\'#FF0000\'>-</font> <font color=\'#FF6600\'>45% ' +
      'reduced ward vision radius</font><br><font color=\'#FF0000\'>-</font> <font color=\'#FF6600\'>Cannot store ' +
      'charges</font></stats>',
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
    },
    map: {
      mapId: 11,
      mapName: 'Summoner\'s Rift'
    }
  };

  describe('with valid Build', () => {
    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [BuildsModule, HttpClientTestingModule, RouterTestingModule],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: {
              data: of({ build: { 'id': build.id, 'savedBuild': build } })
            }
          }
        ]
      })
      .compileComponents();
    }));

    beforeEach(inject([BuildsService, TitleService], (buildsService: BuildsService, title: TitleService) => {
      fixture = TestBed.createComponent(BuildsPage);
      component = fixture.componentInstance;

      spyOn(buildsService, 'getBuild').and.callThrough();
      spyOn(buildsService, 'countBuilds').and.callThrough();

      spyOn(title, 'setTitle').and.callThrough();

      fixture.detectChanges();
    }));

    afterEach(inject([BuildsService], (buildsService: BuildsService) => {
      expect(buildsService.countBuilds).not.toHaveBeenCalled();
      expect(buildsService.getBuild).not.toHaveBeenCalledWith(build.id);
    }));

    it('should show a Troll Build with no invalid attributes',
      inject([BuildsService, TitleService], (buildsService: BuildsService, title: TitleService) => {
      expectChampionAndMapsAndSavedTrollBuild();
      expect(title.setTitle).toHaveBeenCalledWith(`${build.champion.name} Build | ${build.id}`);
    }));

    it('should redirect to individual Champion page after clicking the new build button',
      inject([BuildsService, TitleService, Router], (buildsService: BuildsService, title: TitleService, router: Router) => {
      expectChampionAndMapsAndSavedTrollBuild();
      expect(title.setTitle).toHaveBeenCalledWith(`${build.champion.name} Build | ${build.id}`);

      spyOn(router, 'navigate');

      const newBuildDe = fixture.debugElement.query(By.css('#new-build-btn'));
      newBuildDe.triggerEventHandler('click', null);

      fixture.detectChanges();

      expect(router.navigate).toHaveBeenCalledWith(['/champions', build.championId]);
    }));
  });

  describe('with not found 404 Build', () => {
    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [BuildsModule, HttpClientTestingModule, RouterTestingModule],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: {
              data: of({ build: { 'id': 1, 'savedBuild': null } })
            }
          }
        ]
      })
      .compileComponents();
    }));

    beforeEach(inject([BuildsService, TitleService], (buildsService: BuildsService, title: TitleService) => {
      fixture = TestBed.createComponent(BuildsPage);
      component = fixture.componentInstance;

      spyOn(buildsService, 'countBuilds').and.callThrough();

      spyOn(title, 'setTitle').and.callThrough();

      fixture.detectChanges();
    }));

    it('should show no Build found alert',
      inject([BuildsService, TitleService], (buildsService: BuildsService, title: TitleService) => {
      const buildNotFoundAlertDe = fixture.debugElement.query(By.css('#build-not-found-alert'));
      expect(buildNotFoundAlertDe.nativeElement.textContent).toBe('Couldn\'t find a saved Troll Build with ID: 1');

      const randomBuildLinkDe = fixture.debugElement.query(By.css('.try-random-build'));
      expect(randomBuildLinkDe.nativeElement.textContent.trim()).toBe('Try a random Troll Build');
      const returnHomeRouterLink = randomBuildLinkDe.injector.get(RouterLinkWithHref);
      expect(returnHomeRouterLink.href).toBe('/builds');

      expect(title.setTitle).toHaveBeenCalledWith(`Couldn't find Troll Build 1`);
    }));
  });

  describe('with Build having invalid Items', () => {
    const invalidItemsBuild = Object.assign({}, build);
    invalidItemsBuild.item6 = null;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [BuildsModule, HttpClientTestingModule, RouterTestingModule],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: {
              data: of({ build: { 'id': 1, 'savedBuild': invalidItemsBuild } })
            }
          }
        ]
      })
      .compileComponents();
    }));

    beforeEach(inject([BuildsService, TitleService], (buildsService: BuildsService, title: TitleService) => {
      fixture = TestBed.createComponent(BuildsPage);
      component = fixture.componentInstance;

      spyOn(buildsService, 'countBuilds').and.callThrough();

      spyOn(title, 'setTitle').and.callThrough();

      fixture.detectChanges();
    }));

    it('should show invalid Items alert',
      inject([BuildsService, TitleService], (buildsService: BuildsService, title: TitleService) => {
      const buildInvalidAlertDe = fixture.debugElement.query(By.css('#build-invalid-items-alert'));
      expect(buildInvalidAlertDe.nativeElement.textContent).toBe('This Troll Build with ID: 1 has become invalid due to outdated Items');

      const randomBuildLinkDe = fixture.debugElement.query(By.css('.try-random-build'));
      expect(randomBuildLinkDe.nativeElement.textContent.trim()).toBe('Try a random Troll Build');
      const returnHomeRouterLink = randomBuildLinkDe.injector.get(RouterLinkWithHref);
      expect(returnHomeRouterLink.href).toBe('/builds');

      expect(title.setTitle).toHaveBeenCalledWith(`Invalid attributes in Troll Build 1`);
    }));
  });

  describe('with Build having invalid Summoner Spells', () => {
    const invalidSummonerSpellsBuild = Object.assign({}, build);
    invalidSummonerSpellsBuild.summonerSpell2 = null;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [BuildsModule, HttpClientTestingModule, RouterTestingModule],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: {
              data: of({ build: { 'id': 1, 'savedBuild': invalidSummonerSpellsBuild } })
            }
          }
        ]
      })
      .compileComponents();
    }));

    beforeEach(inject([BuildsService, TitleService], (buildsService: BuildsService, title: TitleService) => {
      fixture = TestBed.createComponent(BuildsPage);
      component = fixture.componentInstance;

      spyOn(buildsService, 'countBuilds').and.callThrough();

      spyOn(title, 'setTitle').and.callThrough();

      fixture.detectChanges();
    }));

    it('should show invalid Summoner Spells alert',
      inject([BuildsService, TitleService], (buildsService: BuildsService, title: TitleService) => {
      const buildInvalidAlertDe = fixture.debugElement.query(By.css('#build-invalid-summoner-spells-alert'));
      expect(buildInvalidAlertDe.nativeElement.textContent).toBe('This Troll Build with ID: 1 has become invalid due to outdated Summoner Spells');

      const randomBuildLinkDe = fixture.debugElement.query(By.css('.try-random-build'));
      expect(randomBuildLinkDe.nativeElement.textContent.trim()).toBe('Try a random Troll Build');
      const returnHomeRouterLink = randomBuildLinkDe.injector.get(RouterLinkWithHref);
      expect(returnHomeRouterLink.href).toBe('/builds');

      expect(title.setTitle).toHaveBeenCalledWith(`Invalid attributes in Troll Build 1`);
    }));
  });

  describe('with Build having an invalid Trinket', () => {
    const invalidTrinketBuild = Object.assign({}, build);
    invalidTrinketBuild.trinket = null;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [BuildsModule, HttpClientTestingModule, RouterTestingModule],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: {
              data: of({ build: { 'id': 1, 'savedBuild': invalidTrinketBuild } })
            }
          }
        ]
      })
      .compileComponents();
    }));

    beforeEach(inject([BuildsService, TitleService], (buildsService: BuildsService, title: TitleService) => {
      fixture = TestBed.createComponent(BuildsPage);
      component = fixture.componentInstance;

      spyOn(buildsService, 'countBuilds').and.callThrough();

      spyOn(title, 'setTitle').and.callThrough();
    }));

    it('should show invalid Trinket alert',
      inject([BuildsService, TitleService], (buildsService: BuildsService, title: TitleService) => {
      fixture.detectChanges();

      const buildInvalidAlertDe = fixture.debugElement.query(By.css('#build-invalid-trinket-alert'));
      expect(buildInvalidAlertDe.nativeElement.textContent).toBe('This Troll Build with ID: 1 has become invalid due to an outdated Trinket');

      const randomBuildLinkDe = fixture.debugElement.query(By.css('.try-random-build'));
      expect(randomBuildLinkDe.nativeElement.textContent.trim()).toBe('Try a random Troll Build');
      const returnHomeRouterLink = randomBuildLinkDe.injector.get(RouterLinkWithHref);
      expect(returnHomeRouterLink.href).toBe('/builds');

      expect(title.setTitle).toHaveBeenCalledWith(`Invalid attributes in Troll Build 1`);
    }));
  });

  function expectChampionAndMapsAndSavedTrollBuild() {
    // Champion
    const championsNameDe = fixture.debugElement.query(By.css('#champion-name'));
    expect(championsNameDe.nativeElement.textContent).toBe(build.champion.name);

    const championsTitleDe = fixture.debugElement.query(By.css('#champion-title'));
    expect(championsTitleDe.nativeElement.textContent).toBe(build.champion.title);

    const championImgDe = fixture.debugElement.query(By.css('#champion-img'));
    expect(championImgDe.nativeElement.src).toContain(`/api/img/champions/${build.champion.id}`);

    const championPartypeDe = fixture.debugElement.query(By.css('#champion-partype'));
    expect(championPartypeDe.nativeElement.textContent).toBe(`${build.champion.partype}`);

    const championTagsDe = fixture.debugElement.queryAll(By.css('.champion-tag'));
    expect(championTagsDe.map(championTagDe => championTagDe.nativeElement.textContent)).toEqual(build.champion.tags);

    // New Build
    const newBuildBtnDe = fixture.debugElement.query(By.css('#new-build-btn'));
    expect(newBuildBtnDe.nativeElement.textContent).toBe('New Build');

    // Maps
    const mapsOptionDe = fixture.debugElement.query(By.css('.map-option'));
    expect(mapsOptionDe.nativeElement.textContent.trim()).toBe(build.map.mapName);

    // Troll Build
    const trollBuildItemsHeaderDe = fixture.debugElement.query(By.css('.troll-build-items-header'));
    expect(trollBuildItemsHeaderDe.nativeElement.textContent).toBe('Items');
    const trollBuildItemsDe = fixture.debugElement.queryAll(By.css('.troll-build-item'));
    for (let i = 0; i < trollBuildItemsDe.length; i++) {
      expect(trollBuildItemsDe[i].attributes['data-tooltip']).toBe(getItem(i).name);
      const trollBuildItemImgDe = trollBuildItemsDe[i].query(By.css(`#troll-build-item${i + 1}-img`));
      expect(trollBuildItemImgDe.nativeElement.src).toContain(`/api/img/items/${getItem(i).id}`);
    }

    const trollBuildSummonerSpellsHeaderDe = fixture.debugElement.query(By.css('.troll-build-summoner-spells-header'));
    expect(trollBuildSummonerSpellsHeaderDe.nativeElement.textContent).toBe('Summoner Spells');
    const trollBuildSummonerSpellsDe = fixture.debugElement.queryAll(By.css('.troll-build-summoner-spell'));
    for (let i = 0; i < trollBuildSummonerSpellsDe.length; i++) {
      expect(trollBuildSummonerSpellsDe[i].attributes['data-tooltip']).toBe(getSummonerSpell(i).name);
      const trollBuildSummonerSpellsImgDe = trollBuildSummonerSpellsDe[i].query(By.css(`#troll-build-summoner-spell${i + 1}-img`));
      expect(trollBuildSummonerSpellsImgDe.nativeElement.src).toContain(`/api/img/summoner-spells/${getSummonerSpell(i).id}`);
    }

    const trollBuildTrinketHeaderDe = fixture.debugElement.query(By.css('.troll-build-trinket-header'));
    expect(trollBuildTrinketHeaderDe.nativeElement.textContent).toBe('Trinket');
    const trollBuildTrinketDe = fixture.debugElement.query(By.css('.troll-build-trinket'));
    expect(trollBuildTrinketDe).toBeTruthy();
    expect(trollBuildTrinketDe.attributes['data-tooltip']).toBe(build.trinket.name);
    const trollBuildTrinketImgDe = fixture.debugElement.query(By.css('#troll-build-trinket-img'));
    expect(trollBuildTrinketImgDe.nativeElement.src).toContain(`/api/img/items/${build.trinket.id}`);

    function getItem(index: number): Item {
      switch (index) {
        case 0:
          return build.item1;
        case 1:
          return build.item2;
        case 2:
          return build.item3;
        case 3:
          return build.item4;
        case 4:
          return build.item5;
        case 5:
          return build.item6;
      }
    }

    function getSummonerSpell(index: number): SummonerSpell {
      switch (index) {
        case 0:
          return build.summonerSpell1;
        case 1:
          return build.summonerSpell2;
      }
    }
  }

});
