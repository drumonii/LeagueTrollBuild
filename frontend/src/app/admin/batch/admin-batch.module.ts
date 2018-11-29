import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { NgxDatatableModule } from '@swimlane/ngx-datatable';

import { AdminBatchRoutingModule } from './admin-batch-routing.module';
import { AdminBatchPage } from './admin-batch.page';
import { AdminBatchService } from './admin-batch.service';

@NgModule({
  imports: [
    CommonModule,
    NgxDatatableModule,
    AdminBatchRoutingModule
  ],
  declarations: [
    AdminBatchPage
  ],
  providers: [
    AdminBatchService
  ]
})
export class AdminBatchModule { }
