import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { NbButtonModule, NbIconModule, NbInputModule, NbSpinnerModule, NbTooltipModule } from '@nebular/theme';

import { TrollBuildComponent } from './troll-build.component';

@NgModule({
  imports: [
    CommonModule,
    NbButtonModule,
    NbIconModule,
    NbInputModule,
    NbSpinnerModule,
    NbTooltipModule
  ],
  declarations: [
    TrollBuildComponent
  ],
  exports: [
    TrollBuildComponent
  ]
})
export class TrollBuildModule { }
