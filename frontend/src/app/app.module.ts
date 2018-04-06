import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';

import { AppComponent } from './app.component';

import { FooterComponent } from '@layout/footer/footer.component';
import { HeaderComponent } from '@layout/header/header.component';

import { ChampionsPage } from '@page/champions/champions.page';

import { ChampionsNameFilterPipe } from '@pipe/champions-name-filter.pipe';

import { ChampionsService } from '@service/champions.service';
import { GameMapsService } from '@service/game-maps.service';

import { BaseUrlHttpInterceptor } from './interceptor/base-url.http-interceptor';

@NgModule({
  declarations: [
    AppComponent,
    ChampionsNameFilterPipe,
    ChampionsPage,
    FooterComponent,
    HeaderComponent
  ],
  imports: [
    AppRoutingModule,
    BrowserModule,
    FormsModule,
    HttpClientModule
  ],
  providers: [
    ChampionsService,
    GameMapsService,
    { provide: HTTP_INTERCEPTORS, useClass: BaseUrlHttpInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
