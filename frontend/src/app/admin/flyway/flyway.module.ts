import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { FlywayRoutingModule } from './flyway-routing.module';
import { FlywayPage } from './flyway.page';
import { FlywayService } from './flyway.service';

@NgModule({
  imports: [
    CommonModule,
    FlywayRoutingModule
  ],
  declarations: [
    FlywayPage
  ],
  providers: [
    FlywayService
  ]
})
export class FlywayModule { }
