import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { Observable, Subscription } from 'rxjs';

import { AdminAuthService } from '@admin-security/admin-auth.service';
import { AdminUserDetails } from '@admin-security/admin-user-details';

@Component({
  selector: 'ltb-admin-header',
  templateUrl: './admin-header.component.html',
  styleUrls: ['./admin-header.component.scss']
})
export class AdminHeaderComponent implements OnInit, OnDestroy {

  header = 'League Troll Build Admin';

  adminUserDetails$: Observable<AdminUserDetails>;

  private subscriptions = new Subscription();

  constructor(private authService: AdminAuthService, private router: Router) {}

  ngOnInit() {
    this.adminUserDetails$ = this.authService.adminUserDetails;
  }

  logout(): void {
    this.subscriptions.add(this.authService.logoutAdmin()
      .subscribe(() => this.router.navigate(['/admin/login'], { queryParams: { logout: true } })));
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

}
