import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HTTP_INTERCEPTORS, } from '@angular/common/http';

import { LoadingBarHttpClientModule } from '@ngx-loading-bar/http-client';

import { TrollBuildAppComponent } from './troll-build-app.component';

// components in troll-build folder have ltb alias and admin have admin alias
import { TrollBuildAppRoutingModule } from './troll-build-app-routing.module';

import { LayoutModule } from '@ltb-layout/layout.module';
import { BasePathHttpInterceptor } from '@ltb-interceptor/base-path.http-interceptor';

@NgModule({
  declarations: [
    TrollBuildAppComponent
  ],
  imports: [
    CommonModule,
    LoadingBarHttpClientModule,
    TrollBuildAppRoutingModule,
    LayoutModule
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: BasePathHttpInterceptor, multi: true }
  ]
})
export class TrollBuildAppModule { }
