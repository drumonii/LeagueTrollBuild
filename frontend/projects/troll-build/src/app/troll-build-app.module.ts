import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';

import { LoadingBarHttpClientModule } from '@ngx-loading-bar/http-client';

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
    LoadingBarHttpClientModule,
    TrollBuildAppRoutingModule,
    LayoutModule
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: BasePathHttpInterceptor, multi: true }
  ],
  bootstrap: [TrollBuildAppComponent]
})
export class TrollBuildAppModule { }
