import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HTTP_INTERCEPTORS, } from '@angular/common/http';

import { LoadingBarHttpClientModule } from '@ngx-loading-bar/http-client';

import { AdminAppComponent } from './admin-app.component';
import { AdminAppRoutingModule } from './admin-app-routing.module';

import { AdminLayoutModule } from '@admin-layout//admin-layout.module';

import { AdminGuard } from '@admin-guard/admin.guard';
import { AdminAlreadyLoggedInGuard } from '@admin-guard/admin-already-logged-in.guard';
import { AdminBasePathHttpInterceptor } from '@admin-interceptor/admin-base-path.http-interceptor';
import { AdminErrorHttpInterceptor } from '@admin-interceptor/admin-error.http-interceptor';

@NgModule({
  declarations: [
    AdminAppComponent
  ],
  imports: [
    CommonModule,
    LoadingBarHttpClientModule,
    AdminAppRoutingModule,
    AdminLayoutModule
  ],
  providers: [
    AdminGuard,
    AdminAlreadyLoggedInGuard,
    { provide: HTTP_INTERCEPTORS, useClass: AdminBasePathHttpInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: AdminErrorHttpInterceptor, multi: true }
  ]
})
export class AdminAppModule { }
