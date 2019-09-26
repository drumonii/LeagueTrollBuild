import { ChangeDetectionStrategy, Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

import { Observable } from 'rxjs';

import { TrollBuild } from '@ltb-model/troll-build';
import { Item } from '@ltb-model/item';
import { Build } from '@ltb-model/build';
import { SummonerSpell } from '@ltb-model/summoner-spell';

@Component({
  selector: 'ltb-troll-build',
  templateUrl: './troll-build.component.html',
  styleUrls: ['./troll-build.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class TrollBuildComponent implements OnInit {

  @Input()
  trollBuild: TrollBuild;
  @Input()
  build$: Observable<Build>;
  @Input()
  buildSaving: boolean;

  @Output()
  saveTrollBuildEvent = new EventEmitter<TrollBuild>();

  constructor() { }

  ngOnInit() {
  }

  saveBuild(): void {
    this.saveTrollBuildEvent.next(this.trollBuild);
  }

  copyBuildToClipboard(savedBuildLinkInput: HTMLInputElement): void {
    savedBuildLinkInput.select();
    document.execCommand('copy');
    savedBuildLinkInput.setSelectionRange(0, 0);
  }

  trackByItems(index: number, item: Item): number {
    return item.id;
  }

  trackBySummonerSpells(index: number, summonerSpell: SummonerSpell): number {
    return summonerSpell.id;
  }

}
