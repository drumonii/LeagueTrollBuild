import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AdminFooterComponent } from '@admin-layout/footer/admin-footer.component';

@NgModule({
  imports: [
    CommonModule
  ],
  declarations: [
    AdminFooterComponent
  ],
  exports: [
    AdminFooterComponent
  ]
})
export class AdminFooterModule { }
