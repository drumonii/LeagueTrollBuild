import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { ChampionRoutingModule } from './champion-routing.module';

import { ChampionPage } from './champion.page';
import { TitleService } from '@service/title.service';
import { ChampionService } from './champion.service';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ChampionRoutingModule
  ],
  declarations: [
    ChampionPage,
  ],
  providers: [
    TitleService,
    ChampionService
  ]
})
export class ChampionModule { }