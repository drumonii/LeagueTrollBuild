import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { SavedBuildsResolver } from './saved-builds.resolver';

import { SavedBuildsPage } from './saved-builds.page';

const routes: Routes = [
  { path: '', component: SavedBuildsPage, resolve: { build: SavedBuildsResolver } },
  { path: ':buildId', component: SavedBuildsPage, resolve: { build: SavedBuildsResolver } }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
  providers: [SavedBuildsResolver]
})
export class SavedBuildsRoutingModule { }
