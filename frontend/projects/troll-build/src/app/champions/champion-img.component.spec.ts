import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { RouterLinkWithHref } from '@angular/router';
import { By } from '@angular/platform-browser';

import { ChampionImgModule } from './champion-img.module';
import { ChampionImgComponent } from './champion-img.component';
import { Champion } from '@ltb-model/champion';
import { LazyLoadImgDirective } from '@ltb-directive/lazy-load-img.directive';

describe('ChampionImgComponent', () => {
  let component: ChampionImgComponent;
  let fixture: ComponentFixture<ChampionImgComponent>;

  const alistar: Champion =  {
    id: 12,
    key: 'Alistar',
    name: 'Alistar',
    title: 'the Minotaur',
    partype: 'Mana',
    info: {
      attack: 6,
      defense: 9,
      magic: 5,
      difficulty: 7
    },
    spells: [], // omitted for brevity
    passive: {
      name: 'Triumphant Roar',
      description: 'Alistar charges his roar by stunning or displacing enemy champions or when nearby enemies die. When fully charged he ' +
        'heals himself and all nearby allied champions.'
    },
    tags: [
      'Mage',
      'Support'
    ]
  };

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule, ChampionImgModule]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ChampionImgComponent);
    component = fixture.componentInstance;

    component.champion = alistar;

    fixture.detectChanges();
  });

  it('should show the Champion', () => {
    const championLink = fixture.debugElement.query(By.css('[data-e2e="champion-link"]'));
    const championRouterLink = championLink.injector.get(RouterLinkWithHref);
    expect(championRouterLink.href).toBe(`/champions/${alistar.key}`);

    const championName = fixture.debugElement.query(By.css('[data-e2e="champion-name"]'));
    expect(championName.nativeElement.textContent).toBe(alistar.name);

    const championImg = fixture.debugElement.query(By.css('[data-e2e="champion-img"]'));
    expect(championImg.injector.get(LazyLoadImgDirective)).toBeTruthy();
    expect(championImg.nativeElement.src).toContain('assets/images/dummy_champion.png');
    expect(championImg.attributes['ng-reflect-img-src']).toBe(`/api/img/champions/${alistar.id}`);
  });
});
