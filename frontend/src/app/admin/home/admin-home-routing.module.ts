import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { AdminHomePage } from './admin-home.page';

const routes: Routes = [
  { path: '', component: AdminHomePage }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminHomeRoutingModule { }
