import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ServletErrorComponent } from './servlet-error.component';
import { ServletErrorService } from './servlet-error.service';

@NgModule({
  imports: [
    CommonModule
  ],
  declarations: [
    ServletErrorComponent
  ],
  exports: [
    ServletErrorComponent
  ],
  providers: [
    ServletErrorService
  ]
})
export class ServletErrorModule { }
