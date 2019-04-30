import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize, tap } from 'rxjs/operators';

import { TitleService } from '@ltb-service/title.service';
import { ChampionService } from './champion.service';
import { Build, BuildBuilder } from '@ltb-model/build';
import { Champion } from '@ltb-model/champion';
import { GameMap, SummonersRiftId } from '@ltb-model/game-map';
import { TrollBuild } from '@ltb-model/troll-build';

@Component({
  selector: 'ltb-champion',
  templateUrl: './champion.page.html',
  styleUrls: ['./champion.page.scss']
})
export class ChampionPage implements OnInit {

  gameMap: GameMap;
  champion: Champion;
  gameMaps$: Observable<GameMap[]>;
  trollBuild$: Observable<TrollBuild>;
  trollBuildLoading: boolean;
  build: Build;
  buildSaving: boolean;

  constructor(private championService: ChampionService, private title: TitleService, private route: ActivatedRoute) {}

  ngOnInit() {
    this.route.data.subscribe((data: { champion: Champion }) => {
      this.champion = data.champion;
      this.setTitle(data.champion.name);
    });

    this.gameMaps$ = this.getGameMaps()
      .pipe(
        tap(gameMaps => {
          this.gameMap = gameMaps.find(gameMap => gameMap.mapId === SummonersRiftId);
          return gameMaps;
        }),
        finalize(() => this.getTrollBuild())
      );
  }

  private setTitle(championName: string) {
    this.title.setTitle(championName);
  }

  getGameMaps(): Observable<GameMap[]> {
    return this.championService.mapsForTrollBuild();
  }

  getTrollBuild(): void {
    this.build = null;
    this.trollBuildLoading = true;
    this.trollBuild$ = this.championService.getTrollBuild(this.champion.name, this.gameMap.mapId)
      .pipe(
        finalize(() => this.trollBuildLoading = false)
      );
  }

  saveBuild(trollBuild: TrollBuild): void {
    const build = new BuildBuilder()
      .withChampion(this.champion)
      .withItems(trollBuild.items)
      .withSummonerSpells(trollBuild.summonerSpells)
      .withTrinket(trollBuild.trinket)
      .withGameMap(this.gameMap)
      .build();
    this.buildSaving = true;
    this.championService.saveBuild(build).subscribe(res => {
      this.build = res.body;
      this.build.selfRef = res.headers.get('Location').replace('/api', '');
      this.buildSaving = false;
    });
  }

}
