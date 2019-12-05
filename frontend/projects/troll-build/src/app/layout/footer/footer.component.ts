import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';

import { Observable } from 'rxjs';

import { Version } from '@ltb-model/version';
import { VersionsService } from './versions.service';

@Component({
  selector: 'ltb-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
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
