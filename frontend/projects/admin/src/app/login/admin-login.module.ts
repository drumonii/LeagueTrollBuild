import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';

import { ClrAlertModule, ClrFormsModule, ClrLoadingButtonModule, ClrLoadingModule } from '@clr/angular';

import { AdminLoginRoutingModule } from './admin-login-routing.module';
import { AdminLoginPage } from './admin-login.page';

@NgModule({
  imports: [
    CommonModule,
    ReactiveFormsModule,
    ClrAlertModule,
    ClrFormsModule,
    ClrLoadingButtonModule,
    ClrLoadingModule,
    AdminLoginRoutingModule
  ],
  declarations: [
    AdminLoginPage
  ]
})
export class AdminLoginModule { }
