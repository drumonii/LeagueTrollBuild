import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { BuildsRoutingModule } from './builds-routing.module';

import { BuildsPage } from './builds.page';
import { TitleService } from '@service/title.service';
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
    TitleService,
    BuildsService
  ]
})
export class BuildsModule { }
