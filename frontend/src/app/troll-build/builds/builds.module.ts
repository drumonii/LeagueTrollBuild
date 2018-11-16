import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { BuildsRoutingModule } from './builds-routing.module';
import { BuildsPage } from './builds.page';
import { SavedTrollBuildComponent } from './saved-troll-build.component';
import { BuildsService } from './builds.service';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    BuildsRoutingModule
  ],
  declarations: [
    BuildsPage,
    SavedTrollBuildComponent
  ],
  providers: [
    BuildsService
  ]
})
export class BuildsModule { }
