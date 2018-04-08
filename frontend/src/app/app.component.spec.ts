import { TestBed, async } from '@angular/core/testing';
import { APP_BASE_HREF } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';

import { AppComponent } from './app.component';

import { FooterComponent } from '@layout/footer/footer.component';
import { HeaderComponent } from '@layout/header/header.component';

import { ChampionPage } from '@page/champion/champion.page';
import { ChampionsPage } from '@page/champions/champions.page';

import { ChampionsNameFilterPipe } from '@pipe/champions-name-filter.pipe';
import { ChampionsTagsFilterPipe } from '@pipe/champions-tags-filter.pipe';

describe('AppComponent', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        AppComponent,
        ChampionsNameFilterPipe,
        ChampionsTagsFilterPipe,
        ChampionPage,
        ChampionsPage,
        FooterComponent,
        HeaderComponent
      ],
      imports: [
        AppRoutingModule,
        FormsModule
      ],
      providers: [
        { provide: APP_BASE_HREF, useValue: '/' }
      ]
    })
    .compileComponents();
  }));
  it('should create the app', async(() => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.debugElement.componentInstance;
    expect(app).toBeTruthy();
  }));
});
