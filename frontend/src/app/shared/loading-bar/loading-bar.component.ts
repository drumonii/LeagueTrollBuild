import { Component, OnInit } from '@angular/core';

import { Observable } from 'rxjs';

import { LoadingBarService } from './loading-bar.service';

@Component({
  selector: 'ltb-loading-bar',
  templateUrl: './loading-bar.component.html',
  styleUrls: ['./loading-bar.component.scss']
})
export class LoadingBarComponent implements OnInit {

  progress$: Observable<number>;

  constructor(private service: LoadingBarService) {}

  ngOnInit(): void {
    this.progress$ = this.service.progress;
  }

}

