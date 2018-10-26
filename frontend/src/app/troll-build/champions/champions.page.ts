import { Component, OnInit } from '@angular/core';

import { Observable } from 'rxjs';

import { TitleService } from '@service/title.service';
import { ChampionsService } from './champions.service';
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

  constructor(private championsService: ChampionsService, private title: TitleService) {}

  ngOnInit() {
    this.setTitle();
    this.getChampions();
    this.getChampionTags();
  }

  private setTitle(): void {
    this.title.resetTitle();
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
