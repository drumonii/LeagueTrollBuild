import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute, ParamMap } from '@angular/router';

import { Observable, forkJoin } from 'rxjs';
import { switchMap } from 'rxjs/operators';

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
    this.route.paramMap.pipe(
      switchMap(((params: ParamMap) => {
        const name = params.get('name');
        this.setTitle(name);
        return forkJoin(this.getChampion(name), this.getGameMaps());
      })))
      .subscribe(([champion, gameMaps]) => {
        this.champion = champion;
        this.gameMap = gameMaps.find(gameMap => gameMap.mapId === GameMap.summonersRiftId);
        this.gameMaps = gameMaps;
        this.getTrollBuild();
      });
  }

  private setTitle(name: string) {
    if (this.title.getTitle().indexOf('|') === -1) {
      this.title.setTitle(`${this.title.getTitle()} | ${name}`);
    } else {
      this.title.setTitle(`${this.title.getTitle().substring(0, this.title.getTitle().indexOf('|') - 1)} | ${name}`);
    }
  }

  private getChampion(name: string): Observable<Champion> {
    return this.championService.getChampion(name);
  }

  private getGameMaps(): Observable<GameMap[]> {
    return this.gameMapsService.forTrollBuild();
  }

  getTrollBuild(): void {
    this.build = null;
    this.trollBuild$ = this.championService.getTrollBuild(this.champion.name, this.gameMap.mapId);
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
