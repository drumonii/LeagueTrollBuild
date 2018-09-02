import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HTTP_INTERCEPTORS } from '@angular/common/http';

import { LoadingBarComponent } from './loading-bar.component';
import { LoadingBarInterceptor } from './loading-bar.interceptor';
import { LoadingBarService } from './loading-bar.service';

@NgModule({
  imports: [
    CommonModule
  ],
  declarations: [
    LoadingBarComponent
  ],
  exports: [
    LoadingBarComponent
  ],
  providers: [
    LoadingBarService,
    { provide: HTTP_INTERCEPTORS, useClass: LoadingBarInterceptor, multi: true },
  ]
})
export class LoadingBarModule { }
