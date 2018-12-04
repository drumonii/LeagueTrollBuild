import { Component, OnInit } from '@angular/core';

import { forkJoin, Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ServletErrorService } from './servlet-error.service';

@Component({
  selector: 'ltb-admin-servlet-error',
  templateUrl: './servlet-error.component.html',
  styleUrls: ['./servlet-error.component.scss']
})
export class ServletErrorComponent implements OnInit {

  servletErrors$: Observable<any>;
  gettingServletErrors: boolean;

  constructor(private service: ServletErrorService) {}

  ngOnInit() {
    this.getServletErrors();
  }

  getServletErrors(): void {
    this.gettingServletErrors = true;
    this.servletErrors$ = forkJoin(this.service.getServletErrors())
      .pipe(
        finalize(() => this.gettingServletErrors = false)
      );
  }

  getServletErrorsClass(servletErrors: number): string {
    if (servletErrors > 0) {
      return 'has-text-danger';
    }
    return 'has-text-black';
  }

}
