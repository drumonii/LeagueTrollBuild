import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';

import { NbStatusService } from '@nebular/theme';
import { NbEvaIconsModule } from '@nebular/eva-icons';

import { NotFoundPage } from './not-found.page';
import { NotFoundModule } from './not-found.module';

describe('NotFoundPage', () => {
  let component: NotFoundPage;
  let fixture: ComponentFixture<NotFoundPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RouterTestingModule, NbEvaIconsModule, NotFoundModule],
      providers: [
        {
          provide: NbStatusService,
          useValue: {
            isCustomStatus: () => false
          }
        }
      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NotFoundPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should show not found (404) page', () => {
    expect(component).toBeTruthy();
  });
});
