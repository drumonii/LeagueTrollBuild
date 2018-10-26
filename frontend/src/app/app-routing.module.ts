import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

// See TrollBuildAppRoutingModule and AdminAppRoutingModule for routes
const routes: Routes = [];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
