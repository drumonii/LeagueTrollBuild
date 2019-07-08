import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

import { ClrDropdownModule, ClrNavigationModule } from '@clr/angular';

import { AdminHeaderComponent } from '@admin-layout/header/admin-header.component';

@NgModule({
  imports: [
    CommonModule,
    RouterModule,
    ClrDropdownModule,
    ClrNavigationModule
  ],
  declarations: [
    AdminHeaderComponent
  ],
  exports: [
    AdminHeaderComponent
  ]
})
export class AdminHeaderModule { }
