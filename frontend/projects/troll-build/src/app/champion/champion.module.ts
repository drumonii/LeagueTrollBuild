import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { NbAlertModule, NbButtonModule, NbSelectModule, NbSpinnerModule } from '@nebular/theme';

import { ChampionRoutingModule } from './champion-routing.module';
import { TrollBuildModule } from './troll-build.module';
import { ChampionPage } from './champion.page';
import { ChampionService } from './champion.service';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    NbAlertModule,
    NbButtonModule,
    NbSelectModule,
    NbSpinnerModule,
    ChampionRoutingModule,
    TrollBuildModule
  ],
  declarations: [
    ChampionPage,
  ],
  providers: [
    ChampionService
  ]
})
export class ChampionModule { }
