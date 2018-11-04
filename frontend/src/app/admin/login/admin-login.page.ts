import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';

import { finalize } from 'rxjs/operators';

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
    private router: Router) {}

  ngOnInit() {
    this.setTitle();
  }

  private setTitle() {
    this.titleService.setTitle('Admin Login');
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
  }

}
