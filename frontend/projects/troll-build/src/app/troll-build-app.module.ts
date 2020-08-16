import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';

import { NbLayoutModule, NbThemeModule } from '@nebular/theme';
import { NbEvaIconsModule } from '@nebular/eva-icons';
import { LoadingBarHttpClientModule } from '@ngx-loading-bar/http-client';
import { LoadingBarRouterModule } from '@ngx-loading-bar/router';

import { TrollBuildAppComponent } from './troll-build-app.component';
import { TrollBuildAppRoutingModule } from './troll-build-app-routing.module';
import { BasePathHttpInterceptor } from './interceptor/base-path.http-interceptor';
import { LayoutModule } from '@ltb-layout/layout.module';

@NgModule({
  declarations: [
    TrollBuildAppComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    BrowserAnimationsModule,
    NbThemeModule.forRoot({ name: 'league-troll-build' }),
    NbLayoutModule,
    NbEvaIconsModule,
    LoadingBarHttpClientModule,
    LoadingBarRouterModule,
    TrollBuildAppRoutingModule,
    LayoutModule
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: BasePathHttpInterceptor, multi: true }
  ],
  bootstrap: [TrollBuildAppComponent]
})
export class TrollBuildAppModule { }
