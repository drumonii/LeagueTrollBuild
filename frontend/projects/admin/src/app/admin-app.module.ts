import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HTTP_INTERCEPTORS, HttpClientModule, HttpClientXsrfModule } from '@angular/common/http';

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
    HttpClientModule,
    HttpClientXsrfModule,
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
