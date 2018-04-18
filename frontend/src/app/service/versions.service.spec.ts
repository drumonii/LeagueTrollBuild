import { TestBed, inject } from '@angular/core/testing';

import { VersionsService } from './versions.service';

describe('VersionsService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [VersionsService]
    });
  });

  it('should be created', inject([VersionsService], (service: VersionsService) => {
    expect(service).toBeTruthy();
  }));
});
