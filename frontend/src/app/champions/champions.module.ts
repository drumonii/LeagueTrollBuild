import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { ChampionsRoutingModule } from './champions-routing.module';

import { ChampionsNameFilterPipe } from './champions-name-filter.pipe';
import { ChampionsTagsFilterPipe } from './champions-tags-filter.pipe';

import { ChampionsPage } from './champions.page';
import { ChampionsService } from './champions.service';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ChampionsRoutingModule,
  ],
  declarations: [
    ChampionsNameFilterPipe,
    ChampionsTagsFilterPipe,
    ChampionsPage
  ],
  providers: [
    ChampionsService
  ]
})
export class ChampionsModule { }
