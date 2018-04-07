import { async, ComponentFixture, inject, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { FormsModule } from '@angular/forms';

import { of } from 'rxjs/observable/of';

import { ChampionsPage } from './champions.page';
import { ChampionsService } from '@service/champions.service';
import { ChampionsNameFilterPipe } from '@pipe/champions-name-filter.pipe';

describe('ChampionsPage', () => {
  let component: ChampionsPage;
  let fixture: ComponentFixture<ChampionsPage>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [FormsModule, HttpClientTestingModule, RouterTestingModule],
      declarations: [ChampionsPage, ChampionsNameFilterPipe],
      providers: [ChampionsService]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ChampionsPage);
    component = fixture.componentInstance;
  });

  it('should create', inject([ChampionsService], (championsService: ChampionsService) => {
    spyOn(championsService, 'getChampions').and.returnValue(of([]));
    spyOn(championsService, 'getChampionTags').and.returnValue(of([]));

    fixture.detectChanges();

    expect(championsService.getChampions).toHaveBeenCalled();
    expect(championsService.getChampionTags).toHaveBeenCalled();
  }));
});
