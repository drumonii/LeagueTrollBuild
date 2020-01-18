import { inject, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';

import { DisabledGuard } from './disabled.guard';

describe('DisabledGuard', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule],
      providers: [DisabledGuard]
    });
  });

  it('should return false', inject([DisabledGuard], (guard: DisabledGuard) => {
    expect(guard.canLoad(null, null)).toBeFalse();
  }));
});
