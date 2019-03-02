import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { AdminAppComponent } from './admin-app.component';

import { AdminGuard } from '@admin-guard/admin.guard';
import { AdminAlreadyLoggedInGuard } from '@admin-guard/admin-already-logged-in.guard';

const routes: Routes = [
  {
    path: 'admin',
    component: AdminAppComponent,
    children: [
      { path: '', pathMatch: 'full', canActivate: [AdminGuard], loadChildren: './home/admin-home.module#AdminHomeModule' },
      { path: 'login', canActivate: [AdminAlreadyLoggedInGuard], loadChildren: './login/admin-login.module#AdminLoginModule' },
      { path: 'batch', canActivate: [AdminGuard], loadChildren: './batch/admin-batch.module#AdminBatchModule' },
      { path: 'flyway', canActivate: [AdminGuard], loadChildren: './flyway/flyway.module#FlywayModule' },
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminAppRoutingModule { }
