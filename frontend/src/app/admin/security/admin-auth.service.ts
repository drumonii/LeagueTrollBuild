import { Injectable } from '@angular/core';

import { BehaviorSubject, Observable } from 'rxjs';

import { AdminUserDetails } from '@security/admin-user-details';

@Injectable({
  providedIn: 'root'
})
export class AdminAuthService {

  static adminUserDetailsKey = 'adminUserDetails';

  private adminUserDetails$ = new BehaviorSubject<AdminUserDetails>(null);

  constructor() { }

  get adminUserDetails(): Observable<AdminUserDetails> {
    this.adminUserDetails$.next(JSON.parse(localStorage.getItem(AdminAuthService.adminUserDetailsKey)));
    return this.adminUserDetails$.asObservable();
  }

}
