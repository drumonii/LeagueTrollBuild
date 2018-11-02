import { Component, Input, OnInit } from '@angular/core';
import { Build } from '@model/build';

@Component({
  selector: 'ltb-saved-troll-build',
  templateUrl: './saved-troll-build.component.html',
  styleUrls: ['./saved-troll-build.component.scss']
})
export class SavedTrollBuildComponent implements OnInit {

  @Input()
  build: Build;

  constructor() { }

  ngOnInit() {
  }

}
