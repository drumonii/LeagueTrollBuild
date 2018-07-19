import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { ChampionPage } from './champion.page';

const routes: Routes = [
  { path: '', component: ChampionPage }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ChampionRoutingModule { }
