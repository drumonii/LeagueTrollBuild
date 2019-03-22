import { async, inject, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { ActivatedRouteSnapshot, convertToParamMap, Router } from '@angular/router';

import { of } from 'rxjs';

import { ChampionResolver } from './champion.resolver';
import { ChampionService } from './champion.service';
import { Champion } from '@ltb-model/champion';

describe('ChampionResolver', () => {
  let resolver: ChampionResolver;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule],
      providers: [ChampionResolver, ChampionService,
        {
          provide: ActivatedRouteSnapshot,
          useValue: {
            paramMap: convertToParamMap({ name: 'Kennen' })
          }
        }
      ]
    })
    .compileComponents();
  }));

  beforeEach(inject([Router], (router: Router) => {
    resolver = TestBed.get(ChampionResolver);

    spyOn(router, 'navigate');
  }));

  afterEach(inject([ChampionService], (championsService: ChampionService) => {
    expect(championsService.getChampion).toHaveBeenCalledWith('Kennen');
  }));

  it('should resolve a Champion',
    async(inject([ChampionService, Router, ActivatedRouteSnapshot],
      (championsService: ChampionService, router: Router, route: ActivatedRouteSnapshot) => {
    const kennen: Champion = {
      id: 85,
      key: 'Kennen',
      name: 'Kennen',
      title: 'the Heart of the Tempest',
      partype: 'Energy',
      info: {
        attack: 6,
        defense: 4,
        magic: 7,
        difficulty: 4
      },
      spells: [], // omitted for brevity
      passive: {
        name: 'Mark of the Storm',
        description: 'Kennen\'s abilities add one stack of Mark of the Storm to their target for 6 seconds. If the ' +
          'target reaches 3 stacks, it is stunned for 1.25 seconds and Kennen gains 25 Energy. <br><br>' +
          '<font color=\'#99FF99\'>The stun will be a diminished duration of 0.5 seconds if it occurs again within 6 seconds.</font>'
      },
      tags: [
        'Mage',
        'Marksman'
      ]
    };
    spyOn(championsService, 'getChampion').and.returnValue(of(kennen));

    resolver.resolve(route, null).subscribe(champion => {
      expect(champion).toEqual(kennen);
      expect(router.navigate).not.toHaveBeenCalled();
    });
  })));

  it('should redirect to /champions when unable to resolve a Champion',
    async(inject([ChampionService, Router, ActivatedRouteSnapshot],
      (championsService: ChampionService, router: Router, route: ActivatedRouteSnapshot) => {
    spyOn(championsService, 'getChampion').and.returnValue(of(null));

    resolver.resolve(route, null).subscribe(champion => {
      expect(champion).toBeNull();
      expect(router.navigate).toHaveBeenCalledWith(['/champions']);
    });
  })));
});
