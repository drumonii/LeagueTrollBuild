import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';

import { forkJoin, Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { CpuUsage, CpuUsageService } from './cpu-usage.service';

@Component({
  selector: 'ltb-admin-cpu-usage',
  templateUrl: './cpu-usage.component.html',
  styleUrls: ['./cpu-usage.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CpuUsageComponent implements OnInit {

  cpuUsage$: Observable<CpuUsage>;
  gettingCpuUsage: boolean;

  constructor(private service: CpuUsageService) {}

  ngOnInit() {
    this.getCpuUsage();
  }

  getCpuUsage(): void {
    this.gettingCpuUsage = true;
    this.cpuUsage$ = forkJoin([this.service.getCpuUsagePerc(), this.service.getCpuCount()])
      .pipe(
        finalize(() => this.gettingCpuUsage = false),
        map(([cpuUsage, cpuCount]) => ({ percentage: cpuUsage, cpus: cpuCount }))
      );
  }

  getCpuUsageClass(cpuUsagePercentage: any): string {
    if (cpuUsagePercentage >= 50) {
      return 'label-danger';
    } else if (cpuUsagePercentage >= 25) {
      return 'label-warning';
    }
    return '';
  }

}
