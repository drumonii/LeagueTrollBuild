import { Injectable } from '@angular/core';

import { BehaviorSubject } from 'rxjs';

/**
 * Slim loading bar service heavily inspired by ngx-loading-bar simplified for LTB's needs.
 */
@Injectable({
  providedIn: 'root'
})
export class LoadingBarService {

  private progress$ = new BehaviorSubject<number>(1);

  private pendingRequests = 0;
  private incTimeout: any;

  get progress() {
    return this.progress$;
  }

  start(): void {
    this.pendingRequests++;
    if (this.progress$.value === 0 || this.pendingRequests === 1) {
      this.set(1);
    }
  }

  complete(): void {
    if (this.pendingRequests > 0) {
      this.pendingRequests--;
      if (this.pendingRequests === 0) {
        this.set(100);
        // Attempt to aggregate any start/complete calls within 500ms:
        setTimeout(() => this.set(0), 500);
      }
    }
  }

  private set(progress: number) {
    this.progress$.next(progress);
    if (this.pendingRequests > 0 && this.progress$.value > 0 && this.progress$.value < 100) {

      // increment progress to give the illusion that there is loading but make sure to cancel the previous timeouts so
      // we don't have multiple incs running at the same time.
      clearTimeout(this.incTimeout);
      this.incTimeout = setTimeout(() => this.increment(), 250);
    }
  }

  private increment() {
    let rnd = 0;

    // increments the progress by a large random amount initially and will slow down as it progresses
    if (this.progress$.value >= 0 && this.progress$.value < 25) {
      // increment between 0 - 6%
      rnd = Math.random() * 6;
    } else if (this.progress$.value >= 25 && this.progress$.value < 65) {
      // increment between 0 - 3%
      rnd = Math.random() * 3;
    } else if (this.progress$.value >= 65 && this.progress$.value < 90) {
      // increment between 0 - 2%
      rnd = Math.random() * 2;
    } else if (this.progress$.value >= 90 && this.progress$.value < 99) {
      // finally, increment by 0.5%
      rnd = 0.5;
    }

    this.set(this.progress$.value + rnd);
  }

}
