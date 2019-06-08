import { ChangeDetectionStrategy, Component, Input, OnInit } from '@angular/core';
import { Build } from '@ltb-model/build';

@Component({
  selector: 'ltb-saved-troll-build',
  templateUrl: './saved-troll-build.component.html',
  styleUrls: ['./saved-troll-build.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class SavedTrollBuildComponent implements OnInit {

  @Input()
  build: Build;

  constructor() { }

  ngOnInit() {
  }

}
