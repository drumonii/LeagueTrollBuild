import { Component, OnInit } from '@angular/core';

import { ActivatedRoute, ParamMap } from '@angular/router';

import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/switchMap';

import { ChampionsService } from '@service/champions.service';
import { Champion } from '@model/champion';

@Component({
  selector: 'ltb-champion',
  templateUrl: './champion.page.html',
  styleUrls: ['./champion.page.scss']
})
export class ChampionPage implements OnInit {

  champion$: Observable<Champion>;

  constructor(private championService: ChampionsService, private route: ActivatedRoute) {}

  ngOnInit() {
    this.getChampion();
  }

  getChampion(): void {
    this.champion$ = this.route.paramMap
      .switchMap((params: ParamMap) => this.championService.getChampion(params.get('name')));
  }

}
