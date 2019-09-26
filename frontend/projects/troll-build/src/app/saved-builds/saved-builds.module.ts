import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { NbAlertModule, NbButtonModule, NbSelectModule } from '@nebular/theme';

import { SavedBuildsRoutingModule } from './saved-builds-routing.module';
import { SavedTrollBuildModule } from './saved-troll-build.module';
import { SavedBuildsPage } from './saved-builds.page';
import { SavedBuildsService } from './saved-builds.service';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    NbAlertModule,
    NbButtonModule,
    NbSelectModule,
    SavedBuildsRoutingModule,
    SavedTrollBuildModule
  ],
  declarations: [
    SavedBuildsPage
  ],
  providers: [
    SavedBuildsService
  ]
})
export class SavedBuildsModule { }
