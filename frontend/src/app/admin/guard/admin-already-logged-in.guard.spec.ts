import { inject, TestBed } from '@angular/core/testing';

import { AdminAlreadyLoggedInGuard } from './admin-already-logged-in.guard';

describe('AdminAlreadyLoggedInGuard', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [AdminAlreadyLoggedInGuard]
    });
  });

  it('should ...', inject([AdminAlreadyLoggedInGuard], (guard: AdminAlreadyLoggedInGuard) => {
    expect(guard).toBeTruthy();
  }));
});
