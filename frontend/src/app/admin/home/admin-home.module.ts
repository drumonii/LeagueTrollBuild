import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AdminHomeRoutingModule } from './admin-home-routing.module';

import { AdminHomePage } from './admin-home.page';

@NgModule({
  imports: [
    CommonModule,
    AdminHomeRoutingModule
  ],
  declarations: [
    AdminHomePage
  ],
})
export class AdminHomeModule { }
