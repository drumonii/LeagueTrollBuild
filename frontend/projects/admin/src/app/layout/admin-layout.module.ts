import { NgModule } from '@angular/core';

import { AdminFooterModule } from '@admin-layout/footer/admin-footer.module';
import { AdminHeaderModule } from '@admin-layout/header/admin-header.module';

@NgModule({
  exports: [
    AdminFooterModule,
    AdminHeaderModule
  ]
})
export class AdminLayoutModule { }
