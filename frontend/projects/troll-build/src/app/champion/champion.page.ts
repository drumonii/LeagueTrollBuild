import { ChangeDetectionStrategy, Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { Observable, of } from 'rxjs';
import { finalize, map, tap } from 'rxjs/operators';

import { TitleService } from '@ltb-service/title.service';
import { ChampionService } from './champion.service';
import { Build, BuildBuilder } from '@ltb-model/build';
import { Champion } from '@ltb-model/champion';
import { GameMap, SummonersRiftId } from '@ltb-model/game-map';
import { TrollBuild } from '@ltb-model/troll-build';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'ltb-champion',
  templateUrl: './champion.page.html',
  styleUrls: ['./champion.page.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush // to avoid ExpressionChangedAfterItHasBeenCheckedError in tests
})
export class ChampionPage implements OnInit {

  gameMap: GameMap;
  gameMaps$: Observable<GameMap[]>;

  champion: Champion;

  trollBuild$: Observable<TrollBuild>;
  trollBuildLoading = true;

  build$: Observable<Build | null>;
  buildSaving = false;

  constructor(private championService: ChampionService, private title: TitleService, private route: ActivatedRoute) {}

  ngOnInit() {
    this.route.data.subscribe((data: any) => {
      this.champion = data.champion;
      this.setTitle(data.champion.name);
    });

    this.gameMaps$ = this.getGameMaps()
      .pipe(
        tap(gameMaps => {
          this.gameMap = gameMaps.find(gameMap => gameMap.mapId === SummonersRiftId);
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
    this.build$ = of(null);
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
    this.build$ = this.championService.saveBuild(build)
      .pipe(
        map((res) => {
          const savedBuild = res.body;
          savedBuild.selfRef = res.headers.get('Location').replace('/api', '');
          return savedBuild;
        }),
        finalize(() => this.buildSaving = false)
      );
  }

}
