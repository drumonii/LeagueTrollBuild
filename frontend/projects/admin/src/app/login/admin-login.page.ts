import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';

import { ClrLoadingState } from '@clr/angular';

import { of, Subscription } from 'rxjs';
import { switchMap } from 'rxjs/operators';

import { AdminTitleService } from '@admin-service/admin-title.service';
import { AdminAuthService } from '@admin-security/admin-auth.service';
import { AdminLoginStatus } from '@admin-security/admin-login-response';
import { AdminLoginState } from './admin-login-state';

@Component({
  selector: 'ltb-admin-login',
  templateUrl: './admin-login.page.html',
  styleUrls: ['./admin-login.page.scss']
})
export class AdminLoginPage implements OnInit, OnDestroy {

  adminLoginForm = this.fb.group({
    username: ['', Validators.required],
    password: ['', Validators.required]
  });

  adminLoginState = AdminLoginState.UNKNOWN;

  private subscriptions = new Subscription();

  constructor(private authService: AdminAuthService, private fb: FormBuilder, private titleService: AdminTitleService,
              private route: ActivatedRoute, private router: Router) {}

  ngOnInit() {
    this.setTitle();
    this.isLoggedOut();
  }

  private setTitle(): void {
    this.titleService.setTitle('Admin Login');
  }

  private isLoggedOut(): void {
    this.subscriptions.add(this.route.queryParamMap
      .pipe(
        switchMap((params: ParamMap) => of(!!params.get('logout') || false))
      )
      .subscribe(isLoggedOut => {
        if (isLoggedOut) {
          this.adminLoginState = AdminLoginState.LOGGED_OUT;
        }
      }));
  }

  onSubmit(): void {
    if (this.adminLoginForm.valid) {
      this.beforeLogIn();
      const username = this.adminLoginForm.get('username').value;
      const password = this.adminLoginForm.get('password').value;
      this.subscriptions.add(this.authService.loginAdmin(username, password)
        .subscribe((loginResponse) => {
          if (loginResponse) {
            if (loginResponse.status === AdminLoginStatus.SUCCESS) {
              this.router.navigate(['/admin']);
            } else if (loginResponse.status === AdminLoginStatus.FAILED) {
              this.adminLoginState = AdminLoginState.BAD_CREDENTIALS;
            }
          } else {
            this.adminLoginState = AdminLoginState.UNEXPECTED_ERROR;
          }
        }));
    }
  }

  private beforeLogIn(): void {
    this.adminLoginState = AdminLoginState.LOGGING_IN;
    this.router.navigate(['/admin/login']); // rid of query params
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

}
