import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { ChampionsPage } from '@page/champions/champions.page';

const routes: Routes = [
  { path: 'champions', component: ChampionsPage },
  { path: '', redirectTo: '/champions', pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
