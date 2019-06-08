import { ChangeDetectionStrategy, Component, Input, OnInit } from '@angular/core';

import { Champion } from '@ltb-model/champion';

@Component({
  selector: 'ltb-champion',
  templateUrl: './champion.component.html',
  styleUrls: ['./champion.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ChampionComponent implements OnInit {

  @Input()
  champion: Champion;

  constructor() { }

  ngOnInit() {
  }

}
