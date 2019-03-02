import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { LazyLoadImgDirective } from '@ltb-directive/lazy-load-img.directive';

@NgModule({
  imports: [
    CommonModule
  ],
  declarations: [
    LazyLoadImgDirective
  ],
  exports: [
    LazyLoadImgDirective
  ]
})
export class LazyLoadImgModule { }
