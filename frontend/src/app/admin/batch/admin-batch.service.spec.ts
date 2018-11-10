import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';

import { AdminBatchService } from './admin-batch.service';

describe('AdminBatchService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AdminBatchService]
    });
  });

  it('should be created', () => {
    const service: AdminBatchService = TestBed.get(AdminBatchService);
    expect(service).toBeTruthy();
  });
});
