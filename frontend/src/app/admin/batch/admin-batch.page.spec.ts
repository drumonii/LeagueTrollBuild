import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminBatchModule } from './admin-batch.module';
import { AdminBatchPage } from './admin-batch.page';

describe('AdminBatchPage', () => {
  let component: AdminBatchPage;
  let fixture: ComponentFixture<AdminBatchPage>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [AdminBatchModule]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminBatchPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
