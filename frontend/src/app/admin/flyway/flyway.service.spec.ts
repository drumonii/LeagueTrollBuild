import { TestBed } from '@angular/core/testing';

import { FlywayService } from './flyway.service';

describe('FlywayService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: FlywayService = TestBed.get(FlywayService);
    expect(service).toBeTruthy();
  });
});
