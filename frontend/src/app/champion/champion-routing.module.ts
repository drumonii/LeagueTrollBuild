import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { ChampionResolver } from './champion.resolver';

import { ChampionPage } from './champion.page';

const routes: Routes = [
  { path: '', component: ChampionPage, resolve: { champion: ChampionResolver } }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
  providers: [ChampionResolver]
})
export class ChampionRoutingModule { }
