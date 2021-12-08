import { Injectable } from '@angular/core';
import { Title } from '@angular/platform-browser';

@Injectable({
  providedIn: 'root'
})
export class TitleService {

  baseTitle = 'League Troll Build';

  constructor(private title: Title) {}

  setTitle(contentTitle: string): void {
    this.resetTitle();
    this.title.setTitle(`${this.title.getTitle()} | ${contentTitle}`);
  }

  resetTitle(): void {
    this.title.setTitle(this.baseTitle);
  }

}
