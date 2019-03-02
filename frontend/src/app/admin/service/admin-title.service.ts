import { Injectable } from '@angular/core';
import { Title } from '@angular/platform-browser';

@Injectable({
  providedIn: 'root'
})
export class AdminTitleService {

  baseTitle = 'League Troll Build Admin';

  constructor(private title: Title) {}

  setTitle(contentTitle): void {
    this.resetTitle();
    this.title.setTitle(`${this.title.getTitle()} | ${contentTitle}`);
  }

  resetTitle(): void {
    this.title.setTitle(this.baseTitle);
  }

}
