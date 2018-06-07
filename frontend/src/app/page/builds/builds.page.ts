import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';

import { switchMap } from 'rxjs/operators';

import { BuildsService } from '@service/builds.service';
import { Build, BuildType } from '@model/build';
import { Title } from '@angular/platform-browser';

@Component({
  selector: 'ltb-builds',
  templateUrl: './builds.page.html',
  styleUrls: ['./builds.page.scss']
})
export class BuildsPage implements OnInit {

  buildId: number;
  build: Build;
  buildType: BuildType;

  constructor(private buildsService: BuildsService, private title: Title, private router: Router,
    private route: ActivatedRoute) {}

  ngOnInit() {
    this.getBuild();
  }

  private setTitle() {
    if (this.title.getTitle().indexOf('|') === -1) {
      this.title.setTitle(`${this.title.getTitle()} | ${this.getTitle()}`);
    } else {
      this.title.setTitle(`${this.title.getTitle().substring(0, this.title.getTitle().indexOf('|') - 1)} | ${this.getTitle()}`);
    }
  }

  private getTitle(): string {
    switch (this.buildType) {
      case BuildType.Ok:
        return `${this.build.champion.name} Build`;
      case BuildType.NotFound:
        return `Couldn't find Troll Build ${this.buildId}`;
      default:
        return `Invalid attributes in Troll Build ${this.buildId}`;
    }
  }

  private getBuild(): void {
    this.route.paramMap.pipe(
      switchMap(((params: ParamMap) => {
        this.buildId = +params.get('buildId');
        return this.buildsService.getBuild(this.buildId);
      })))
      .subscribe(build => {
        this.buildType = this.getBuildType(build);
        this.build = build;
        this.setTitle();
      });
  }

  private getBuildType(build: Build): BuildType {
    if (build == null) {
      return BuildType.NotFound;
    } else if (build.item1 == null || build.item2 == null || build.item3 == null ||
      build.item4 == null || build.item5 == null || build.item6 == null) {
      return BuildType.InvalidItems;
    } else if (build.summonerSpell1 == null || build.summonerSpell2 == null) {
      return BuildType.InvalidSummonerSpells;
    } else if (build.trinket == null) {
      return BuildType.InvalidTrinket;
    }
    return BuildType.Ok;
  }

  getTrollBuild(): void {
    this.router.navigate(['/champions', this.build.championId]);
  }

}
