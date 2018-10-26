import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { TrollBuildAppComponent } from './troll-build-app.component';

// components in troll-build folder have ltb alias and admin have admin alias
import { TrollBuildAppRoutingModule } from './troll-build-app-routing.module';

import { LoadingBarModule } from '@loading-bar/loading-bar.module';
import { LayoutModule } from '@ltb-layout/layout.module';

@NgModule({
  declarations: [
    TrollBuildAppComponent
  ],
  imports: [
    CommonModule,
    TrollBuildAppRoutingModule,
    LoadingBarModule,
    LayoutModule
  ]
})
export class TrollBuildAppModule { }
