import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';

import { TitleService } from '@service/title.service';
import { AdminAuthService } from '@security/admin-auth.service';

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
      // TODO: Check authentication with auth service
      this.router.navigate(['/admin']);
    }
  }

}
