import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HTTP_INTERCEPTORS, HttpClientModule, HttpClientXsrfModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';

import { AppComponent } from './app.component';

import { LayoutModule } from '@layout/layout.module';
import { LoadingBarModule } from '@loading-bar/loading-bar.module';

import { BasePathHttpInterceptor } from '@interceptor/base-path.http-interceptor';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    AppRoutingModule,
    BrowserModule,
    HttpClientModule,
    HttpClientXsrfModule,
    LayoutModule,
    LoadingBarModule
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: BasePathHttpInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
