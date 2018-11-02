import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { ChampionRoutingModule } from './champion-routing.module';

import { ChampionPage } from './champion.page';
import { TrollBuildComponent } from './troll-build.component';
import { ChampionService } from './champion.service';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ChampionRoutingModule
  ],
  declarations: [
    ChampionPage,
    TrollBuildComponent
  ],
  providers: [
    ChampionService
  ]
})
export class ChampionModule { }
