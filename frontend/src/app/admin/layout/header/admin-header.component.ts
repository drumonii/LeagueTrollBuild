import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { AdminAuthService } from '@security/admin-auth.service';

@Component({
  selector: 'ltb-admin-header',
  templateUrl: './admin-header.component.html',
  styleUrls: ['./admin-header.component.scss']
})
export class AdminHeaderComponent implements OnInit {

  header = 'League Troll Build Admin';
  isAdminCollapsed: boolean;

  constructor(private authService: AdminAuthService, private router: Router) {}

  ngOnInit() {
  }

  toggleAdminCollapse(): void {
    this.isAdminCollapsed = !this.isAdminCollapsed;
  }

  logout(): void {
    // TODO: Call auth service logout and redirect to /login on successful logout
  }

}
