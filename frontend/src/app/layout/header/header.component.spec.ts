import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { RouterLinkWithHref } from '@angular/router';
import { By } from '@angular/platform-browser';

import { HeaderComponent } from './header.component';

describe('HeaderComponent', () => {
  let component: HeaderComponent;
  let fixture: ComponentFixture<HeaderComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule],
      declarations: [HeaderComponent]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HeaderComponent);
    component = fixture.componentInstance;
  });

  it('should show header with link to root', () => {
    fixture.detectChanges();

    const headerLinkDe = fixture.debugElement.query(By.css('#header-title'));
    expect(headerLinkDe.nativeElement.textContent).toBe(component.header);
    const headerRouterLink = headerLinkDe.injector.get(RouterLinkWithHref);
    expect(headerRouterLink.href).toBe('/');
  });
});
