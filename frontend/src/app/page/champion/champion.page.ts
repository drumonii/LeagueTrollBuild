import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { TitleService } from '@service/title.service';
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
  trollBuildLoading: boolean;
  build: Build;
  buildSaving: boolean;

  constructor(private championService: ChampionService, private gameMapsService: GameMapsService,
    private buildsService: BuildsService, private title: TitleService, private route: ActivatedRoute) {}

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

  private setTitle(championName: string) {
    this.title.setTitle(championName);
  }

  getGameMaps(): Observable<GameMap[]> {
    return this.gameMapsService.forTrollBuild();
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
    this.buildsService.saveBuild(build).subscribe(res => {
      this.build = res.body;
      this.build.selfRef = res.headers.get('Location').replace('/api', '');
      this.buildSaving = false;
    });
  }

  copyBuildToClipboard(savedBuildLinkInput: HTMLInputElement): void {
    savedBuildLinkInput.select();
    document.execCommand('copy');
    savedBuildLinkInput.setSelectionRange(0, 0);
  }

}
