import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AdminLoginRoutingModule } from './admin-login-routing.module';

import { AdminLoginPage } from './admin-login.page';

@NgModule({
  imports: [
    CommonModule,
    AdminLoginRoutingModule
  ],
  declarations: [
    AdminLoginPage
  ]
})
export class AdminLoginModule { }
