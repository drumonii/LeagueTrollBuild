import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AdminBatchRoutingModule } from './admin-batch-routing.module';

import { AdminBatchPage } from './admin-batch.page';
import { AdminBatchService } from './admin-batch.service';

@NgModule({
  imports: [
    CommonModule,
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
