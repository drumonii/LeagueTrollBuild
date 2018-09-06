import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { BuildsResolver } from './builds.resolver';

import { BuildsPage } from './builds.page';

const routes: Routes = [
  { path: '', component: BuildsPage, resolve: { build: BuildsResolver } },
  { path: ':buildId', component: BuildsPage, resolve: { build: BuildsResolver } }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
  providers: [BuildsResolver]
})
export class BuildsRoutingModule { }
