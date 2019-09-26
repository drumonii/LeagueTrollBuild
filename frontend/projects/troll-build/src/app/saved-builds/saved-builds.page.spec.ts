import { async, ComponentFixture, inject, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { ActivatedRoute, Router, RouterLinkWithHref } from '@angular/router';
import { By } from '@angular/platform-browser';

import { NbSelectComponent, NbThemeModule } from '@nebular/theme';

import { of } from 'rxjs';

import { SavedBuildsPage } from './saved-builds.page';
import { SavedBuildsModule } from './saved-builds.module';
import { SavedBuildsService } from './saved-builds.service';
import { TitleService } from '@ltb-service/title.service';
import { Build } from '@ltb-model/build';
import { Item } from '@ltb-model/item';
import { SummonerSpell } from '@ltb-model/summoner-spell';

describe('SavedBuildsPage', () => {
  let component: SavedBuildsPage;
  let fixture: ComponentFixture<SavedBuildsPage>;

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
        10: true,
        11: true,
        12: true
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
        10: true,
        11: true,
        12: true
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
        10: true,
        11: true,
        12: true
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
        10: true,
        11: true,
        12: true
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
        10: true,
        11: true,
        12: true
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
        10: false,
        11: true,
        12: false
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
        10: false,
        11: true,
        12: true
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
        imports: [HttpClientTestingModule, RouterTestingModule, NbThemeModule.forRoot(), SavedBuildsModule],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: {
              data: of({ build: { id: build.id, savedBuild: build } })
            }
          }
        ]
      })
      .compileComponents();
    }));

    beforeEach(inject([SavedBuildsService, TitleService], (buildsService: SavedBuildsService, title: TitleService) => {
      fixture = TestBed.createComponent(SavedBuildsPage);
      component = fixture.componentInstance;

      spyOn(buildsService, 'getBuild').and.callThrough();
      spyOn(buildsService, 'countBuilds').and.callThrough();

      spyOn(title, 'setTitle').and.callThrough();

      fixture.detectChanges();
    }));

    afterEach(inject([SavedBuildsService], (buildsService: SavedBuildsService) => {
      expect(buildsService.countBuilds).not.toHaveBeenCalled();
      expect(buildsService.getBuild).not.toHaveBeenCalledWith(build.id);
    }));

    it('should show a Troll Build with no invalid attributes',
      inject([SavedBuildsService, TitleService], (buildsService: SavedBuildsService, title: TitleService) => {
      expectChampionAndMapsAndSavedTrollBuild();
      expect(title.setTitle).toHaveBeenCalledWith(`${build.champion.name} Build | ${build.id}`);
    }));

    it('should redirect to individual Champion page after clicking the new build button',
      inject([SavedBuildsService, TitleService, Router], (buildsService: SavedBuildsService, title: TitleService, router: Router) => {
      expectChampionAndMapsAndSavedTrollBuild();
      expect(title.setTitle).toHaveBeenCalledWith(`${build.champion.name} Build | ${build.id}`);

      spyOn(router, 'navigate');

      const newBuild = fixture.debugElement.query(By.css('#new-build-btn'));
      newBuild.triggerEventHandler('click', null);

      fixture.detectChanges();

      expect(router.navigate).toHaveBeenCalledWith(['/champions', build.championId]);
    }));
  });

  describe('with not found 404 Build', () => {
    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [SavedBuildsModule, HttpClientTestingModule, RouterTestingModule],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: {
              data: of({ build: { id: 1, savedBuild: null } })
            }
          }
        ]
      })
      .compileComponents();
    }));

    beforeEach(inject([SavedBuildsService, TitleService], (buildsService: SavedBuildsService, title: TitleService) => {
      fixture = TestBed.createComponent(SavedBuildsPage);
      component = fixture.componentInstance;

      spyOn(buildsService, 'countBuilds').and.callThrough();

      spyOn(title, 'setTitle').and.callThrough();

      fixture.detectChanges();
    }));

    it('should show no Build found alert',
      inject([SavedBuildsService, TitleService], (buildsService: SavedBuildsService, title: TitleService) => {
      const buildNotFoundAlert = fixture.debugElement.query(By.css('[data-e2e="build-not-found-alert"]'));
      expect(buildNotFoundAlert.nativeElement.textContent).toBe('Couldn\'t find a saved Troll Build with ID: 1');

      const randomBuildLink = fixture.debugElement.query(By.css('[data-e2e="try-random-build"]'));
      expect(randomBuildLink.nativeElement.textContent.trim()).toBe('Try a random Troll Build');
      const returnHomeRouterLink = randomBuildLink.injector.get(RouterLinkWithHref);
      expect(returnHomeRouterLink.href).toBe('/builds');

      expect(title.setTitle).toHaveBeenCalledWith(`Couldn't find Troll Build 1`);
    }));
  });

  describe('with Build having invalid Items', () => {
    const invalidItemsBuild = Object.assign({}, build);
    invalidItemsBuild.item6 = null;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [SavedBuildsModule, HttpClientTestingModule, RouterTestingModule],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: {
              data: of({ build: { id: 1, savedBuild: invalidItemsBuild } })
            }
          }
        ]
      })
      .compileComponents();
    }));

    beforeEach(inject([SavedBuildsService, TitleService], (buildsService: SavedBuildsService, title: TitleService) => {
      fixture = TestBed.createComponent(SavedBuildsPage);
      component = fixture.componentInstance;

      spyOn(buildsService, 'countBuilds').and.callThrough();

      spyOn(title, 'setTitle').and.callThrough();

      fixture.detectChanges();
    }));

    it('should show invalid Items alert',
      inject([SavedBuildsService, TitleService], (buildsService: SavedBuildsService, title: TitleService) => {
      const buildInvalidAlert = fixture.debugElement.query(By.css('[data-e2e="build-invalid-items-alert"]'));
      expect(buildInvalidAlert.nativeElement.textContent).toBe('This Troll Build with ID: 1 has become invalid due to outdated Items');

      const randomBuildLink = fixture.debugElement.query(By.css('[data-e2e="try-random-build"]'));
      expect(randomBuildLink.nativeElement.textContent.trim()).toBe('Try a random Troll Build');
      const returnHomeRouterLink = randomBuildLink.injector.get(RouterLinkWithHref);
      expect(returnHomeRouterLink.href).toBe('/builds');

      expect(title.setTitle).toHaveBeenCalledWith(`Invalid attributes in Troll Build 1`);
    }));
  });

  describe('with Build having invalid Summoner Spells', () => {
    const invalidSummonerSpellsBuild = Object.assign({}, build);
    invalidSummonerSpellsBuild.summonerSpell2 = null;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [SavedBuildsModule, HttpClientTestingModule, RouterTestingModule],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: {
              data: of({ build: { id: 1, savedBuild: invalidSummonerSpellsBuild } })
            }
          }
        ]
      })
      .compileComponents();
    }));

    beforeEach(inject([SavedBuildsService, TitleService], (buildsService: SavedBuildsService, title: TitleService) => {
      fixture = TestBed.createComponent(SavedBuildsPage);
      component = fixture.componentInstance;

      spyOn(buildsService, 'countBuilds').and.callThrough();

      spyOn(title, 'setTitle').and.callThrough();

      fixture.detectChanges();
    }));

    it('should show invalid Summoner Spells alert',
      inject([SavedBuildsService, TitleService], (buildsService: SavedBuildsService, title: TitleService) => {
      const buildInvalidAlert = fixture.debugElement.query(By.css('[data-e2e="build-invalid-summoner-spells-alert"]'));
      expect(buildInvalidAlert.nativeElement.textContent)
        .toBe('This Troll Build with ID: 1 has become invalid due to outdated Summoner Spells');

      const randomBuildLink = fixture.debugElement.query(By.css('[data-e2e="try-random-build"]'));
      expect(randomBuildLink.nativeElement.textContent.trim()).toBe('Try a random Troll Build');
      const returnHomeRouterLink = randomBuildLink.injector.get(RouterLinkWithHref);
      expect(returnHomeRouterLink.href).toBe('/builds');

      expect(title.setTitle).toHaveBeenCalledWith(`Invalid attributes in Troll Build 1`);
    }));
  });

  describe('with Build having an invalid Trinket', () => {
    const invalidTrinketBuild = Object.assign({}, build);
    invalidTrinketBuild.trinket = null;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [SavedBuildsModule, HttpClientTestingModule, RouterTestingModule],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: {
              data: of({ build: { id: 1, savedBuild: invalidTrinketBuild } })
            }
          }
        ]
      })
      .compileComponents();
    }));

    beforeEach(inject([SavedBuildsService, TitleService], (buildsService: SavedBuildsService, title: TitleService) => {
      fixture = TestBed.createComponent(SavedBuildsPage);
      component = fixture.componentInstance;

      spyOn(buildsService, 'countBuilds').and.callThrough();

      spyOn(title, 'setTitle').and.callThrough();
    }));

    it('should show invalid Trinket alert',
      inject([SavedBuildsService, TitleService], (buildsService: SavedBuildsService, title: TitleService) => {
      fixture.detectChanges();

      const buildInvalidAlert = fixture.debugElement.query(By.css('[data-e2e="build-invalid-trinket-alert"]'));
      expect(buildInvalidAlert.nativeElement.textContent).toBe('This Troll Build with ID: 1 has become invalid due to an outdated Trinket');

      const randomBuildLink = fixture.debugElement.query(By.css('[data-e2e="try-random-build"]'));
      expect(randomBuildLink.nativeElement.textContent.trim()).toBe('Try a random Troll Build');
      const returnHomeRouterLink = randomBuildLink.injector.get(RouterLinkWithHref);
      expect(returnHomeRouterLink.href).toBe('/builds');

      expect(title.setTitle).toHaveBeenCalledWith(`Invalid attributes in Troll Build 1`);
    }));
  });

  function expectChampionAndMapsAndSavedTrollBuild() {
    // Champion
    const championsName = fixture.debugElement.query(By.css('#champion-name'));
    expect(championsName.nativeElement.textContent).toBe(build.champion.name);

    const championsTitle = fixture.debugElement.query(By.css('#champion-title'));
    expect(championsTitle.nativeElement.textContent).toBe(build.champion.title);

    const championImg = fixture.debugElement.query(By.css('#champion-img'));
    expect(championImg.nativeElement.src).toContain(`/api/img/champions/${build.champion.id}`);

    const championPartype = fixture.debugElement.query(By.css('[data-e2e="champion-partype"]'));
    expect(championPartype.nativeElement.textContent).toBe(`${build.champion.partype}`);

    const championTags = fixture.debugElement.queryAll(By.css('#champion-tags .ltb-tag'));
    expect(championTags.map(championTag => championTag.nativeElement.textContent)).toEqual(build.champion.tags);

    // New Build
    const newBuildBtn = fixture.debugElement.query(By.css('#new-build-btn'));
    expect(newBuildBtn.nativeElement.textContent.trim()).toBe('New Build');

    // Maps
    expect(fixture.debugElement.query(By.css('[data-e2e="map-select"]'))).toBeTruthy();
    const cdkMapSelect = fixture.debugElement.query(By.directive(NbSelectComponent)).componentInstance;
    expect(cdkMapSelect.options.length).toBe(1);
    expect(cdkMapSelect.options._results[0].content.trim()).toBe(build.map.mapName);

    // Troll Build
    const trollBuildItemsRow = fixture.debugElement.query(By.css('[data-e2e="items-row"]'));
    const trollBuildItemsHeader = trollBuildItemsRow.query(By.css('.troll-build-objects-header'));
    expect(trollBuildItemsHeader.nativeElement.textContent).toBe('Items');
    const trollBuildItems = trollBuildItemsRow.queryAll(By.css('.ltb-list > .ltb-list-item'));
    for (let i = 0; i < trollBuildItems.length; i++) {
      const trollBuildItemImg = trollBuildItems[i].query(By.css('.troll-build-object-img'));
      expect(trollBuildItemImg.nativeElement.src).toContain(`/api/img/items/${getItem(i).id}`);
    }

    const trollBuildSummonerSpellsRow = fixture.debugElement.query(By.css('[data-e2e="summoner-spells-row"]'));
    const trollBuildSummonerSpellsHeader = trollBuildSummonerSpellsRow.query(By.css('.troll-build-objects-header'));
    expect(trollBuildSummonerSpellsHeader.nativeElement.textContent).toBe('Summoner Spells');
    const trollBuildSummonerSpells = trollBuildSummonerSpellsRow.queryAll(By.css('.ltb-list > .ltb-list-item'));
    for (let i = 0; i < trollBuildSummonerSpells.length; i++) {
      const trollBuildSummonerSpellsImg = trollBuildSummonerSpells[i].query(By.css('.troll-build-object-img'));
      expect(trollBuildSummonerSpellsImg.nativeElement.src).toContain(`/api/img/summoner-spells/${getSummonerSpell(i).id}`);
    }

    const trollBuildTrinketRow = fixture.debugElement.query(By.css('[data-e2e="trinket-row"]'));
    const trollBuildTrinketHeader = trollBuildTrinketRow.query(By.css('.troll-build-objects-header'));
    expect(trollBuildTrinketHeader.nativeElement.textContent).toBe('Trinket');
    const trollBuildTrinkets = trollBuildTrinketRow.queryAll(By.css('.ltb-list > .ltb-list-item'));
    expect(trollBuildTrinkets.length).toBe(1);
    const trollBuildTrinketImg = trollBuildTrinkets[0].query(By.css('.troll-build-object-img'));
    expect(trollBuildTrinketImg.nativeElement.src).toContain(`/api/img/items/${build.trinket.id}`);

    function getItem(index: number): Item {
      const items = { 0: build.item1, 1: build.item2, 2: build.item3, 3: build.item4, 4: build.item5, 5: build.item6 };
      return items[index];
    }

    function getSummonerSpell(index: number): SummonerSpell {
      const summonerSpells = { 0: build.summonerSpell1, 1: build.summonerSpell2 };
      return summonerSpells[index];
    }
  }

});
