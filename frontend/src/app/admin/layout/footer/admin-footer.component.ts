import { Component, OnInit } from '@angular/core';

import { Observable, of } from 'rxjs';

import * as packageJson from '../../../../../package.json';

@Component({
  selector: 'ltb-admin-footer',
  templateUrl: './admin-footer.component.html',
  styleUrls: ['./admin-footer.component.scss']
})
export class AdminFooterComponent implements OnInit {

  appVersion$: Observable<string>;

  constructor() { }

  ngOnInit() {
    this.getAppVersion();
  }

  private getAppVersion() {
    this.appVersion$ = of(packageJson.version);
  }

}
