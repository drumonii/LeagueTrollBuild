import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { ChampionRoutingModule } from './champion-routing.module';

import { ChampionPage } from './champion.page';
import { BuildsService } from '@service/builds.service';
import { ChampionsService } from '@service/champions.service';
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
    ChampionsService,
    GameMapsService
  ]
})
export class ChampionModule { }
