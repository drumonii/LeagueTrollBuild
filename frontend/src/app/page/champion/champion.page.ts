import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, ParamMap } from '@angular/router';

import { Observable } from 'rxjs/Observable';
import { of } from 'rxjs/observable/of';
import 'rxjs/add/operator/switchMap';

import { BuildsService } from '@service/builds.service';
import { ChampionsService } from '@service/champions.service';
import { GameMapsService } from '@service/game-maps.service';
import { Build, BuildBuilder } from '@model/build';
import { Champion } from '@model/champion';
import { GameMap } from '@model/game-map';
import { TrollBuild } from '@model/troll-build';

@Component({
  selector: 'ltb-champion',
  templateUrl: './champion.page.html',
  styleUrls: ['./champion.page.scss']
})
export class ChampionPage implements OnInit {

  gameMap: GameMap;
  champion: Champion;
  gameMaps: GameMap[];
  trollBuild$: Observable<TrollBuild>;
  build: Build;

  constructor(private championService: ChampionsService, private gameMapsService: GameMapsService,
    private buildsService: BuildsService, private title: Title, private route: ActivatedRoute) {}

  ngOnInit() {
    this.setTitle();
    this.getChampion();
    this.getGameMaps();
    this.getTrollBuild();
  }

  setTitle(): void {
    this.route.paramMap
      .switchMap((params: ParamMap) => of(params.get('name')))
      .subscribe(name => this.title.setTitle(`${this.title.getTitle()} | ${name}`));
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

  saveBuild(trollBuild: TrollBuild): void {
    const build = new BuildBuilder()
      .withChampion(this.champion)
      .withItems(trollBuild.items)
      .withSummonerSpells(trollBuild.summonerSpells)
      .withTrinket(trollBuild.trinket)
      .withGameMap(this.gameMap)
      .build();
    this.buildsService.saveBuild(build).subscribe(res => {
      this.build = res.body;
      this.build.selfRef = res.headers.get('Location');
    });
  }

}
