import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { SavedBuildsRoutingModule } from './saved-builds-routing.module';
import { SavedBuildsPage } from './saved-builds.page';
import { SavedTrollBuildComponent } from './saved-troll-build.component';
import { SavedBuildsService } from './saved-builds.service';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    SavedBuildsRoutingModule
  ],
  declarations: [
    SavedBuildsPage,
    SavedTrollBuildComponent
  ],
  providers: [
    SavedBuildsService
  ]
})
export class SavedBuildsModule { }
