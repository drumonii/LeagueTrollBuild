import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';

import { SavedBuildsResolverData } from './saved-builds.resolver.data';
import { TitleService } from '@ltb-service/title.service';
import { SavedBuildsService } from './saved-builds.service';
import { Build, BuildType } from '@ltb-model/build';

@Component({
  selector: 'ltb-saved-builds',
  templateUrl: './saved-builds.page.html',
  styleUrls: ['./saved-builds.page.scss']
})
export class SavedBuildsPage implements OnInit {

  buildId: number;
  build: Build;
  buildType: BuildType;

  constructor(private buildsService: SavedBuildsService, private title: TitleService, private router: Router,
              private route: ActivatedRoute) {}

  ngOnInit() {
    this.getBuild();
  }

  private setTitle() {
    this.title.setTitle(this.getTitle());
  }

  private getTitle(): string {
    switch (this.buildType) {
      case BuildType.Ok:
        return `${this.build.champion.name} Build | ${this.buildId}`;
      case BuildType.NotFound:
        return `Couldn't find Troll Build ${this.buildId}`;
      default:
        return `Invalid attributes in Troll Build ${this.buildId}`;
    }
  }

  private getBuild(): void {
    this.route.data.subscribe((data: { build: SavedBuildsResolverData }) => {
      this.buildId = data.build.id;
      this.build = data.build.savedBuild;
      this.buildType = this.getBuildType(data.build.savedBuild);
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
