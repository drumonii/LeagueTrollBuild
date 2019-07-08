import { Component, OnInit, ViewEncapsulation } from '@angular/core';

import { AdminTitleService } from '@admin-service/admin-title.service';

@Component({
  selector: 'ltb-admin-home',
  templateUrl: './admin-home.page.html',
  styleUrls: ['./admin-home.page.scss'],
  encapsulation: ViewEncapsulation.None
})
export class AdminHomePage implements OnInit {

  constructor(private titleService: AdminTitleService) {}

  ngOnInit() {
    this.setTitle();
  }

  private setTitle(): void {
    this.titleService.resetTitle();
  }

}
