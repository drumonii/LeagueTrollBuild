import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';

import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { EnvInfo, EnvService } from './env.service';

@Component({
  selector: 'ltb-admin-env',
  templateUrl: './env.component.html',
  styleUrls: ['./env.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class EnvComponent implements OnInit {

  gettingEnv: boolean;
  env$: Observable<EnvInfo>;

  constructor(private envService: EnvService) {}

  ngOnInit() {
    this.getEnv();
  }

  getEnv(): void {
    this.gettingEnv  = true;
    this.env$ = this.envService.getEnv()
      .pipe(
        finalize(() => this.gettingEnv = false)
      );
  }

}
