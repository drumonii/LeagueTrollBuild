import { async, ComponentFixture, inject, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { RouterLinkWithHref } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { By, Title } from '@angular/platform-browser';

import { of } from 'rxjs';

import { ChampionsPage } from './champions.page';
import { Champion } from '@model/champion';
import { ChampionsService } from '@service/champions.service';
import { ChampionsNameFilterPipe } from '@pipe/champions-name-filter.pipe';
import { ChampionsTagsFilterPipe } from '@pipe/champions-tags-filter.pipe';

describe('ChampionsPage', () => {
  let component: ChampionsPage;
  let fixture: ComponentFixture<ChampionsPage>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [FormsModule, HttpClientTestingModule, RouterTestingModule],
      declarations: [ChampionsPage, ChampionsNameFilterPipe, ChampionsTagsFilterPipe],
      providers: [ChampionsService]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ChampionsPage);
    component = fixture.componentInstance;
  });

  describe('with Champions and Champion tags', () => {

    const champions: Champion[] = [
      {
        id: 57,
        key: 'Maokai',
        name: 'Maokai',
        title: 'the Twisted Treant',
        partype: 'Mana',
        info: {
          attack: 3,
          defense: 8,
          magic: 6,
          difficulty: 3
        },
        spells: [], // omitted for brevity
        passive: {
          name: 'Sap Magic',
          description: 'Maokai\'s basic attack also heal him on a moderate cooldown. Each time Maokai casts a spell ' +
            'or is struck by an enemy\'s spell, this cooldown is reduced.',
          image: {
            full: 'Maokai_Passive.png',
            sprite: 'passive2.png',
            group: 'passive',
            imgSrc: [],
            x: 432,
            y: 0,
            w: 48,
            h: 48
          }
        },
        image: {
          full: 'Maokai.png',
          sprite: 'champion2.png',
          group: 'champion',
          imgSrc: [],
          x: 432,
          y: 0,
          w: 48,
          h: 48
        },
        tags: [
          'Mage',
          'Tank'
        ]
      },
      {
        id: 14,
        key: 'Sion',
        name: 'Sion',
        title: 'The Undead Juggernaut',
        partype: 'Mana',
        info: {
          attack: 5,
          defense: 9,
          magic: 3,
          difficulty: 5
        },
        spells: [], // omitted for brevity
        passive: {
          name: 'Glory in Death',
          description: 'After being killed, Sion will reanimate with rapidly decaying Health. His attacks become very ' +
            'rapid, gain 100% Lifesteal and deal bonus damage equal to 10% of his target\'s maximum Health ' +
            '(max 75 to monsters).<br><br>All his abilities are replaced with Death Surge, which grants a burst of Movement Speed.',
          image: {
            full: 'Sion_Passive1.png',
            sprite: 'passive3.png',
            group: 'passive',
            imgSrc: [],
            x: 0,
            y: 48,
            w: 48,
            h: 48
          }
        },
        image: {
          full: 'Sion.png',
          sprite: 'champion3.png',
          group: 'champion',
          imgSrc: [],
          x: 0,
          y: 48,
          w: 48,
          h: 48
        },
        tags: [
          'Fighter',
          'Tank'
        ]
      }
    ];

    const tags = ['Assassin', 'Fighter', 'Mage', 'Marksman', 'Support', 'Tank'];

    beforeEach(inject([ChampionsService, Title], (championsService: ChampionsService) => {
      spyOn(championsService, 'getChampions').and.returnValue(of(champions));
      spyOn(championsService, 'getChampionTags').and.returnValue(of(tags));
    }));

    afterEach(inject([ChampionsService, Title], (championsService: ChampionsService) => {
      expect(championsService.getChampions).toHaveBeenCalled();
      expect(championsService.getChampionTags).toHaveBeenCalled();
    }));

    it('should leave title as is with no content title', inject([Title], (title: Title) => {
      spyOn(title, 'getTitle').and.returnValue('League Troll Build');
      spyOn(title, 'setTitle').and.callThrough();

      fixture.detectChanges();

      expectChampionAndTags();

      expect(title.setTitle).not.toHaveBeenCalled();
    }));

    it('should reset title with existing content title', inject([Title], (title: Title) => {
      spyOn(title, 'getTitle').and.returnValue('League Troll Build | Ryze');
      spyOn(title, 'setTitle').and.callThrough();

      fixture.detectChanges();

      expectChampionAndTags();

      expect(title.setTitle).toHaveBeenCalledWith('League Troll Build');
    }));

    it('should filter Champions with clicking a tag', () => {
      fixture.detectChanges();

      expectChampionAndTags();

      const tagToFilter = 'Mage';
      const tagToFilterIndex = tags.findIndex(tag => tag === tagToFilter);

      const championTagsDe: DebugElement[] = fixture.debugElement.queryAll(By.css('.champion-tag-btn'));
      championTagsDe[tagToFilterIndex].triggerEventHandler('click', null);

      fixture.detectChanges();

      expect(component.championsFilterTag).toBe(tagToFilter);

      const championsDe: DebugElement[] = fixture.debugElement.queryAll(By.css('.champion'));
      expect(championsDe.length).toBe(1);
    });

    it('should reset the Champions filter with re-clicking the same tag', () => {
      fixture.detectChanges();

      expectChampionAndTags();

      const tagToFilter = 'Fighter';
      const tagToFilterIndex = tags.findIndex(tag => tag === tagToFilter);
      component.championsFilterTag = tagToFilter;

      const championTagsDe: DebugElement[] = fixture.debugElement.queryAll(By.css('.champion-tag-btn'));
      championTagsDe[tagToFilterIndex].triggerEventHandler('click', null);

      fixture.detectChanges();

      expect(component.championsFilterTag).toBe('');

      const championsDe: DebugElement[] = fixture.debugElement.queryAll(By.css('.champion'));
      expect(championsDe.length).toBe(champions.length);
    });

    it('should filter Champions with search input', () => {
      fixture.detectChanges();

      expectChampionAndTags();

      const championsSearchDe: DebugElement = fixture.debugElement.query(By.css('#champions-search-input'));
      championsSearchDe.nativeElement.value = 'maokai';
      championsSearchDe.nativeElement.dispatchEvent(new Event('input'));

      fixture.detectChanges();

      const championsDe: DebugElement[] = fixture.debugElement.queryAll(By.css('.champion'));
      expect(championsDe.length).toBe(1);
    });

    function expectChampionAndTags() {
      const championsSearchDe = fixture.debugElement.query(By.css('#champions-search-input'));
      expect(championsSearchDe.nativeElement.placeholder).toBe('Search by Champion');

      const championTagsDe = fixture.debugElement.queryAll(By.css('.champion-tag-btn'));
      expect(championTagsDe.map(championTagDe => championTagDe.nativeElement.textContent.trim())).toEqual(tags);

      const championsDe = fixture.debugElement.queryAll(By.css('.champion'));
      expect(championsDe.length).toBe(champions.length);

      const maokaiLinkDe = championsDe[0].query(By.css('a'));
      expect(maokaiLinkDe.nativeElement.textContent).toContain('Maokai');
      const maokaiRouterLink = maokaiLinkDe.injector.get(RouterLinkWithHref);
      expect(maokaiRouterLink.href).toBe('/champions/Maokai');

      const sionLinkDe = championsDe[1].query(By.css('a'));
      expect(sionLinkDe.nativeElement.textContent).toContain('Sion');
      const sionRouterLink = sionLinkDe.injector.get(RouterLinkWithHref);
      expect(sionRouterLink.href).toBe('/champions/Sion');
    }

  });

  describe('with no Champions nor Champion tags', () => {

    beforeEach(inject([ChampionsService], (championsService: ChampionsService) => {
      spyOn(championsService, 'getChampions').and.returnValue(of([]));
      spyOn(championsService, 'getChampionTags').and.returnValue(of([]));
    }));

    afterEach(inject([ChampionsService], (championsService: ChampionsService) => {
      expect(championsService.getChampions).toHaveBeenCalled();
      expect(championsService.getChampionTags).toHaveBeenCalled();
    }));

    it('should show the no Champions alert', () => {
      fixture.detectChanges();

      const alertDe = fixture.debugElement.query(By.css('#no-champions-alert'));
      expect(alertDe.nativeElement.textContent).toBe('No Champions exist in the database!');
    });

  });

});
