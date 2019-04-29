import { async, ComponentFixture, inject, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { By } from '@angular/platform-browser';

import { of } from 'rxjs';

import { ChampionsPage } from './champions.page';
import { ChampionsModule } from './champions.module';
import { ChampionComponent } from './champion.component';
import { Champion } from '@ltb-model/champion';
import { TitleService } from '@ltb-service/title.service';
import { ChampionsService } from './champions.service';

describe('ChampionsPage', () => {
  let component: ChampionsPage;
  let fixture: ComponentFixture<ChampionsPage>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ChampionsModule, HttpClientTestingModule, RouterTestingModule],
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

    beforeEach(inject([ChampionsService], (championsService: ChampionsService) => {
      spyOn(championsService, 'getChampions').and.returnValue(of(champions));
      spyOn(championsService, 'getChampionTags').and.returnValue(of(tags));
    }));

    afterEach(inject([ChampionsService], (championsService: ChampionsService) => {
      expect(championsService.getChampions).toHaveBeenCalled();
      expect(championsService.getChampionTags).toHaveBeenCalled();
    }));

    it('should reset the title', inject([TitleService], (title: TitleService) => {
      spyOn(title, 'resetTitle').and.callThrough();

      fixture.detectChanges();

      expectChampionAndTags();

      expect(title.resetTitle).toHaveBeenCalledWith();
    }));

    it('should filter Champions with clicking a tag', () => {
      fixture.detectChanges();

      expectChampionAndTags();

      const tagToFilter = 'Mage';
      const tagToFilterIndex = tags.findIndex(tag => tag === tagToFilter);

      const championTags = fixture.debugElement.queryAll(By.css('.champion-tag-btn'));
      championTags[tagToFilterIndex].triggerEventHandler('click', null);

      fixture.detectChanges();

      expect(component.championsFilterTag).toBe(tagToFilter);

      const championBoxes = fixture.debugElement.queryAll(By.css('.champion'));
      expect(championBoxes.length).toBe(1);
    });

    it('should reset the Champions filter with re-clicking the same tag', () => {
      fixture.detectChanges();

      expectChampionAndTags();

      const tagToFilter = 'Fighter';
      const tagToFilterIndex = tags.findIndex(tag => tag === tagToFilter);
      component.championsFilterTag = tagToFilter;

      const championTags = fixture.debugElement.queryAll(By.css('.champion-tag-btn'));
      championTags[tagToFilterIndex].triggerEventHandler('click', null);

      fixture.detectChanges();

      expect(component.championsFilterTag).toBe('');

      const championCards = fixture.debugElement.queryAll(By.css('.champion'));
      expect(championCards.length).toBe(champions.length);
    });

    it('should filter Champions with search input', () => {
      fixture.detectChanges();

      expectChampionAndTags();

      const championsSearch = fixture.debugElement.query(By.css('#champions-search-input'));
      championsSearch.nativeElement.value = 'maokai';
      const inputEvent = document.createEvent('Event');
      inputEvent.initEvent('input', false, false);
      championsSearch.nativeElement.dispatchEvent(inputEvent);

      fixture.detectChanges();

      const championBoxes = fixture.debugElement.queryAll(By.css('.champion'));
      expect(championBoxes.length).toBe(1);
    });

    function expectChampionAndTags() {
      const championsSearch = fixture.debugElement.query(By.css('#champions-search-input'));
      expect(championsSearch.nativeElement.placeholder).toBe('Search by Champion');

      const championTags = fixture.debugElement.queryAll(By.css('.champion-tag-btn'));
      expect(championTags.map(championTag => championTag.nativeElement.textContent.trim())).toEqual(tags);

      const championBoxes = fixture.debugElement.queryAll(By.css('.champion'));
      expect(championBoxes.length).toBe(champions.length);

      expect(fixture.debugElement.queryAll(By.directive(ChampionComponent)).length).toBe(champions.length);

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

      const alert = fixture.debugElement.query(By.css('#no-champions-alert'));
      expect(alert).toBeTruthy();

      const alertMsg = fixture.debugElement.query(By.css('#no-champions-alert-msg'));
      expect(alertMsg.nativeElement.textContent).toBe('No Champions exist in the database!');
    });

  });

});
