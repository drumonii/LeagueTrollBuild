import { async, ComponentFixture,  inject, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, convertToParamMap } from '@angular/router';

import { of } from 'rxjs/observable/of';

import { ChampionPage } from './champion.page';
import { BuildsService } from '@service/builds.service';
import { ChampionsService } from '@service/champions.service';
import { GameMapsService } from '@service/game-maps.service';
import { GameMap } from '@model/game-map';

describe('ChampionPage', () => {
  let component: ChampionPage;
  let fixture: ComponentFixture<ChampionPage>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [FormsModule, HttpClientTestingModule, RouterTestingModule],
      declarations: [ChampionPage],
      providers: [
        BuildsService,
        ChampionsService,
        GameMapsService,
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

  it('should create', inject([ChampionsService, GameMapsService], (championsService: ChampionsService, gameMapsService: GameMapsService) => {
    spyOn(championsService, 'getChampion').and.returnValue(of(null));
    spyOn(championsService, 'getTrollBuild').and.returnValue(of(null));
    spyOn(gameMapsService, 'forTrollBuild').and.returnValue(of([]));

    fixture.detectChanges();

    expect(championsService.getChampion).toHaveBeenCalledWith('Skarner');
    expect(championsService.getTrollBuild).toHaveBeenCalledWith('Skarner', GameMap.summonersRiftId);
    expect(gameMapsService.forTrollBuild).toHaveBeenCalled();
  }));
});
