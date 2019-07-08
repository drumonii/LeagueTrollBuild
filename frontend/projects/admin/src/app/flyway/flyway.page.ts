import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';

import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { FlywayMigration } from './flyway-response';
import { FlywayService } from './flyway.service';
import { AdminTitleService } from '@admin-service/admin-title.service';

@Component({
  selector: 'ltb-admin-flyway',
  templateUrl: './flyway.page.html',
  styleUrls: ['./flyway.page.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush // to avoid ExpressionChangedAfterItHasBeenCheckedError in tests
})
export class FlywayPage implements OnInit {

  loading: boolean;

  flyway$: Observable<FlywayMigration[]>;

  constructor(private service: FlywayService, private titleService: AdminTitleService) {}

  ngOnInit() {
    this.setTitle();

    this.getFlyway();
  }

  private setTitle(): void {
    this.titleService.setTitle('Flyway Migrations');
  }

  getFlyway(): void {
    this.loading = true;
    this.flyway$ = this.service.getFlyway()
      .pipe(
        finalize(() => this.loading = false)
      );
  }

  trackByFlyway(index: number, flyway: FlywayMigration): number {
    return flyway.checksum;
  }

}
