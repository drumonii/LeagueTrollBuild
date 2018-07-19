import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClientModule, HttpClientXsrfModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';

import { AppComponent } from './app.component';

import { FooterComponent } from '@layout/footer/footer.component';
import { HeaderComponent } from '@layout/header/header.component';

import { NotFoundPage } from '@page/error/not-found.page';

import { VersionsService } from '@service/versions.service';

@NgModule({
  declarations: [
    AppComponent,
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
    VersionsService
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
