import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { ChampionsRoutingModule } from './champions-routing.module';
import { ChampionsNameFilterPipe } from './champions-name-filter.pipe';
import { ChampionsTagsFilterPipe } from './champions-tags-filter.pipe';
import { ChampionsPage } from './champions.page';
import { ChampionsService } from './champions.service';
import { ChampionComponent } from './champion.component';

import { LazyLoadImgModule } from '@ltb-directive/lazy-load-img.module';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ChampionsRoutingModule,
    LazyLoadImgModule
  ],
  declarations: [
    ChampionsNameFilterPipe,
    ChampionsTagsFilterPipe,
    ChampionsPage,
    ChampionComponent
  ],
  providers: [
    ChampionsService
  ]
})
export class ChampionsModule { }
