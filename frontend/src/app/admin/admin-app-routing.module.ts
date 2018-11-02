import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { AdminAppComponent } from './admin-app.component';

import { AdminGuard } from '@guard/admin.guard';
import { AdminAlreadyLoggedInGuard } from '@guard/admin-already-logged-in.guard';

const routes: Routes = [
  {
    path: 'admin',
    component: AdminAppComponent,
    children: [
      { path: '', pathMatch: 'full', canActivate: [AdminGuard], loadChildren: './home/admin-home.module#AdminHomeModule' },
      { path: 'login', canActivate: [AdminAlreadyLoggedInGuard], loadChildren: './login/admin-login.module#AdminLoginModule' }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminAppRoutingModule { }
