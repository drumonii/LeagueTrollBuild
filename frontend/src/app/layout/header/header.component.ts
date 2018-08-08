import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'ltb-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  header = 'League Troll Build';
  isBrowserOutdated: boolean;

  constructor() { }

  ngOnInit() {
    this.isBrowserOutdatedIe();
  }

  isBrowserOutdatedIe(): void {
    const userAgent = navigator.userAgent;
    this.isBrowserOutdated = userAgent.indexOf('MSIE') > 0; // was IE < 11
  }

}
