import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HTTP_INTERCEPTORS, HttpClientModule, HttpClientXsrfModule } from '@angular/common/http';

import { ClrLayoutModule } from '@clr/angular';
import { LoadingBarHttpClientModule } from '@ngx-loading-bar/http-client';

import { AdminAppComponent } from './admin-app.component';
import { AdminBasePathHttpInterceptor } from './interceptor/admin-base-path.http-interceptor';
import { AdminErrorHttpInterceptor } from './interceptor/admin-error.http-interceptor';
import { AdminAppRoutingModule } from './admin-app-routing.module';
import { AdminLayoutModule } from './layout/admin-layout.module';

@NgModule({
  declarations: [
    AdminAppComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    HttpClientXsrfModule,
    ClrLayoutModule,
    LoadingBarHttpClientModule,
    AdminAppRoutingModule,
    AdminLayoutModule
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: AdminBasePathHttpInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: AdminErrorHttpInterceptor, multi: true }
  ],
  bootstrap: [AdminAppComponent]
})
export class AdminAppModule { }
