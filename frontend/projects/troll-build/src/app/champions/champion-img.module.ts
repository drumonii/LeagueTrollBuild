import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

import { ChampionImgComponent } from './champion-img.component';
import { LazyLoadImgModule } from '@ltb-directive/lazy-load-img.module';

@NgModule({
  imports: [
    CommonModule,
    RouterModule,
    LazyLoadImgModule
  ],
  declarations: [
    ChampionImgComponent
  ],
  exports: [
    ChampionImgComponent
  ]
})
export class ChampionImgModule { }
