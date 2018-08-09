import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { ChampionsRoutingModule } from './champions-routing.module';

import { ChampionsNameFilterPipe } from '@pipe/champions-name-filter.pipe';
import { ChampionsTagsFilterPipe } from '@pipe/champions-tags-filter.pipe';

import { ChampionsPage } from './champions.page';
import { TitleService } from '@service/title.service';
import { ChampionsService } from '@service/champions.service';
import { GameMapsService } from '@service/game-maps.service';

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
    TitleService,
    ChampionsService,
    GameMapsService
  ]
})
export class ChampionsModule { }
