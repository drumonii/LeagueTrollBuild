import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { FlywayPage } from './flyway.page';

const routes: Routes = [
  { path: '', component: FlywayPage }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class FlywayRoutingModule { }
