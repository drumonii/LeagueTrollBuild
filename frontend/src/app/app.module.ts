import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS, HttpClientModule, HttpClientXsrfModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';

import { AppComponent } from './app.component';

import { FooterComponent } from '@layout/footer/footer.component';
import { HeaderComponent } from '@layout/header/header.component';

import { BuildsPage } from '@page/builds/builds.page';
import { ChampionPage } from '@page/champion/champion.page';
import { ChampionsPage } from '@page/champions/champions.page';

import { ChampionsNameFilterPipe } from '@pipe/champions-name-filter.pipe';
import { ChampionsTagsFilterPipe } from '@pipe/champions-tags-filter.pipe';

import { BuildsService } from '@service/builds.service';
import { ChampionsService } from '@service/champions.service';
import { GameMapsService } from '@service/game-maps.service';
import { VersionsService } from '@service/versions.service';

import { BaseUrlHttpInterceptor } from './interceptor/base-url.http-interceptor';
import { WithCredentialsHttpInterceptor } from './interceptor/with-credentials.http-interceptor';

@NgModule({
  declarations: [
    AppComponent,
    ChampionsNameFilterPipe,
    ChampionsTagsFilterPipe,
    BuildsPage,
    ChampionPage,
    ChampionsPage,
    FooterComponent,
    HeaderComponent
  ],
  imports: [
    AppRoutingModule,
    BrowserModule,
    FormsModule,
    HttpClientModule,
    HttpClientXsrfModule
  ],
  providers: [
    BuildsService,
    ChampionsService,
    GameMapsService,
    VersionsService,
    { provide: HTTP_INTERCEPTORS, useClass: BaseUrlHttpInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: WithCredentialsHttpInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
