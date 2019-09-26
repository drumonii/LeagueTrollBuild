import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { NbButtonModule, NbIconModule, NbInputModule, NbSpinnerModule, NbTooltipModule } from '@nebular/theme';

import { SavedTrollBuildComponent } from './saved-troll-build.component';

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
    SavedTrollBuildComponent
  ],
  exports: [
    SavedTrollBuildComponent
  ]
})
export class SavedTrollBuildModule { }
