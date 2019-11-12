import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';

import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { AppHealth, AppHealthService } from './app-health.service';

@Component({
  selector: 'ltb-admin-app-health',
  templateUrl: './app-health.component.html',
  styleUrls: ['./app-health.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AppHealthComponent implements OnInit {

  gettingAppHealth: boolean;
  appHealth$: Observable<AppHealth>;

  constructor(private appHealthService: AppHealthService) {}

  ngOnInit() {
    this.getAppHealth();
  }

  getAppHealth(): void {
    this.gettingAppHealth = true;
    this.appHealth$ = this.appHealthService.getAppHealth()
      .pipe(
        finalize(() => this.gettingAppHealth = false)
      );
  }

}
