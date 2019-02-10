import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';

import { of } from 'rxjs';
import { switchMap } from 'rxjs/operators';

import { TitleService } from '@service/title.service';
import { AdminAuthService } from '@security/admin-auth.service';
import { AdminLoginStatus } from '@security/admin-login-response';

import { AdminLoginState } from './admin-login-state';

@Component({
  selector: 'ltb-admin-login',
  templateUrl: './admin-login.page.html',
  styleUrls: ['./admin-login.page.scss']
})
export class AdminLoginPage implements OnInit {

  adminLoginForm = this.fb.group({
    username: ['', Validators.required],
    password: ['', Validators.required]
  });

  adminLoginState = AdminLoginState.UNKNOWN;

  constructor(private authService: AdminAuthService, private fb: FormBuilder, private titleService: TitleService,
              private route: ActivatedRoute, private router: Router) {}

  ngOnInit() {
    this.setTitle();
    this.isLoggedOut();
  }

  private setTitle() {
    this.titleService.setTitle('Admin Login');
  }

  private isLoggedOut() {
    this.route.queryParamMap
      .pipe(
        switchMap((params: ParamMap) => of(!!params.get('logout') || false))
      )
      .subscribe(isLoggedOut => {
        if (isLoggedOut) {
          this.adminLoginState = AdminLoginState.LOGGED_OUT;
        }
      });
  }

  onSubmit(): void {
    if (this.adminLoginForm.valid) {
      this.beforeLogIn();
      this.authService.loginAdmin(this.adminLoginForm.get('username').value, this.adminLoginForm.get('password').value)
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
        });
    }
  }

  private beforeLogIn(): void {
    this.adminLoginState = AdminLoginState.LOGGING_IN;
    this.router.navigate(['/admin/login']); // rid of query params
  }

}
