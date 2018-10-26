import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

import { AdminFooterComponent } from '@admin-layout/footer/admin-footer.component';
import { AdminHeaderComponent } from '@admin-layout/header/admin-header.component';

@NgModule({
  imports: [
    CommonModule,
    RouterModule
  ],
  declarations: [
    AdminFooterComponent,
    AdminHeaderComponent
  ],
  exports: [
    AdminFooterComponent,
    AdminHeaderComponent
  ]
})
export class AdminLayoutModule { }
