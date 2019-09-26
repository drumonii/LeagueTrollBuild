import { ChangeDetectionStrategy, Component, Input, OnInit } from '@angular/core';

import { Champion } from '@ltb-model/champion';

@Component({
  selector: 'ltb-champion-img',
  templateUrl: './champion-img.component.html',
  styleUrls: ['./champion-img.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ChampionImgComponent implements OnInit {

  @Input()
  champion: Champion;

  constructor() { }

  ngOnInit() {
  }

}
