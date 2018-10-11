import { async, ComponentFixture, inject, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { By } from '@angular/platform-browser';

import { of } from 'rxjs';

import { ChampionPage } from './champion.page';
import { ChampionModule } from './champion.module';
import { TitleService } from '@service/title.service';
import { ChampionService } from './champion.service';
import { Champion } from '@model/champion';
import { GameMap } from '@model/game-map';
import { TrollBuild } from '@model/troll-build';
import { Build, BuildBuilder } from '@model/build';

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
          'Slash\'s cooldown by 0.25 seconds (quadrupled against champions).'
      },
      {
        key: 'SkarnerExoskeleton',
        name: 'Crystalline Exoskeleton',
        description: 'Skarner gains a shield and has increased Movement Speed while the shield persists.',
        tooltip: 'Skarner is shielded for <span class=\'colorCC3300\'>{{ f1 }}</span> ({{ e1 }}% of his maximum health) ' +
          '<span class=\'color99FF99\'>(+{{ a1 }})</span> damage for {{ e4 }} seconds. While the shield persists, ' +
          'Skarner gains movement speed that ramps up to {{ e5 }}% over 3 seconds.'
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
          'them to deal {{ e2 }} additional physical damage and stun the target for {{ e3 }} second.'
      },
      {
        key: 'SkarnerImpale',
        name: 'Impale',
        description: 'Skarner suppresses an enemy champion and deals damage to it. During this time, Skarner can move ' +
          'freely and will drag his helpless victim around with him. When the effect ends, the target will be dealt additional damage.',
        tooltip: 'Skarner suppresses an enemy champion for {{ e1 }} seconds, dealing <span class=\'colorFF8C00\'> ' +
          '{{ a2 }}</span> physical damage plus {{ e2 }} <span class=\'color99FF99\'>(+{{ a1 }})</span> magic damage. ' +
          'Skarner can move freely during this time, and will drag his helpless victim around with him. When the effect ' +
          'ends, Skarner\'s target will be dealt the same damage again.'
      }
    ],
    passive: {
      name: 'Crystal Spires',
      description: 'Skarner\'s presence causes crystals to spawn in set locations around the map. While near crystals ' +
        'his team owns, Skarner gains tremendous movement speed, attack speed, and mana regeneration.'
    },
    tags: [
      'Fighter',
      'Tank'
    ]
  };

  const twistedTreeline: GameMap = {
    mapId: 10,
    mapName: 'The Twisted Treeline'
  };

  const summonersRift: GameMap = {
    mapId: 11,
    mapName: 'Summoner\'s Rift'
  };

  const howlingAbyss: GameMap = {
    mapId: 12,
    mapName: 'Howling Abyss'
  };

  const maps: GameMap[] = [howlingAbyss, summonersRift, twistedTreeline]; // sorted alphabetically from api

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

    const twistedTreelineTrollBuild: TrollBuild = {
      summonerSpells: [
        {
          id: 1,
          name: 'Cleanse',
          description: 'Removes all disables (excluding suppression and airborne) and summoner spell debuffs affecting ' +
            'your champion and lowers the duration of incoming disables by 65% for 3 seconds.',
          cooldown: [
            210
          ],
          key: 'SummonerBoost',
          modes: [
            'ARAM',
            'CLASSIC',
            'TUTORIAL'
          ]
        },
        {
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
        }
      ],
      items: [
        {
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
            '12': true,
            '21': true
          },
          gold: {
            base: 800,
            total: 1100,
            sell: 770,
            purchasable: true
          }
        },
        {
          id: 3026,
          name: 'Guardian Angel',
          group: null,
          consumed: null,
          description: '<stats>+45 Attack Damage<br>+40 Armor</stats><br><br><unique>UNIQUE Passive:</unique> Upon taking ' +
            'lethal damage, restores 50% of base Health and 30% of maximum Mana after 4 seconds of stasis (300 second cooldown).',
          from: [
            1031,
            1038,
            2420
          ],
          into: [],
          requiredChampion: null,
          requiredAlly: null,
          maps: {
            '10': false,
            '11': true,
            '12': false,
            '21': false
          },
          gold: {
            base: 100,
            total: 2800,
            sell: 1120,
            purchasable: true
          }
        },
        {
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
            '12': false,
            '21': false
          },
          gold: {
            base: 550,
            total: 1800,
            sell: 720,
            purchasable: true
          }
        },
        {
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
            '12': true,
            '21': true
          },
          gold: {
            base: 1365,
            total: 2650,
            sell: 1855,
            purchasable: true
          }
        },
        {
          id: 3165,
          name: 'Morellonomicon',
          group: null,
          consumed: null,
          description: '<stats>+80 Ability Power<br>+300 Health</stats><br><br><unique>UNIQUE Passive - Touch of Death:' +
            '</unique> +15 <a href=\'FlatMagicPen\'>Magic Penetration</a><br><unique>UNIQUE Passive - Cursed Strike:' +
            '</unique> Magic damage dealt to champions inflicts them with <a href=\'GrievousWounds\'>Grievous Wounds</a> ' +
            'for 3 seconds.',
          from: [
            1026,
            3916
          ],
          into: [],
          requiredChampion: null,
          requiredAlly: null,
          maps: {
            '10': true,
            '11': true,
            '12': true,
            '21': true
          },
          gold: {
            base: 650,
            total: 3000,
            sell: 2100,
            purchasable: true
          }
        },
        {
          id: 3508,
          name: 'Essence Reaver',
          group: null,
          consumed: null,
          description: '<stats>+75 Attack Damage<br>+20% Cooldown Reduction<br><mana>+300 Mana</mana></stats><br><br><unique>' +
            'UNIQUE Passive:</unique> Basic attacks refund 1% of your missing mana.<br><unique>UNIQUE Passive:</unique> ' +
            'After you cast your ultimate, your next basic attack within 10 seconds grants you <unlockedPassive>Essence ' +
            'Flare</unlockedPassive> for 6 seconds (30 second cooldown).<br><br><unlockedPassive>Essence Flare:</unlockedPassive> ' +
            'Gain 50% attack speed, and basic attacks refund 20% of your remaining non-ultimate cooldowns.',
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
            '12': true,
            '21': true
          },
          gold: {
            base: 450,
            total: 3200,
            sell: 2240,
            purchasable: true
          }
        }
      ],
      trinket: {
        id: 3348,
        name: 'Arcane Sweeper',
        group: null,
        consumed: null,
        description: '<active>UNIQUE Active - Hunter\'s Sight:</active> An arcane mist grants vision in the target area ' +
          'for 5 seconds, revealing enemy champions and granting <font color=\'#ee91d7\'>True Sight</font> of traps in ' +
          'the area for 3 seconds (90 second cooldown).',
        from: [],
        into: [],
        requiredChampion: null,
        requiredAlly: null,
        maps: {
          '10': true,
          '11': false,
          '12': false,
          '21': true
        },
        gold: {
          base: 0,
          total: 0,
          sell: 0,
          purchasable: false
        }
      }
    };

    const summonersRiftTrollBuild: TrollBuild = {
      summonerSpells: [
        {
          id: 12,
          name: 'Teleport',
          description: 'After channeling for 4.5 seconds, teleports your champion to target allied structure, minion, or ward.',
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
      ],
      items: [
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
          into: [],
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
          into: [],
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
          into: [],
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
          into: [],
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
          into: [],
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
          into: [],
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
          gold: {
            base: 1150,
            total: 3700,
            sell: 2590,
            purchasable: true
          }
        }
      ],
      trinket: {
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
        gold: {
          base: 0,
          total: 0,
          sell: 0,
          purchasable: true
        }
      }
    };

    const howlingAbyssTrollBuild: TrollBuild = {
      summonerSpells: [
        {
          id: 4,
          name: 'Flash',
          description: 'Teleports your champion a short distance toward your cursor\'s location.',
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
          id: 3,
          name: 'Exhaust',
          description: 'Exhausts target enemy champion, reducing their Movement Speed by 30%, and their damage dealt ' +
            'by 40% for 2.5 seconds.',
          cooldown: [
            210
          ],
          key: 'SummonerExhaust',
          modes: [
            'ARAM',
            'CLASSIC',
            'TUTORIAL'
          ]
        }
      ],
      items: [
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
          into: [],
          requiredChampion: null,
          requiredAlly: null,
          maps: {
            '10': true,
            '11': true,
            '12': true,
            '21': true
          },
          gold: {
            base: 600,
            total: 900,
            sell: 630,
            purchasable: true
          }
        },
        {
          id: 3814,
          name: 'Edge of Night',
          group: null,
          consumed: null,
          description: '<stats>+250 Health<br>+55 Attack Damage</stats><br><br><unique>UNIQUE Passive:</unique> +18 ' +
            '<a href=\'Lethality\'>Lethality</a><br><unique>UNIQUE Active - Night\'s Veil:</unique> Channel for 1.5 ' +
            'second to grant a spell shield that blocks the next enemy ability. Lasts for 7 seconds (40 second cooldown).' +
            '<br><br><rules>(Can move while channeling, but taking damage breaks the channel.)</rules>',
          from: [
            1028,
            1037,
            3134
          ],
          into: [],
          requiredChampion: null,
          requiredAlly: null,
          maps: {
            '10': true,
            '11': true,
            '12': true,
            '21': true
          },
          gold: {
            base: 725,
            total: 3100,
            sell: 2170,
            purchasable: true
          }
        },
        {
          id: 3030,
          name: 'Hextech GLP-800',
          group: null,
          consumed: null,
          description: '<stats>+80 Ability Power<br><mana>+600 Mana</mana><br>+10% Cooldown Reduction</stats><br><br>' +
            '<unique>UNIQUE Passive - Haste:</unique> This item gains an additional 10% Cooldown Reduction.<br><unique>' +
            'UNIQUE Active - Frost Bolt:</unique> Fires a spray of icy bolts that explode on first unit hit, dealing ' +
            '<scaleLevel>100 - 200</scaleLevel> (+20% of your Ability Power) magic damage to all enemies hit. (40 second ' +
            'cooldown, shared with other <font color=\'#9999FF\'><a href=\'itembolt\'>Hextech</a></font> items).<br><br> ' +
            'Enemies hit are slowed by 65% decaying over 2 second.',
          from: [
            3145,
            3802
          ],
          into: [],
          requiredChampion: null,
          requiredAlly: null,
          maps: {
            '10': true,
            '11': true,
            '12': true,
            '21': true
          },
          gold: {
            base: 450,
            total: 2800,
            sell: 1960,
            purchasable: true
          }
        },
        {
          id: 3683,
          name: 'Rainbow Snax Party Pack!',
          group: null,
          consumed: null,
          description: '<active>Active - <a href=\'FeedTheKing\'>Feed The King</a>:</active> The King tosses many Snax ' +
            'behind the enemy, attracting Poros which dash back towards him. Enemy champions hit will be knocked forwards ' +
            'and dealt <scaleLevel>230-680</scaleLevel> physical damage. (120s cooldown)',
          from: [],
          into: [],
          requiredChampion: null,
          requiredAlly: null,
          maps: {
            '10': false,
            '11': false,
            '12': true,
            '21': false
          },
          gold: {
            base: 0,
            total: 0,
            sell: 0,
            purchasable: true
          }
        },
        {
          id: 3115,
          name: 'Nashor\'s Tooth',
          group: null,
          consumed: null,
          description: '<stats>+50% Attack Speed<br>+80 Ability Power</stats><br><br><unique>UNIQUE Passive:</unique> +20% ' +
            'Cooldown Reduction<br><unique>UNIQUE Passive:</unique> Basic attacks deal 15 (+15% of Ability Power) bonus ' +
            'magic damage on hit.<br>',
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
            '12': true,
            '21': true
          },
          gold: {
            base: 1000,
            total: 3000,
            sell: 2100,
            purchasable: true
          }
        },
        {
          id: 3748,
          name: 'Titanic Hydra',
          group: null,
          consumed: null,
          description: '<stats>+450 Health<br>+40 Attack Damage<br>+100% Base Health Regen </stats><br><br><unique>UNIQUE ' +
            'Passive - Cleave:</unique> Basic attacks deal 5 + 1% of your maximum health as bonus physical damage  to ' +
            'your target and 40 + 2.5% of your maximum health as physical damage  to other enemies in a cone on hit.' +
            '<br><active>UNIQUE Active - Crescent:</active> Cleave damage to all targets is increased to 40 + 10% of ' +
            'your maximum health as bonus physical damage  in a larger cone for your next basic attack (20 second ' +
            'cooldown).<br><br><rules>(Unique Passives with the same name don\'t stack.)</rules>',
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
            '12': true,
            '21': true
          },
          gold: {
            base: 700,
            total: 3500,
            sell: 2450,
            purchasable: true
          }
        }
      ],
      trinket: {
        id: 2052,
        name: 'Poro-Snax',
        group: null,
        consumed: true,
        description: 'This savory blend of free-range, grass-fed Avarosan game hens and organic, non-ZMO Freljordian ' +
          'herbs contains the essential nutrients necessary to keep your Poro purring with pleasure.<br><br><i>All ' +
          'proceeds will be donated towards fighting Noxian animal cruelty.</i>',
        from: [],
        into: [],
        requiredChampion: null,
        requiredAlly: null,
        maps: {
          '10': true,
          '11': true,
          '12': true,
          '21': true
        },
        gold: {
          base: 0,
          total: 0,
          sell: 0,
          purchasable: false
        }
      }
    };

    const trollBuilds: { [mapId: number]: TrollBuild; } = {
      [twistedTreeline.mapId]: twistedTreelineTrollBuild,
      [summonersRift.mapId]: summonersRiftTrollBuild,
      [howlingAbyss.mapId]: howlingAbyssTrollBuild
    };

    beforeEach(inject([ChampionService, TitleService],
      (championService: ChampionService, title: TitleService) => {
      spyOn(championService, 'getChampion').and.returnValue(of(skarner));
      spyOn(championService, 'mapsForTrollBuild').and.returnValue(of(maps));

      spyOn(title, 'setTitle').and.callThrough();
    }));

    afterEach(inject([ChampionService, TitleService],
      (championService: ChampionService, title: TitleService) => {
      expect(championService.getChampion).not.toHaveBeenCalledWith(skarner.name);
      expect(championService.mapsForTrollBuild).toHaveBeenCalled();

      expect(title.setTitle).toHaveBeenCalledWith(skarner.name);
    }));

    it('should show Troll Build for Summoner\'s Rift by default', inject([ChampionService], (championService: ChampionService) => {
      spyOn(championService, 'getTrollBuild').and.returnValue(of(summonersRiftTrollBuild));

      fixture.detectChanges();

      expect(championService.getTrollBuild).toHaveBeenCalledWith(skarner.name, GameMap.summonersRiftId);
    }));

    it('should generate a new Troll Build after selecting a new map',
      inject([ChampionService], (championService: ChampionService) => {
      fixture.detectChanges();

      const mapSelectDe = fixture.debugElement.query(By.css('#map-select'));
      expect(mapSelectDe).toBeTruthy();

      const championServiceSpy = spyOn(championService, 'getTrollBuild');

      for (let i = 0; i < maps.length; i++) {
        const mapId = maps[i].mapId;
        const trollBuild = trollBuilds[mapId];

        championServiceSpy.and.returnValue(of(trollBuild));

        mapSelectDe.nativeElement.value = mapSelectDe.nativeElement.options[i].value;
        mapSelectDe.nativeElement.dispatchEvent(new Event('change'));

        fixture.detectChanges();

        expectChampionAndMapsAndTrollBuild(trollBuild);

        expect(championService.getTrollBuild).toHaveBeenCalledWith(skarner.name, mapId);
        expect(component.build).toBeNull();
      }
    }));

    it('should reset the saved Build and generate a new Troll Build after clicking the new build button',
      inject([ChampionService], (championService: ChampionService) => {
      fixture.detectChanges();

      const championServiceSpy = spyOn(championService, 'getTrollBuild');

      const mapSelectDe = fixture.debugElement.query(By.css('#map-select'));
      expect(mapSelectDe).toBeTruthy();

      const newBuildDe = fixture.debugElement.query(By.css('#new-build-btn'));
      expect(newBuildDe).toBeTruthy();

      for (let i = 0; i < maps.length; i++) {
        const map = maps[i];
        const mapId = map.mapId;
        const trollBuild = trollBuilds[mapId];

        championServiceSpy.and.returnValue(of(trollBuild));

        const build = new BuildBuilder()
          .withChampion(skarner)
          .withItems(trollBuild.items)
          .withSummonerSpells(trollBuild.summonerSpells)
          .withTrinket(trollBuild.trinket)
          .withGameMap(map)
          .build();

        component.build = build;

        mapSelectDe.nativeElement.value = mapSelectDe.nativeElement.options[i].value;
        mapSelectDe.nativeElement.dispatchEvent(new Event('change'));

        newBuildDe.triggerEventHandler('click', null);

        fixture.detectChanges();

        expectChampionAndMapsAndTrollBuild(trollBuild);

        expect(championService.getTrollBuild).toHaveBeenCalledWith(skarner.name, mapId);
        expect(component.build).toBeNull();
      }
    }));

    it('should save a Troll Build with after clicking the save build button and then show its self link',
      inject([ChampionService], (championService: ChampionService) => {
      fixture.detectChanges();

      const championServiceSpy = spyOn(championService, 'getTrollBuild');
      const buildsServiceSpy = spyOn(championService, 'saveBuild');
      const execCommandSpy =  spyOn(document, 'execCommand');

      const mapSelectDe = fixture.debugElement.query(By.css('#map-select'));
      expect(mapSelectDe).toBeTruthy();

      for (let i = 0; i < maps.length; i++) {
        const map = maps[i];
        const mapId = map.mapId;
        const trollBuild = trollBuilds[mapId];

        championServiceSpy.and.returnValue(of(trollBuild));

        const build = new BuildBuilder()
          .withChampion(skarner)
          .withItems(trollBuild.items)
          .withSummonerSpells(trollBuild.summonerSpells)
          .withTrinket(trollBuild.trinket)
          .withGameMap(map)
          .build();

        const savedBuild = new Build();
        savedBuild.id = Math.floor((Math.random() * 1000) + 1);
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
        const selfRef = `http://localhost/api/build/${savedBuild.id}`;

        buildsServiceSpy.and.returnValue(of(new HttpResponse<Build>({
          body: savedBuild,
          headers: new HttpHeaders({
            'Location': selfRef
          }),
          status: 201
        })));

        mapSelectDe.nativeElement.value = mapSelectDe.nativeElement.options[i].value;
        mapSelectDe.nativeElement.dispatchEvent(new Event('change'));

        fixture.detectChanges();

        expectChampionAndMapsAndTrollBuild(trollBuild);

        const newBuildBtnDe = fixture.debugElement.query(By.css('#save-build-btn'));
        newBuildBtnDe.triggerEventHandler('click', null);

        fixture.detectChanges();

        expect(championService.saveBuild).toHaveBeenCalledWith(build);

        const savedBuildInputDe = fixture.debugElement.query(By.css('#saved-build-input-link'));
        expect(savedBuildInputDe.nativeElement.value).toBe(`http://localhost/build/${savedBuild.id}`);
        expect(savedBuildInputDe.attributes['readonly']).toBe('');

        expect(newBuildBtnDe.nativeElement.disabled).toBeTruthy('Expected save build button to be disabled');

        execCommandSpy.and.callThrough();

        const savedBuildCopyBtnDe = fixture.debugElement.query(By.css('#copy-build-link-btn'));
        expect(savedBuildCopyBtnDe).toBeTruthy();
        savedBuildCopyBtnDe.triggerEventHandler('click', null);

        fixture.detectChanges();

        expect(document.execCommand).toHaveBeenCalledWith('copy');
      }
    }));

    function expectChampionAndMapsAndTrollBuild(trollBuild: TrollBuild) {
      // Champion
      const championsNameDe = fixture.debugElement.query(By.css('#champion-name'));
      expect(championsNameDe.nativeElement.textContent).toBe(skarner.name);

      const championsTitleDe = fixture.debugElement.query(By.css('#champion-title'));
      expect(championsTitleDe.nativeElement.textContent).toBe(skarner.title);

      const championPartypeDe = fixture.debugElement.query(By.css('#champion-partype'));
      expect(championPartypeDe.nativeElement.textContent).toBe(`${skarner.partype}`);

      const championTagsDe = fixture.debugElement.queryAll(By.css('.champion-tag'));
      expect(championTagsDe.map(championTagDe => championTagDe.nativeElement.textContent)).toEqual(skarner.tags);

      // New Build
      const newBuildBtnDe = fixture.debugElement.query(By.css('#new-build-btn'));
      expect(newBuildBtnDe.classes['is-loading']).toBeFalsy();
      expect(newBuildBtnDe.nativeElement.textContent).toBe('New Build');

      // Maps
      const mapsOptionDe = fixture.debugElement.queryAll(By.css('.map-option'));
      expect(mapsOptionDe.map(mapOptionDe => mapOptionDe.nativeElement.textContent.trim()))
        .toEqual(maps.map(map => map.mapName));

      // Troll Build
      const trollBuildItemsHeaderDe = fixture.debugElement.query(By.css('.troll-build-items-header'));
      expect(trollBuildItemsHeaderDe.nativeElement.textContent).toBe('Items');
      const trollBuildItemsDe = fixture.debugElement.queryAll(By.css('.troll-build-item'));
      expect(trollBuildItemsDe.length).toBe(trollBuildItemsDe.length);
      for (let i = 0; i < trollBuildItemsDe.length; i++) {
        expect(trollBuildItemsDe[i].attributes['data-tooltip']).toBe(trollBuild.items[i].name);
        const trollBuildItemImgDe = trollBuildItemsDe[i].query(By.css('.troll-build-item-img'));
        expect(trollBuildItemImgDe.nativeElement.src).toContain(`/api/img/items/${trollBuild.items[i].id}`);
      }

      const trollBuildSummonerSpellsHeaderDe = fixture.debugElement.query(By.css('.troll-build-summoner-spells-header'));
      expect(trollBuildSummonerSpellsHeaderDe.nativeElement.textContent).toBe('Summoner Spells');
      const trollBuildSummonerSpellsDe = fixture.debugElement.queryAll(By.css('.troll-build-summoner-spell'));
      expect(trollBuildSummonerSpellsDe.length).toBe(trollBuildSummonerSpellsDe.length);
      for (let i = 0; i < trollBuildSummonerSpellsDe.length; i++) {
        expect(trollBuildSummonerSpellsDe[i].attributes['data-tooltip']).toBe(trollBuild.summonerSpells[i].name);
        const trollBuildSummonerSpellsImgDe = trollBuildSummonerSpellsDe[i].query(By.css('.troll-build-summoner-spells-img'));
        expect(trollBuildSummonerSpellsImgDe.nativeElement.src).toContain(`/api/img/summoner-spells/${trollBuild.summonerSpells[i].id}`);
      }

      const trollBuildTrinketHeaderDe = fixture.debugElement.query(By.css('.troll-build-trinket-header'));
      expect(trollBuildTrinketHeaderDe.nativeElement.textContent).toBe('Trinket');
      const trollBuildTrinketDe = fixture.debugElement.query(By.css('.troll-build-trinket'));
      expect(trollBuildTrinketDe).toBeTruthy();
      expect(trollBuildTrinketDe.attributes['data-tooltip']).toBe(trollBuild.trinket.name);
      const trollBuildTrinketImgDe = fixture.debugElement.query(By.css('.troll-build-trinket-img'));
      expect(trollBuildTrinketImgDe.nativeElement.src).toContain(`/api/img/items/${trollBuild.trinket.id}`);

      // Save Troll Build
      const saveBuildBtnDe = fixture.debugElement.query(By.css('#save-build-btn'));
      expect(saveBuildBtnDe.nativeElement.textContent).toBe('Save Build');
      expect(newBuildBtnDe.nativeElement.disabled).toBeFalsy('Expected save build button to be enabled');
    }

  });

  describe('show Champion by name with a loading Troll Build and maps to select', () => {

    beforeEach(inject([ChampionService], (championService: ChampionService) => {
      spyOn(championService, 'getChampion').and.returnValue(of(skarner));
      spyOn(championService, 'getTrollBuild').and.callThrough();
      spyOn(championService, 'mapsForTrollBuild').and.returnValue(of(maps));
    }));

    afterEach(inject([ChampionService], (championService: ChampionService) => {
      expect(championService.getChampion).not.toHaveBeenCalledWith(skarner.name);
      expect(championService.getTrollBuild).toHaveBeenCalledWith(skarner.name, GameMap.summonersRiftId);
      expect(championService.mapsForTrollBuild).toHaveBeenCalled();
    }));

    it('should show placeholders indicating a loading Troll Build ', () => {
      fixture.detectChanges();

      const newBuildBtnLoadingDe = fixture.debugElement.query(By.css('#new-build-btn'));
      expect(newBuildBtnLoadingDe.classes['is-loading']).toBeTruthy();

      const trollBuildLoadingItemsHeaderDe = fixture.debugElement.query(By.css('.troll-build-items-header'));
      expect(trollBuildLoadingItemsHeaderDe.nativeElement.textContent).toBe('Items');
      const trollBuildLoadingItemsDe = fixture.debugElement.queryAll(By.css('.troll-build-loading-item'));
      expect(trollBuildLoadingItemsDe.length).toBe(6);

      const trollBuildLoadingSummonerSpellsHeaderDe = fixture.debugElement.query(By.css('.troll-build-summoner-spells-header'));
      expect(trollBuildLoadingSummonerSpellsHeaderDe.nativeElement.textContent).toBe('Summoner Spells');
      const trollBuildLoadingSummonerSpellsDe = fixture.debugElement.queryAll(By.css('.troll-build-loading-summoner-spell'));
      expect(trollBuildLoadingSummonerSpellsDe.length).toBe(2);

      const trollBuildLoadingTrinketHeaderDe = fixture.debugElement.query(By.css('.troll-build-trinket-header'));
      expect(trollBuildLoadingTrinketHeaderDe.nativeElement.textContent).toBe('Trinket');
      const trollBuildLoadingTrinketDe = fixture.debugElement.query(By.css('.troll-build-loading-trinket'));
      expect(trollBuildLoadingTrinketDe).toBeTruthy();

      const saveBuildBtnDe = fixture.debugElement.query(By.css('#save-build-btn'));
      expect(saveBuildBtnDe).toBeFalsy();
    });

  });

  describe('show Champion by name with a failed to load Troll Build and maps to select', () => {

    beforeEach(inject([ChampionService], (championService: ChampionService) => {
      spyOn(championService, 'getChampion').and.returnValue(of(skarner));
      spyOn(championService, 'getTrollBuild').and.returnValue(of(new TrollBuild()));
      spyOn(championService, 'mapsForTrollBuild').and.returnValue(of(maps));
    }));

    afterEach(inject([ChampionService], (championService: ChampionService) => {
      expect(championService.getChampion).not.toHaveBeenCalledWith(skarner.name);
      expect(championService.getTrollBuild).toHaveBeenCalledWith(skarner.name, GameMap.summonersRiftId);
      expect(championService.mapsForTrollBuild).toHaveBeenCalled();
    }));

    it('should show unable to generate Troll Build alert', () => {
      fixture.detectChanges();

      const alertDe = fixture.debugElement.query(By.css('#no-troll-build-alert'));
      expect(alertDe).toBeTruthy();

      const alertMsgDe = fixture.debugElement.query(By.css('#no-troll-build-alert-msg'));
      expect(alertMsgDe.nativeElement.textContent)
        .toBe('An error has occurred while generating a Troll Build. Please try again later.');
    });

  });

  describe('show Champion by name with loading game maps', () => {

    beforeEach(inject([ChampionService], (championService: ChampionService) => {
      spyOn(championService, 'getChampion').and.returnValue(of(skarner));
      spyOn(championService, 'getTrollBuild').and.callThrough();
      spyOn(championService, 'mapsForTrollBuild').and.callThrough();
    }));

    afterEach(inject([ChampionService], (championService: ChampionService) => {
      expect(championService.getChampion).not.toHaveBeenCalledWith(skarner.name);
      expect(championService.getTrollBuild).not.toHaveBeenCalledWith(skarner.name, GameMap.summonersRiftId);
      expect(championService.mapsForTrollBuild).toHaveBeenCalled();
    }));

    it('should should game maps select with single loading option', () => {
      fixture.detectChanges();

      const mapSelectLoadingDe = fixture.debugElement.query(By.css('#map-select-loading'));
      expect(mapSelectLoadingDe).toBeTruthy();

      const mapsOptionDe = fixture.debugElement.queryAll(By.css('.map-option-loading'));
      expect(mapsOptionDe.map(mapOptionDe => mapOptionDe.nativeElement.textContent.trim()))
        .toEqual(jasmine.arrayContaining(['Fetching maps...']));
    });

  });

});
