import { Component, OnInit } from '@angular/core';

import { Observable } from 'rxjs/Observable';

import { ChampionsService } from '@service/champions.service';
import { Champion } from '@model/champion';

@Component({
  selector: 'ltb-champions',
  templateUrl: './champions.page.html',
  styleUrls: ['./champions.page.scss']
})
export class ChampionsPage implements OnInit {

  champions$: Observable<Champion[]>;
  championsSearchName: string;

  constructor(private championsService: ChampionsService) {}

  ngOnInit() {
    this.getChampions();
  }

  getChampions(): void {
    this.champions$ = this.championsService.getChampions();
  }

}
