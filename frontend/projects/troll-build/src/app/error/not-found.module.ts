import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { NbIconModule } from '@nebular/theme';

import { NotFoundRoutingModule } from './not-found-routing.module';
import { NotFoundPage } from './not-found.page';

@NgModule({
  imports: [
    CommonModule,
    NbIconModule,
    NotFoundRoutingModule
  ],
  declarations: [
    NotFoundPage
  ]
})
export class NotFoundModule { }
