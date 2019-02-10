import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { Observable } from 'rxjs';

import { AdminAuthService } from '@security/admin-auth.service';
import { AdminUserDetails } from '@security/admin-user-details';

@Component({
  selector: 'ltb-admin-header',
  templateUrl: './admin-header.component.html',
  styleUrls: ['./admin-header.component.scss']
})
export class AdminHeaderComponent implements OnInit {

  header = 'League Troll Build Admin';
  isAdminCollapsed: boolean;

  adminUserDetails$: Observable<AdminUserDetails>;

  constructor(private authService: AdminAuthService, private router: Router) {}

  ngOnInit() {
    this.adminUserDetails$ = this.authService.adminUserDetails;
  }

  toggleAdminCollapse(): void {
    this.isAdminCollapsed = !this.isAdminCollapsed;
  }

  logout(): void {
    this.authService.logoutAdmin()
      .subscribe(() => this.router.navigate(['/admin/login'], { queryParams: { logout: true } }));
  }

}
