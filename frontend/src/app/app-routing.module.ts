import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  { path: 'builds', loadChildren: './page/builds/builds.module#BuildsModule' },
  { path: 'champions', loadChildren: './page/champions/champions.module#ChampionsModule' },
  { path: 'champions/:name', loadChildren: './page/champion/champion.module#ChampionModule' },
  { path: '', redirectTo: '/champions', pathMatch: 'full' },
  { path: '**', loadChildren: './page/error/not-found.module#NotFoundModule' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
