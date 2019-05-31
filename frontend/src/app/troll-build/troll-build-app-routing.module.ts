import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { TrollBuildAppComponent } from './troll-build-app.component';
import { DisabledGuard } from '@ltb-guard/disabled.guard';

const routes: Routes = [
  {
    path: '',
    component: TrollBuildAppComponent,
    children: [
      {
        path: '',
        redirectTo: '/champions',
        pathMatch: 'full'
      },
      {
        path: 'builds',
        loadChildren: () => import('./saved-builds/saved-builds.module').then(m => m.SavedBuildsModule),
        canLoad: [DisabledGuard]
      },
      {
        path: 'champions',
        loadChildren: () => import('./champions/champions.module').then(m => m.ChampionsModule)
      },
      {
        path: 'champions/:name',
        loadChildren: () => import('./champion/champion.module').then(m => m.ChampionModule)
      },
      {
        path: '**',
        loadChildren: () => import('./error/not-found.module').then(m => m.NotFoundModule)
      }
    ]
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TrollBuildAppRoutingModule { }
