import { TestBed, inject } from '@angular/core/testing';

import { GameMapsService } from './game-maps.service';

describe('GameMapsService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [GameMapsService]
    });
  });

  it('should be created', inject([GameMapsService], (service: GameMapsService) => {
    expect(service).toBeTruthy();
  }));
});
