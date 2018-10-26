import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'ltb-admin-header',
  templateUrl: './admin-header.component.html',
  styleUrls: ['./admin-header.component.scss']
})
export class AdminHeaderComponent implements OnInit {

  header = 'League Troll Build Admin';

  constructor() { }

  ngOnInit() {
  }

}
