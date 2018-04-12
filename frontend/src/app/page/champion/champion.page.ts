import { Component, OnInit } from '@angular/core';

import { ActivatedRoute, ParamMap } from '@angular/router';

import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/switchMap';

import { ChampionsService } from '@service/champions.service';
import { GameMapsService } from '@service/game-maps.service';
import { Champion } from '@model/champion';
import { GameMap } from '@model/game-map';
import { Item } from '@model/item';
import { SummonerSpell } from '@model/summoner-spell';

@Component({
  selector: 'ltb-champion',
  templateUrl: './champion.page.html',
  styleUrls: ['./champion.page.scss']
})
export class ChampionPage implements OnInit {

  gameMapId: number;
  champion$: Observable<Champion>;
  gameMaps$: Observable<GameMap[]>;
  trollBuild$: Observable<Map<String, Item[] | SummonerSpell[]>>;

  constructor(private championService: ChampionsService, private gameMapsService: GameMapsService,
    private route: ActivatedRoute) {
    this.gameMapId = GameMap.summonersRiftId;
  }

  ngOnInit() {
    this.getChampion();
    this.getGameMaps();
    this.getTrollBuild();
  }

  getChampion(): void {
    this.champion$ = this.route.paramMap
      .switchMap((params: ParamMap) => this.championService.getChampion(params.get('name')));
  }

  getGameMaps(): void {
    this.gameMaps$ = this.gameMapsService.forTrollBuild();
  }

  getTrollBuild(gameMapId?: number): void {
    this.trollBuild$ = this.route.paramMap
      .switchMap((params: ParamMap) => this.championService.getTrollBuild(params.get('name'), gameMapId));
  }

}
