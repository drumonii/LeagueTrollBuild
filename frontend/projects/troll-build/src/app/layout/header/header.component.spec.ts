import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { RouterLinkWithHref } from '@angular/router';
import { By } from '@angular/platform-browser';

import { HeaderModule } from './header.module';
import { HeaderComponent } from './header.component';

describe('HeaderComponent', () => {
  let component: HeaderComponent;
  let fixture: ComponentFixture<HeaderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RouterTestingModule, HeaderModule]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(HeaderComponent);
    component = fixture.componentInstance;
  });

  it('should show header with link to root', () => {
    fixture.detectChanges();

    const headerLink = fixture.debugElement.query(By.css('.header-title a'));
    expect(headerLink.nativeElement.textContent).toBe(component.header);
    const headerRouterLink = headerLink.injector.get(RouterLinkWithHref);
    expect(headerRouterLink.href).toBe('/');
  });
});
