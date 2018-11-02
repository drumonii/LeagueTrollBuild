import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { AdminAppComponent } from './admin-app.component';

const routes: Routes = [
  {
    path: 'admin',
    component: AdminAppComponent,
    children: [
      { path: '', pathMatch: 'full', loadChildren: './home/admin-home.module#AdminHomeModule' },
      { path: 'login', loadChildren: './login/admin-login.module#AdminLoginModule' }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminAppRoutingModule { }
