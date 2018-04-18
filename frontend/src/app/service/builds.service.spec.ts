import { TestBed, inject } from '@angular/core/testing';

import { BuildsService } from './builds.service';

describe('BuildsService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [BuildsService]
    });
  });

  it('should be created', inject([BuildsService], (service: BuildsService) => {
    expect(service).toBeTruthy();
  }));
});
