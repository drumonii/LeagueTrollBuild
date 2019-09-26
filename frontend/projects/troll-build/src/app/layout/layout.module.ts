import { NgModule } from '@angular/core';

import { FooterModule } from '@ltb-layout/footer/footer.module';
import { HeaderModule } from '@ltb-layout/header/header.module';

@NgModule({
  exports: [
    FooterModule,
    HeaderModule
  ]
})
export class LayoutModule { }
