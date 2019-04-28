import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';

import { forkJoin, Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { MemoryUsageService } from './memory-usage.service';

@Component({
  selector: 'ltb-admin-memory-usage',
  templateUrl: './memory-usage.component.html',
  styleUrls: ['./memory-usage.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class MemoryUsageComponent implements OnInit {

  memoryUsage$: Observable<any>;
  gettingMemoryUsage: boolean;

  constructor(private service: MemoryUsageService) {}

  ngOnInit() {
    this.getMemoryUsage();
  }

  getMemoryUsage(): void {
    this.gettingMemoryUsage = true;
    this.memoryUsage$ = forkJoin(this.service.getMemoryUsage())
      .pipe(
        finalize(() => this.gettingMemoryUsage = false)
      );
  }

  getMemoryUsageClass(memoryUsage: number): string {
    if (memoryUsage >= 512) {
      return 'has-text-danger';
    } else if (memoryUsage >= 400) {
      return 'has-text-warning';
    }
    return 'has-text-black';
  }

}
