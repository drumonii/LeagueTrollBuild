import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { ChampionsPage } from './champions.page';

const routes: Routes = [
  { path: '', component: ChampionsPage },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ChampionsRoutingModule { }
