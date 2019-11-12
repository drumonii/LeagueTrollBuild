import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';

import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { HttpStats, HttpStatsService } from './http-stats.service';

@Component({
  selector: 'ltb-admin-http-stats',
  templateUrl: './http-stats.component.html',
  styleUrls: ['./http-stats.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class HttpStatsComponent implements OnInit {

  gettingHttpStats: boolean;
  httpStats$: Observable<HttpStats>;

  constructor(private httpStatsService: HttpStatsService) {}

  ngOnInit() {
    this.getHttpStats();
  }

  getHttpStats() {
    this.gettingHttpStats = true;
    this.httpStats$ = this.httpStatsService.getHttpStats()
      .pipe(
        finalize(() => this.gettingHttpStats = false)
      );
  }

}
