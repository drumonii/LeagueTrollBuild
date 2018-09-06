import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  { path: 'builds', loadChildren: './builds/builds.module#BuildsModule' },
  { path: 'champions', loadChildren: './champions/champions.module#ChampionsModule' },
  { path: 'champions/:name', loadChildren: './champion/champion.module#ChampionModule' },
  { path: '', redirectTo: '/champions', pathMatch: 'full' },
  { path: '**', loadChildren: './error/not-found.module#NotFoundModule' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
