import { async, ComponentFixture, inject, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';

import { of } from 'rxjs/observable/of';

import { ChampionsPage } from './champions.page';
import { ChampionsService } from '@service/champions.service';

describe('ChampionsPage', () => {
  let component: ChampionsPage;
  let fixture: ComponentFixture<ChampionsPage>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule],
      declarations: [ChampionsPage],
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

    fixture.detectChanges();

    expect(championsService.getChampions).toHaveBeenCalled();
  }));
});
