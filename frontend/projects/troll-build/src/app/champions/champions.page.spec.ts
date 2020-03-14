import { async, ComponentFixture, inject, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { By } from '@angular/platform-browser';

import { NbA11yModule } from '@nebular/theme';
import { NbEvaIconsModule } from '@nebular/eva-icons';

import { of } from 'rxjs';

import { ChampionsPage } from './champions.page';
import { ChampionsModule } from './champions.module';
import { ChampionImgComponent } from './champion-img.component';
import { ChampionsService } from './champions.service';
import { Champion } from '@ltb-model/champion';
import { TitleService } from '@ltb-service/title.service';

describe('ChampionsPage', () => {
  let component: ChampionsPage;
  let fixture: ComponentFixture<ChampionsPage>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule, NbA11yModule.forRoot(), NbEvaIconsModule, ChampionsModule],
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ChampionsPage);
    component = fixture.componentInstance;
  });

  describe('with Champions and Champion tags', () => {

    const maokai: Champion =  {
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
          'or is struck by an enemy\'s spell, this cooldown is reduced.'
      },
      tags: [
        'Mage',
        'Tank'
      ]
    };

    const sion: Champion = {
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
          '(max 75 to monsters).<br><br>All his abilities are replaced with Death Surge, which grants a burst of Movement Speed.'
      },
      tags: [
        'Fighter',
        'Tank'
      ]
    };

    const champions: Champion[] = [maokai, sion];

    const tags = ['Assassin', 'Fighter', 'Mage', 'Marksman', 'Support', 'Tank'];

    beforeEach(inject([ChampionsService, TitleService], (championsService: ChampionsService, title: TitleService) => {
      spyOn(title, 'resetTitle');
      spyOn(championsService, 'getChampions').and.returnValue(of(champions));
      spyOn(championsService, 'getChampionTags').and.returnValue(of(tags));

      fixture.detectChanges();
    }));

    afterEach(inject([ChampionsService, TitleService], (championsService: ChampionsService, title: TitleService) => {
      expect(championsService.getChampions).toHaveBeenCalled();
      expect(championsService.getChampionTags).toHaveBeenCalled();
      expect(title.resetTitle).toHaveBeenCalled();
    }));

    it('should show champions and tags', () => {
      const championsSearch = fixture.debugElement.query(By.css('[data-e2e="champions-search-input"]'));
      expect(championsSearch.nativeElement.placeholder).toBe('Search by Champion');

      const championTags = fixture.debugElement.queryAll(By.css('.ltb-btn-group button.ltb-btn'));
      expect(championTags.map(championTag => championTag.nativeElement.textContent.trim())).toEqual(tags);

      expect(fixture.debugElement.queryAll(By.directive(ChampionImgComponent)).length).toBe(champions.length);
    });

    it('should filter Champions with clicking a tag', () => {
      const tagToFilter = 'Mage';
      const tagToFilterIndex = tags.findIndex(tag => tag === tagToFilter);

      const championTags = fixture.debugElement.queryAll(By.css('.ltb-btn-group button.ltb-btn'));
      championTags[tagToFilterIndex].triggerEventHandler('click', null);

      fixture.detectChanges();

      expect(component.championsFilterTag).toBe(tagToFilter);

      expect(fixture.debugElement.queryAll(By.directive(ChampionImgComponent)).length).toBe(1);
    });

    it('should reset the Champions filter with re-clicking the same tag', () => {
      const tagToFilter = 'Fighter';
      const tagToFilterIndex = tags.findIndex(tag => tag === tagToFilter);
      component.championsFilterTag = tagToFilter;

      const championTags = fixture.debugElement.queryAll(By.css('.ltb-btn-group button.ltb-btn'));
      championTags[tagToFilterIndex].triggerEventHandler('click', null);

      fixture.detectChanges();

      expect(component.championsFilterTag).toBe('');

      expect(fixture.debugElement.queryAll(By.directive(ChampionImgComponent)).length).toBe(champions.length);
    });

    it('should filter Champions with search input', () => {
      const championsSearch = fixture.debugElement.query(By.css('[data-e2e="champions-search-input"]'));
      championsSearch.nativeElement.value = 'maokai';
      const inputEvent = document.createEvent('Event');
      inputEvent.initEvent('input', false, false);
      championsSearch.nativeElement.dispatchEvent(inputEvent);

      fixture.detectChanges();

      expect(fixture.debugElement.queryAll(By.directive(ChampionImgComponent)).length).toBe(1);
    });

  });

  describe('with no Champions nor Champion tags', () => {

    beforeEach(inject([ChampionsService], (championsService: ChampionsService) => {
      spyOn(championsService, 'getChampions').and.returnValue(of([]));
      spyOn(championsService, 'getChampionTags').and.returnValue(of([]));

      fixture.detectChanges();
    }));

    afterEach(inject([ChampionsService], (championsService: ChampionsService) => {
      expect(championsService.getChampions).toHaveBeenCalled();
      expect(championsService.getChampionTags).toHaveBeenCalled();
    }));

    it('should show the no Champions alert', () => {
      const alert = fixture.debugElement.query(By.css('[data-e2e="no-champions-alert"]'));
      expect(alert.nativeElement.textContent).toContain('No Champions exist in the database!');
    });

  });
});
