import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { NotFoundPage } from '@page/error/not-found.page';

const routes: Routes = [
  { path: 'builds', loadChildren: './page/builds/builds.module#BuildsModule' },
  { path: 'champions', loadChildren: './page/champions/champions.module#ChampionsModule' },
  { path: 'champions/:name', loadChildren: './page/champion/champion.module#ChampionModule' },
  { path: '', redirectTo: '/champions', pathMatch: 'full' },
  { path: '**', component: NotFoundPage }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
