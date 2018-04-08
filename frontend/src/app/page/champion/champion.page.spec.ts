import { async, ComponentFixture,  inject, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { ActivatedRoute, convertToParamMap } from '@angular/router';

import { of } from 'rxjs/observable/of';

import { ChampionPage } from './champion.page';
import { ChampionsService } from '@service/champions.service';

describe('ChampionPage', () => {
  let component: ChampionPage;
  let fixture: ComponentFixture<ChampionPage>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule],
      declarations: [ChampionPage],
      providers: [
        ChampionsService,
        {
          provide: ActivatedRoute,
          useValue: {
            paramMap: of(convertToParamMap({'name': 'Skarner'}))
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

  it('should create', inject([ChampionsService], (championsService: ChampionsService) => {
    spyOn(championsService, 'getChampion').and.returnValue(of(null));

    fixture.detectChanges();

    expect(championsService.getChampion).toHaveBeenCalledWith('Skarner');
  }));
});
