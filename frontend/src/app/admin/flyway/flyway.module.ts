import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { NgxDatatableModule } from '@swimlane/ngx-datatable';

import { FlywayRoutingModule } from './flyway-routing.module';
import { FlywayPage } from './flyway.page';
import { FlywayService } from './flyway.service';

@NgModule({
  imports: [
    CommonModule,
    NgxDatatableModule,
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
