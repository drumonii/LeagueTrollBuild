import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { AdminBatchPage } from './admin-batch.page';

const routes: Routes = [
  { path: '', component: AdminBatchPage }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminBatchRoutingModule { }
