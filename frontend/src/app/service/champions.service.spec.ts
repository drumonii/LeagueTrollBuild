import { TestBed, inject } from '@angular/core/testing';

import { ChampionsService } from './champions.service';

describe('ChampionsService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ChampionsService]
    });
  });

  it('should be created', inject([ChampionsService], (service: ChampionsService) => {
    expect(service).toBeTruthy();
  }));
});
