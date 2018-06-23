import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { BuildsPage } from '@page/builds/builds.page';
import { ChampionPage } from '@page/champion/champion.page';
import { ChampionsPage } from '@page/champions/champions.page';
import { NotFoundPage } from '@page/error/not-found.page';

const routes: Routes = [
  { path: 'builds', redirectTo: '/champions', pathMatch: 'full' },
  { path: 'builds/:buildId', component: BuildsPage },
  { path: 'champions', component: ChampionsPage },
  { path: 'champions/:name', component: ChampionPage },
  { path: '', redirectTo: '/champions', pathMatch: 'full' },
  { path: '**', component: NotFoundPage }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
