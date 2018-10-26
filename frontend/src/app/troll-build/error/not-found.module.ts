import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { NotFoundRoutingModule } from './not-found-routing.module';

import { NotFoundPage } from './not-found.page';

@NgModule({
  imports: [
    CommonModule,
    NotFoundRoutingModule
  ],
  declarations: [
    NotFoundPage
  ]
})
export class NotFoundModule { }
