import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ClrAlertModule, ClrIconModule } from '@clr/angular';

import { ResourcesComponent } from './resources.component';
import { ResourcesService } from './resources.service';
import { PercentagePipe } from './percentage.pipe';
import { DataSizePipe } from './data-size.pipe';

@NgModule({
  imports: [
    CommonModule,
    ClrAlertModule,
    ClrIconModule
  ],
  declarations: [
    ResourcesComponent,
    PercentagePipe,
    DataSizePipe
  ],
  exports: [
    ResourcesComponent
  ],
  providers: [
    ResourcesService
  ]
})
export class ResourcesModule { }
