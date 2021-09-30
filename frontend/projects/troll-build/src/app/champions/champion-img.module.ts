import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

import { ChampionImgComponent } from './champion-img.component';

@NgModule({
  imports: [
    CommonModule,
    RouterModule
  ],
  declarations: [
    ChampionImgComponent
  ],
  exports: [
    ChampionImgComponent
  ]
})
export class ChampionImgModule { }
