import { async, fakeAsync, inject, TestBed, tick } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { ActivatedRouteSnapshot, convertToParamMap, Router } from '@angular/router';

import { Observable, of } from 'rxjs';

import { BuildsResolver } from './builds.resolver';
import { BuildsModule } from './builds.module';
import { BuildsService } from '@service/builds.service';
import { BuildsResolverData } from './builds.resolver.data';
import { Build } from '@model/build';

describe('BuildsResolver', () => {
  let resolver: BuildsResolver;

  const build: Build = {
    id: 9,
    championId: 6,
    item1Id: 3020,
    item2Id: 3156,
    item3Id: 3748,
    item4Id: 3004,
    item5Id: 3124,
    item6Id: 3053,
    summonerSpell1Id: 21,
    summonerSpell2Id: 6,
    trinketId: 3364,
    mapId: 11,
    champion: {
      id: 6,
      key: 'Urgot',
      name: 'Urgot',
      title: 'the Dreadnought',
      partype: 'Mana',
      info: {
        attack: 8,
        defense: 5,
        magic: 3,
        difficulty: 8
      },
      spells: [
        {
          key: 'UrgotQ',
          name: 'Corrosive Charge',
          description: 'Fires an explosive charge at the target location, dealing physical damage and slowing enemies ' +
            'caught in the explosion.',
          tooltip: 'Urgot fires an explosive charge, dealing {{ e2 }} <span class="colorFF8C00">(+{{ a1 }})</span> ' +
            'physical damage and slowing enemies hit by {{ e4 }}% for {{ e5 }} second.',
          image: {
            full: 'UrgotQ.png',
            sprite: 'spell12.png',
            group: 'spell',
            imgSrc: [],
            x: 384,
            y: 48,
            w: 48,
            h: 48
          }
        },
        {
          key: 'UrgotW',
          name: 'Purge',
          description: 'Urgot shields himself, slowing himself while he unloads his weapon on nearby enemies. ' +
            'Prioritizes enemy champions Urgot has recently struck with other abilities and triggers Echoing Flames.',
          tooltip: 'For {{ e3 }} seconds, Urgot shoots at a nearby enemy {{ e8 }} times a second, dealing {{ e9 }} ' +
            '<span class="colorFF8C00">(+{{ f3 }})</span> physical damage per shot, <span class="colorFFEB7F">locking ' +
            'onto</span> champions recently damaged by his other abilities. He also shields himself, absorbing {{ e1 }} ' +
            '<span class="colorCC3300">(+{{ f2 }})</span> damage.<br /><br />While firing, Urgot loses {{ e5 }} movement ' +
            'speed but has {{ e2 }}% slow resistance and can walk over minions and non-epic monsters.<br /><br />' +
            '<rules><span class="color8c8c8c">Applies on-hits at 33% damage. Cannot critically strike. Minimum of ' +
            '{{ e0 }} damage to minions.</span></rules>',
          image: {
            full: 'UrgotW.png',
            sprite: 'spell12.png',
            group: 'spell',
            imgSrc: [],
            x: 432,
            y: 48,
            w: 48,
            h: 48
          }
        },
        {
          key: 'UrgotE',
          name: 'Disdain',
          description: 'Urgot charges in a direction, trampling non-champion enemies. If he catches an enemy champion, ' +
            'he will stop and hurl them out of his way.',
          tooltip: 'Urgot charges forwards. If he catches an enemy champion, he will stop and hurl them behind him, ' +
            'dealing {{ e1 }} <span class="colorFF8C00">(+{{ a1 }})</span> physical damage and stunning them for ' +
            '{{ e6 }} seconds.<br /><br />Non-champions take the same damage but are knocked aside.<br /><br /><rules>' +
            '<span class="color8c8c8c">Cannot cross terrain.</span></rules>',
          image: {
            full: 'UrgotE.png',
            sprite: 'spell12.png',
            group: 'spell',
            imgSrc: [],
            x: 0,
            y: 96,
            w: 48,
            h: 48
          }
        },
        {
          key: 'UrgotR',
          name: 'Fear Beyond Death',
          description: 'Urgot fires a chem-drill that impales the first enemy champion hit. If that champion falls ' +
            'below a health threshold, Urgot judges them weak and can execute them.',
          tooltip: 'Urgot fires a chem-drill, impaling the first enemy champion hit. Deals {{ e1 }} <span class="colorFF8C00">' +
            '(+{{ a1 }})</span> physical damage and slows for {{ e4 }} seconds by up to {{ e3 }}% based on the ' +
            'victim\'s missing health.<br /><br />If the victim falls below {{ e2 }}% health while impaled, Urgot may ' +
            'recast this ability, suppressing the victim and dragging them in for execution. Activates automatically if ' +
            'the target is below {{ e2 }}% at the end of the effect.<br /><br />When Urgot executes a victim, nearby ' +
            'enemies flee in terror for {{ e0 }} seconds.</span></rules>',
          image: {
            full: 'UrgotR.png',
            sprite: 'spell12.png',
            group: 'spell',
            imgSrc: [],
            x: 48,
            y: 96,
            w: 48,
            h: 48
          }
        }
      ],
      passive: {
        name: 'Echoing Flames',
        description: 'Urgot\'s basic attacks and Purge periodically trigger blasts of flame from his legs, dealing ' +
          'physical damage.',
        image: {
          full: 'Urgot_Passive.png',
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
        full: 'Urgot.png',
        sprite: 'champion4.png',
        group: 'champion',
        imgSrc: [],
        x: 0,
        y: 0,
        w: 48,
        h: 48
      },
      tags: [
        'Fighter',
        'Marksman'
      ]
    },
    item1: {
      id: 3020,
      name: 'Sorcerer\'s Shoes',
      group: null,
      consumed: null,
      description: '<groupLimit>Limited to 1 pair of boots.</groupLimit><br><br><stats>+18 <a href=\'FlatMagicPen\'>' +
        'Magic Penetration</a></stats><br><br><unique>UNIQUE Passive - Enhanced Movement:</unique> +45 Movement Speed',
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
        full: '3020.png',
        sprite: 'item0.png',
        group: 'item',
        imgSrc: [],
        x: 0,
        y: 384,
        w: 48,
        h: 48
      },
      gold: {
        base: 800,
        total: 1100,
        sell: 770,
        purchasable: true
      }
    },
    item2: {
      id: 3156,
      name: 'Maw of Malmortius',
      group: null,
      consumed: null,
      description: '<stats>+50 Attack Damage<br>+50 Magic Resist<br>+10% Cooldown Reduction</stats><br><br><unique>' +
        'UNIQUE Passive - Lifeline:</unique> Upon taking magic damage that would reduce Health below 30%, grants a ' +
        'shield that absorbs up to 350 magic damage within 5 seconds (90 second cooldown).<br><unlockedPassive>Lifegrip:' +
        '</unlockedPassive> When <i>Lifeline</i> triggers, gain +20 Attack Damage, +10% Spell Vamp and +10% Life Steal ' +
        'until out of combat.',
      from: [
        3133,
        3155
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
        full: '3156.png',
        sprite: 'item1.png',
        group: 'item',
        imgSrc: [],
        x: 0,
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
    item3: {
      id: 3748,
      name: 'Titanic Hydra',
      group: null,
      consumed: null,
      description: '<stats>+450 Health<br>+40 Attack Damage<br>+100% Base Health Regen </stats><br><br><unique>UNIQUE ' +
        'Passive - Cleave:</unique> Basic attacks deal 5 + 1% of your maximum health as bonus physical damage  to your ' +
        'target and 40 + 2.5% of your maximum health as physical damage  to other enemies in a cone on hit.<br><active>' +
        'UNIQUE Active - Crescent:</active> Cleave damage to all targets is increased to 40 + 10% of your maximum ' +
        'health as bonus physical damage  in a larger cone for your next basic attack (20 second cooldown).<br><br>' +
        '<rules>(Unique Passives with the same name don\'t stack.)</rules>',
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
      image: {
        full: '3748.png',
        sprite: 'item2.png',
        group: 'item',
        imgSrc: [],
        x: 384,
        y: 288,
        w: 48,
        h: 48
      },
      gold: {
        base: 700,
        total: 3500,
        sell: 2450,
        purchasable: true
      }
    },
    item4: {
      id: 3004,
      name: 'Manamune',
      group: null,
      consumed: null,
      description: '<stats>+25 Attack Damage<br><mana>+250 Mana</mana></stats><br><br><mana><unique>UNIQUE Passive - ' +
        'Awe:</unique> Grants bonus Attack Damage equal to 2% of maximum Mana. Refunds 15% of Mana spent.<br><unique>' +
        'UNIQUE Passive - Mana Charge:</unique> Grants +5 maximum Mana (max +750 Mana) for each basic attack or Mana ' +
        'expenditure (occurs up to 3 times every 12 seconds).<br><br>Transforms into Muramana at +750 Mana.</mana>',
      from: [
        1037,
        3070
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
        full: '3004.png',
        sprite: 'item0.png',
        group: 'item',
        imgSrc: [],
        x: 192,
        y: 336,
        w: 48,
        h: 48
      },
      gold: {
        base: 675,
        total: 2400,
        sell: 1680,
        purchasable: true
      }
    },
    item5: {
      id: 3124,
      name: 'Guinsoo\'s Rageblade',
      group: null,
      consumed: null,
      description: '<stats>+25 Attack Damage<br>+25 Ability Power<br>+25% Attack Speed</stats><br><br><passive>Passive: ' +
        '</passive>Basic attacks deal 5 (+10% Bonus Attack Damage) physical and 5 (+10% Ability Power) magic damage on ' +
        'hit.<br><unique>UNIQUE Passive:</unique> Basic attacks grant +8% Attack Speed, +2.5% Bonus Attack Damage, ' +
        'and +2.5% Ability Power for 5 seconds (up to 6 stacks). At max stacks, gain <unlockedPassive>Guinsoo\'s Rage' +
        '</unlockedPassive>.<br><br><unlockedPassive>Guinsoo\'s Rage:</unlockedPassive> Every other basic attack ' +
        'triggers on hit effects twice.<br><br><rules><font color=\'#8c8c8c\'>While at half stacks, melee champions\' ' +
        'next attack will fully stack Rageblade.</font></rules>',
      from: [
        1037,
        1043,
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
      image: {
        full: '3124.png',
        sprite: 'item1.png',
        group: 'item',
        imgSrc: [],
        x: 96,
        y: 240,
        w: 48,
        h: 48
      },
      gold: {
        base: 990,
        total: 3300,
        sell: 2310,
        purchasable: true
      }
    },
    item6: {
      id: 3053,
      name: 'Sterak\'s Gage',
      group: null,
      consumed: null,
      description: '<stats>+450 Health</stats><br><br><unique>UNIQUE Passive - Giant Strength:</unique> Gain 50% of ' +
        'your Base Attack Damage as Bonus Attack Damage <br><unique>UNIQUE Passive - Lifeline:</unique> Upon taking at ' +
        'least 400 to 1800 damage (based on level) within 5 seconds, gain a shield for 75% of your bonus Health. After ' +
        '0.75 seconds, the shield decays over 3 seconds (60 second cooldown).<br><br><unlockedPassive>Sterak\'s Fury:' +
        '</unlockedPassive> When <i>Lifeline</i> triggers, grow in size and strength, gaining +30% Tenacity for 8 seconds.',
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
    summonerSpell1: {
      id: 21,
      name: 'Barrier',
      description: 'Shields your champion from 115-455 damage (depending on champion level) for 2 seconds.',
      image: {
        full: 'SummonerBarrier.png',
        sprite: 'spell0.png',
        group: 'spell',
        imgSrc: [],
        x: 0,
        y: 0,
        w: 48,
        h: 48
      },
      cooldown: [
        180
      ],
      key: 'SummonerBarrier',
      modes: [
        'ARAM',
        'CLASSIC',
        'TUTORIAL'
      ]
    },
    summonerSpell2: {
      id: 6,
      name: 'Ghost',
      description: 'Your champion gains increased Movement Speed and can move through units for 10 seconds. Grants a ' +
        'maximum of 28-45% (depending on champion level) Movement Speed after accelerating for 2 seconds.',
      image: {
        full: 'SummonerHaste.png',
        sprite: 'spell0.png',
        group: 'spell',
        imgSrc: [],
        x: 336,
        y: 0,
        w: 48,
        h: 48
      },
      cooldown: [
        180
      ],
      key: 'SummonerHaste',
      modes: [
        'ARAM',
        'CLASSIC',
        'TUTORIAL'
      ]
    },
    trinket: {
      id: 3364,
      name: 'Oracle Lens',
      group: null,
      consumed: null,
      description: '<groupLimit>Limited to 1 Trinket.</groupLimit><br><br><active>Active:</active> Scans around you, ' +
        'warning against hidden hostile units, and revealing invisible traps and revealing / disabling nearby wards for ' +
        '10 seconds (90 to 60 second cooldown).</maintext>',
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
        full: '3364.png',
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
    },
    map: {
      mapId: 11,
      mapName: 'Summoner\'s Rift',
      image: {
        full: 'map11.png',
        sprite: 'map0.png',
        group: 'map',
        imgSrc: [],
        x: 48,
        y: 0,
        w: 48,
        h: 48
      }
    }
  };

  describe('with build Id link parameter', () => {
    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [BuildsModule, HttpClientTestingModule, RouterTestingModule],
        providers: [
          {
            provide: ActivatedRouteSnapshot,
            useValue: {
              paramMap: convertToParamMap({ 'buildId': '1' })
            }
          }
        ]
      })
      .compileComponents();
    }));

    beforeEach(inject([BuildsService, Router], (buildsService: BuildsService, router: Router) => {
      resolver = TestBed.get(BuildsResolver);

      spyOn(router, 'navigate').and.returnValue(true);
      spyOn(buildsService, 'countBuilds').and.callThrough();
    }));

    afterEach(inject([BuildsService], (buildsService: BuildsService) => {
      expect(buildsService.getBuild).toHaveBeenCalledWith(1);
      expect(buildsService.countBuilds).not.toHaveBeenCalled();
    }));

    it('should resolve a Build', fakeAsync(inject([BuildsService, Router, ActivatedRouteSnapshot],
      (buildsService: BuildsService, router: Router, route: ActivatedRouteSnapshot) => {
      spyOn(buildsService, 'getBuild').and.returnValue(of(build));

      const buildsResolverDataObservable: Observable<BuildsResolverData> = resolver.resolve(route, null);

      tick();

      buildsResolverDataObservable.subscribe((data) => {
        expect(data.id).toBe(1);
        expect(data.savedBuild).toEqual(build);
        expect(router.navigate).not.toHaveBeenCalled();
      });
    })));
  });

  describe('without build Id link parameter', () => {
    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [BuildsModule, HttpClientTestingModule, RouterTestingModule],
        providers: [
          {
            provide: ActivatedRouteSnapshot,
            useValue: {
              paramMap: convertToParamMap({})
            }
          }
        ]
      })
      .compileComponents();
    }));

    beforeEach(inject([BuildsService, Router], (buildsService: BuildsService, router: Router) => {
      resolver = TestBed.get(BuildsResolver);

      spyOn(router, 'navigate').and.returnValue(true);
      spyOn(buildsService, 'getBuild').and.callThrough();
    }));

    afterEach(inject([BuildsService], (buildsService: BuildsService) => {
      expect(buildsService.getBuild).not.toHaveBeenCalled();
      expect(buildsService.countBuilds).toHaveBeenCalled();
    }));

    it('should resolve a random Build', fakeAsync(inject([BuildsService, Router, ActivatedRouteSnapshot],
      (buildsService: BuildsService, router: Router, route: ActivatedRouteSnapshot) => {
      spyOn(buildsService, 'countBuilds').and.returnValue(of(2));

      const buildsResolverDataObservable: Observable<BuildsResolverData> = resolver.resolve(route, null);

      tick();

      buildsResolverDataObservable.subscribe((data) => {
        expect(data.id).toBe(1);
        expect(data.savedBuild).toEqual(build);
        expect(router.navigate).toHaveBeenCalled();
        expect(buildsService.countBuilds).toHaveBeenCalled();
      });
    })));

    it('should resolve the only random Build', fakeAsync(inject([BuildsService, Router, ActivatedRouteSnapshot],
      (buildsService: BuildsService, router: Router, route: ActivatedRouteSnapshot) => {
      spyOn(buildsService, 'countBuilds').and.returnValue(of(1));

      const buildsResolverDataObservable: Observable<BuildsResolverData> = resolver.resolve(route, null);

      tick();

      buildsResolverDataObservable.subscribe((data) => {
        expect(data.id).toBe(1);
        expect(data.savedBuild).toEqual(build);
        expect(router.navigate).toHaveBeenCalled();
        expect(buildsService.countBuilds).toHaveBeenCalled();
      });
    })));
  });

});
