import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ClrAlertModule } from '@clr/angular';

import { AdminBatchRoutingModule } from './admin-batch-routing.module';
import { BatchJobDatagridModule } from './batch-job-datagrid.module';
import { AdminBatchPage } from './admin-batch.page';

@NgModule({
  imports: [
    CommonModule,
    ClrAlertModule,
    AdminBatchRoutingModule,
    BatchJobDatagridModule
  ],
  declarations: [
    AdminBatchPage
  ]
})
export class AdminBatchModule { }
