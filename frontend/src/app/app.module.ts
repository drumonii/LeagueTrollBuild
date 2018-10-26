import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HTTP_INTERCEPTORS, HttpClientModule, HttpClientXsrfModule } from '@angular/common/http';

import { AppComponent } from './app.component';

import { TrollBuildAppModule } from './troll-build/troll-build-app.module';
import { AdminAppModule } from './admin/admin-app.module';

import { AppRoutingModule } from './app-routing.module';

import { BasePathHttpInterceptor } from '@interceptor/base-path.http-interceptor';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    AppRoutingModule,
    AdminAppModule,
    TrollBuildAppModule,
    BrowserModule,
    HttpClientModule,
    HttpClientXsrfModule,
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: BasePathHttpInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
