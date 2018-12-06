import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FlywayModule } from './flyway.module';
import { FlywayPage } from './flyway.page';

describe('FlywayPage', () => {
  let component: FlywayPage;
  let fixture: ComponentFixture<FlywayPage>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [FlywayModule]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FlywayPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
