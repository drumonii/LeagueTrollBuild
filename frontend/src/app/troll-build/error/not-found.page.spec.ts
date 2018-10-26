import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { RouterLinkWithHref } from '@angular/router';
import { By } from '@angular/platform-browser';

import { NotFoundPage } from './not-found.page';
import { NotFoundModule } from './not-found.module';

describe('NotFoundPage', () => {
  let component: NotFoundPage;
  let fixture: ComponentFixture<NotFoundPage>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [NotFoundModule, RouterTestingModule]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NotFoundPage);
    component = fixture.componentInstance;
  });

  it('should show not found (404) page', () => {
    fixture.detectChanges();

    const title = fixture.debugElement.query(By.css('#title'));
    expect(title.nativeElement.textContent.trim()).toBe('Page Not Found');

    const message = fixture.debugElement.query(By.css('#msg'));
    expect(message.nativeElement.textContent.trim())
      .toBe('The page you requested was not found. Please double check the URL and try again.');

    const returnHomeLink = fixture.debugElement.query(By.css('#return-to-home'));
    expect(returnHomeLink.nativeElement.textContent.trim()).toBe('Return to home');
    const returnHomeRouterLink = returnHomeLink.injector.get(RouterLinkWithHref);
    expect(returnHomeRouterLink.href).toBe('/');
  });
});
