import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClientModule, HttpClientXsrfModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';

import { AppComponent } from './app.component';

import { FooterComponent } from '@layout/footer/footer.component';
import { HeaderComponent } from '@layout/header/header.component';

import { ChampionsPage } from '@page/champions/champions.page';
import { NotFoundPage } from '@page/error/not-found.page';

import { ChampionsNameFilterPipe } from '@pipe/champions-name-filter.pipe';
import { ChampionsTagsFilterPipe } from '@pipe/champions-tags-filter.pipe';

import { ChampionsService } from '@service/champions.service';
import { GameMapsService } from '@service/game-maps.service';
import { VersionsService } from '@service/versions.service';

@NgModule({
  declarations: [
    AppComponent,
    ChampionsNameFilterPipe,
    ChampionsTagsFilterPipe,
    ChampionsPage,
    NotFoundPage,
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
    ChampionsService,
    GameMapsService,
    VersionsService
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
