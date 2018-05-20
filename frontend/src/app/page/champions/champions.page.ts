import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';

import { Observable } from 'rxjs/Observable';

import { ChampionsService } from '@service/champions.service';
import { Champion } from '@model/champion';

@Component({
  selector: 'ltb-champions',
  templateUrl: './champions.page.html',
  styleUrls: ['./champions.page.scss']
})
export class ChampionsPage implements OnInit {

  champions$: Observable<Champion[]>;
  championTags$: Observable<string[]>;
  championsSearchName: string;
  championsFilterTag: string;

  constructor(private championsService: ChampionsService, private title: Title) {}

  ngOnInit() {
    this.setTitle();
    this.getChampions();
    this.getChampionTags();
  }

  private setTitle(): void {
    if (this.title.getTitle().indexOf('|') > -1) {
      this.title.setTitle(this.title.getTitle().substring(0, this.title.getTitle().indexOf('|') - 1));
    }
  }

  getChampions(): void {
    this.champions$ = this.championsService.getChampions();
  }

  getChampionTags(): void {
    this.championTags$ = this.championsService.getChampionTags();
  }

  setChampionsFilterTag(selectedFilterTag: string): void {
    if (this.championsFilterTag === selectedFilterTag) {
      this.championsFilterTag = '';
    } else {
      this.championsFilterTag = selectedFilterTag;
    }
  }

}
