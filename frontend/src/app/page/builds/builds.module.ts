import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { BuildsRoutingModule } from './builds-routing.module';

import { BuildsPage } from './builds.page';
import { BuildsService } from '@service/builds.service';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    BuildsRoutingModule
  ],
  declarations: [
    BuildsPage
  ],
  providers: [
    BuildsService
  ]
})
export class BuildsModule { }
