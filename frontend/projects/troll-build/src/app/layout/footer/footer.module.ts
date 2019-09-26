import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { NbIconModule } from '@nebular/theme';

import { FooterComponent } from './footer.component';
import { VersionsService } from './versions.service';

@NgModule({
  imports: [
    CommonModule,
    NbIconModule
  ],
  declarations: [
    FooterComponent
  ],
  exports: [
    FooterComponent
  ],
  providers: [
    VersionsService
  ]
})
export class FooterModule { }
