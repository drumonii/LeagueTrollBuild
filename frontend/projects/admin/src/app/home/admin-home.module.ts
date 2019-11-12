import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AdminHomeRoutingModule } from './admin-home-routing.module';
import { AdminHomePage } from './admin-home.page';
import { AppHealthModule } from './dashboard/app/app-health.module';
import { EnvModule } from './dashboard/env/env.module';
import { HttpStatsModule } from './dashboard/http/http-stats.module';
import { ResourcesModule } from './dashboard/resources/resources.module';

@NgModule({
  imports: [
    CommonModule,
    AdminHomeRoutingModule,
    AppHealthModule,
    EnvModule,
    HttpStatsModule,
    ResourcesModule
  ],
  declarations: [
    AdminHomePage
  ]
})
export class AdminHomeModule { }
