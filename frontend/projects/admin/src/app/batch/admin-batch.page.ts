import { Component, OnInit } from '@angular/core';

import { AdminTitleService } from '@admin-service/admin-title.service';

@Component({
  selector: 'ltb-admin-batch',
  templateUrl: './admin-batch.page.html',
  styleUrls: ['./admin-batch.page.scss']
})
export class AdminBatchPage implements OnInit {

  constructor(private titleService: AdminTitleService) {}

  ngOnInit() {
    this.setTitle();
  }

  private setTitle(): void {
    this.titleService.setTitle('Batch Jobs');
  }

}
