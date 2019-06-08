import { Component, OnInit } from '@angular/core';

import { Observable} from 'rxjs';
import { finalize } from 'rxjs/operators';

import { FlywayMigration } from './flyway-response';
import { FlywayService } from './flyway.service';
import { AdminTitleService } from '@admin-service/admin-title.service';

@Component({
  selector: 'ltb-admin-flyway',
  templateUrl: './flyway.page.html',
  styleUrls: ['./flyway.page.scss']
})
export class FlywayPage implements OnInit {

  loadingIndicator: boolean;

  rows$: Observable<FlywayMigration[]>;

  constructor(private service: FlywayService, private titleService: AdminTitleService) {}

  ngOnInit() {
    this.setTitle();

    this.getFlyway();
  }

  private setTitle() {
    this.titleService.setTitle('Flyway Migrations');
  }

  getFlyway(): void {
    this.loadingIndicator = true;
    this.rows$ = this.service.getFlyway()
      .pipe(
        finalize(() => this.loadingIndicator = false)
      );
  }

}
