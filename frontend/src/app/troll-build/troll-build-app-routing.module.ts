import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { TrollBuildAppComponent } from './troll-build-app.component';

const routes: Routes = [
  {
    path: '',
    component: TrollBuildAppComponent,
    children: [
      { path: '', redirectTo: '/champions', pathMatch: 'full' },
      { path: 'builds', loadChildren: './builds/builds.module#BuildsModule' },
      { path: 'champions', loadChildren: './champions/champions.module#ChampionsModule' },
      { path: 'champions/:name', loadChildren: './champion/champion.module#ChampionModule' },
      { path: '**', loadChildren: './error/not-found.module#NotFoundModule' }
    ]
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TrollBuildAppRoutingModule { }
