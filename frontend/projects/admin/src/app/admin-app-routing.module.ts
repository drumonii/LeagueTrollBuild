import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { AdminGuard } from '@admin-guard/admin.guard';
import { AdminAlreadyLoggedInGuard } from '@admin-guard/admin-already-logged-in.guard';

const routes: Routes = [
  {
    path: 'admin',
    pathMatch: 'full',
    canActivate: [AdminGuard],
    loadChildren: () => import('./home/admin-home.module').then(m => m.AdminHomeModule)
  },
  {
    path: 'admin/login',
    canActivate: [AdminAlreadyLoggedInGuard],
    loadChildren: () => import('./login/admin-login.module').then(m => m.AdminLoginModule)
  },
  {
    path: 'admin/batch',
    canActivate: [AdminGuard],
    loadChildren: () => import('./batch/admin-batch.module').then(m => m.AdminBatchModule)
  },
  {
    path: 'admin/flyway',
    canActivate: [AdminGuard],
    loadChildren: () => import('./flyway/flyway.module').then(m => m.FlywayModule)
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AdminAppRoutingModule { }
