import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { NbAlertModule, NbButtonModule, NbIconModule, NbInputModule } from '@nebular/theme';

import { ChampionsRoutingModule } from './champions-routing.module';
import { ChampionImgModule } from './champion-img.module';
import { ChampionsNameFilterPipe } from './champions-name-filter.pipe';
import { ChampionsTagsFilterPipe } from './champions-tags-filter.pipe';
import { ChampionsPage } from './champions.page';
import { ChampionsService } from './champions.service';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    NbAlertModule,
    NbButtonModule,
    NbIconModule,
    NbInputModule,
    ChampionsRoutingModule,
    ChampionImgModule
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
