import { Component, OnInit } from '@angular/core';

import { Observable } from 'rxjs';

import { Version } from '@model/version';
import { VersionsService } from './versions.service';

@Component({
  selector: 'ltb-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss']
})
export class FooterComponent implements OnInit {

  latestVersion$: Observable<Version>;

  constructor(private versionsService: VersionsService) {}

  ngOnInit() {
    this.getLatestVersion();
  }

  getLatestVersion(): void {
    this.latestVersion$ = this.versionsService.getLatestVersion();
  }

}