import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule, HttpClientXsrfModule } from '@angular/common/http';

import { AppComponent } from './app.component';

import { TrollBuildAppModule } from './troll-build/troll-build-app.module';
import { AdminAppModule } from './admin/admin-app.module';

import { AppRoutingModule } from './app-routing.module';

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
  bootstrap: [AppComponent]
})
export class AppModule {}
