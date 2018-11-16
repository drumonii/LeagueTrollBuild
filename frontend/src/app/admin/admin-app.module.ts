import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AdminAppComponent } from './admin-app.component';
import { AdminAppRoutingModule } from './admin-app-routing.module';

import { AdminLayoutModule } from '@admin-layout//admin-layout.module';
import { LoadingBarModule } from '@loading-bar/loading-bar.module';

import { AdminGuard } from '@guard/admin.guard';
import { AdminAlreadyLoggedInGuard } from '@guard/admin-already-logged-in.guard';

@NgModule({
  declarations: [
    AdminAppComponent
  ],
  imports: [
    CommonModule,
    AdminAppRoutingModule,
    LoadingBarModule,
    AdminLayoutModule
  ],
  providers: [
    AdminGuard,
    AdminAlreadyLoggedInGuard
  ]
})
export class AdminAppModule { }
