import { TestBed } from '@angular/core/testing';

import { AdminHomeService } from './admin-home.service';

describe('AdminHomeService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: AdminHomeService = TestBed.get(AdminHomeService);
    expect(service).toBeTruthy();
  });
});
