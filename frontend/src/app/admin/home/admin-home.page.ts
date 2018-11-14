import { Component, OnInit } from '@angular/core';

import { AdminHomeService } from './admin-home.service';

@Component({
  selector: 'ltb-admin-home',
  templateUrl: './admin-home.page.html',
  styleUrls: ['./admin-home.page.scss']
})
export class AdminHomePage implements OnInit {

  constructor(private service: AdminHomeService) {}

  ngOnInit() {
  }

}
