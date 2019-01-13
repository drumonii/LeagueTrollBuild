import { Component, OnInit } from '@angular/core';

import { forkJoin, Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { GlobalErrorService } from './global-error.service';

@Component({
  selector: 'ltb-admin-global-error',
  templateUrl: './global-error.component.html',
  styleUrls: ['./global-error.component.scss']
})
export class GlobalErrorComponent implements OnInit {

  globalErrors$: Observable<any>;
  gettingGlobalErrors: boolean;

  constructor(private service: GlobalErrorService) {}

  ngOnInit() {
    this.getGlobalErrors();
  }

  getGlobalErrors(): void {
    this.gettingGlobalErrors = true;
    this.globalErrors$ = forkJoin(this.service.getGlobalErrors())
      .pipe(
        finalize(() => this.gettingGlobalErrors = false)
      );
  }

  getGlobalErrorsClass(servletErrors: number): string {
    if (servletErrors > 0) {
      return 'has-text-danger';
    }
    return 'has-text-black';
  }

}
