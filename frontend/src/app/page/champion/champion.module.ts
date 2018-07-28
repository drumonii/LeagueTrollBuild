import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { ChampionRoutingModule } from './champion-routing.module';

import { ChampionPage } from './champion.page';
import { BuildsService } from '@service/builds.service';
import { ChampionService } from '@service/champion.service';
import { GameMapsService } from '@service/game-maps.service';

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
    BuildsService,
    ChampionService,
    GameMapsService
  ]
})
export class ChampionModule { }