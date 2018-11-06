import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';

import { of } from 'rxjs';
import { finalize, switchMap } from 'rxjs/operators';

import { TitleService } from '@service/title.service';
import { AdminAuthService } from '@security/admin-auth.service';
import { AdminLoginStatus } from '@security/admin-login-response';

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

  isLoggingIn: boolean;
  badCredentials: boolean;
  unexpectedError: boolean;
  loggedOut: boolean;

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
      .subscribe(isLoggedOut => this.loggedOut = isLoggedOut);
  }

  onSubmit(): void {
    if (this.adminLoginForm.valid) {
      this.isLoggingIn = true;
      this.resetAlerts();
      this.authService.loginAdmin(this.adminLoginForm.get('username').value, this.adminLoginForm.get('password').value)
        .pipe(
          finalize(() => this.isLoggingIn = false)
        )
        .subscribe((loginResponse) => {
          if (loginResponse) {
            if (loginResponse.status === AdminLoginStatus.SUCCESS) {
              this.router.navigate(['/admin']);
            } else if (loginResponse.status === AdminLoginStatus.FAILED) {
              this.badCredentials = true;
            }
          } else {
            this.unexpectedError = true;
          }
        });
    }
  }

  private resetAlerts(): void {
    this.badCredentials = false;
    this.unexpectedError = false;
    this.loggedOut = false;
    this.router.navigate(['/admin/login']); // rid of query params
  }

}
