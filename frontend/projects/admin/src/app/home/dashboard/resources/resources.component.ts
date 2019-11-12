import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';

import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ResourceConsumption, ResourcesService } from './resources.service';

@Component({
  selector: 'ltb-admin-resources',
  templateUrl: './resources.component.html',
  styleUrls: ['./resources.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ResourcesComponent implements OnInit {

  gettingResourceConsumption: boolean;
  resourceConsumption$: Observable<ResourceConsumption>;

  constructor(private resourcesService: ResourcesService) {}

  ngOnInit() {
    this.getResourceConsumption();
  }

  getResourceConsumption(): void {
    this.gettingResourceConsumption = true;
    this.resourceConsumption$ = this.resourcesService.getResourceConsumption()
      .pipe(
        finalize(() => this.gettingResourceConsumption = false)
      );
  }

}
