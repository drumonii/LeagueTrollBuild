import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';

import { NbEvaIconsModule } from '@nebular/eva-icons';

import { NotFoundPage } from './not-found.page';
import { NotFoundModule } from './not-found.module';

describe('NotFoundPage', () => {
  let component: NotFoundPage;
  let fixture: ComponentFixture<NotFoundPage>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule, NbEvaIconsModule, NotFoundModule]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NotFoundPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should show not found (404) page', () => {
    expect(component).toBeTruthy();
  });
});
