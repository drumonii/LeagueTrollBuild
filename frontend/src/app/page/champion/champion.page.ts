import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';

import { BuildsService } from '@service/builds.service';
import { ChampionService } from '@service/champion.service';
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

  constructor(private championService: ChampionService, private gameMapsService: GameMapsService,
    private buildsService: BuildsService, private title: Title, private route: ActivatedRoute) {}

  ngOnInit() {
    this.route.data.subscribe((data: { champion: Champion }) => {
      this.champion = data.champion;
      this.setTitle(data.champion.name);
    });
    this.getGameMaps().subscribe(gameMaps => {
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

  getGameMaps(): Observable<GameMap[]> {
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
      this.build.selfRef = res.headers.get('Location').replace('/api', '');
    });
  }

}
