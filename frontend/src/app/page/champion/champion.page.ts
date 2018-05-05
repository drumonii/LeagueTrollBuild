import { Component, OnInit } from '@angular/core';

import { ActivatedRoute, ParamMap } from '@angular/router';

import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/switchMap';

import { BuildsService } from '@service/builds.service';
import { ChampionsService } from '@service/champions.service';
import { GameMapsService } from '@service/game-maps.service';
import { Build, BuildBuilder } from '@model/build';
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

  gameMap: GameMap;
  champion: Champion;
  gameMaps: GameMap[];
  trollBuild$: Observable<Map<String, Item[] | SummonerSpell[]>>;
  build: Build;

  constructor(private championService: ChampionsService, private gameMapsService: GameMapsService,
    private buildsService: BuildsService, private route: ActivatedRoute) {}

  ngOnInit() {
    this.getChampion();
    this.getGameMaps();
    this.getTrollBuild();
  }

  getChampion(): void {
    this.route.paramMap
      .switchMap((params: ParamMap) => this.championService.getChampion(params.get('name')))
      .subscribe(champion => this.champion = champion);
  }

  getGameMaps(): void {
    this.gameMapsService.forTrollBuild().subscribe(gameMaps => {
      this.gameMaps = gameMaps;
      this.gameMap = gameMaps.find(gameMap => gameMap.mapId === GameMap.summonersRiftId);
    });
  }

  getTrollBuild(): void {
    this.trollBuild$ = this.route.paramMap
      .switchMap((params: ParamMap) => this.championService.getTrollBuild(params.get('name'),
        this.gameMap ? this.gameMap.mapId : GameMap.summonersRiftId));
    this.build = null;
  }

  saveBuild(trollBuild: Item[] | SummonerSpell[]): void {
    const build = new BuildBuilder()
      .withChampion(this.champion)
      .withItems(trollBuild['items'] as Item[])
      .withSummonerSpells(trollBuild['summoner-spells'] as SummonerSpell[])
      .withTrinket(trollBuild['trinket'] as Item[])
      .withGameMap(this.gameMap)
      .build();
    this.buildsService.saveBuild(build).subscribe(res => {
      this.build = res.body;
      this.build.selfRef = res.headers.get('Location');
    });
  }

}
