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

    const headerLink = fixture.debugElement.query(By.css('#header-title-link'));
    expect(headerLink.nativeElement.textContent).toBe(component.header);
    const headerRouterLink = headerLink.injector.get(RouterLinkWithHref);
    expect(headerRouterLink.href).toBe('/');
  });

  describe('outdated browser warning', () => {

    it('should show with an IE 9 user agent', () => {
      Object.defineProperty(navigator, 'userAgent', {
        get: () => 'Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)'
      });

      fixture.detectChanges();

      expectOutdatedBrowserAlert();
    });

    it('should show with an IE 10 user agent', () => {
      Object.defineProperty(navigator, 'userAgent', {
        get: () => 'Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.2; Trident/6.0)'
      });

      fixture.detectChanges();

      expectOutdatedBrowserAlert();
    });

    it('should not show with an IE 11 user agent', () => {
      Object.defineProperty(navigator, 'userAgent', {
        get: () => 'Mozilla/5.0 (Windows NT 6.3; Trident/7.0; rv:11.0) like Gecko'
      });

      fixture.detectChanges();

      expectNoOutdatedBrowserAlert();
    });

    it('should not show with an Edge user agent', () => {
      Object.defineProperty(navigator, 'userAgent', {
        get: () => 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) ' +
          'Chrome/46.0.2486.0 Safari/537.36 Edge/13.10586'
      });

      fixture.detectChanges();

      expectNoOutdatedBrowserAlert();
    });

    it('should not show with a Chrome user agent', () => {
      Object.defineProperty(navigator, 'userAgent', {
        get: () => 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) ' +
          'Chrome/60.0.3112.113 Safari/537.36'
      });

      fixture.detectChanges();

      expectNoOutdatedBrowserAlert();
    });

    it('should not show with a Firefox user agent', () => {
      Object.defineProperty(navigator, 'userAgent', {
        get: () => 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:61.0) Gecko/20100101 Firefox/61.0'
      });

      fixture.detectChanges();

      expectNoOutdatedBrowserAlert();
    });

    function expectOutdatedBrowserAlert() {
      const outdatedBrowserAlert = fixture.debugElement.query(By.css('#outdated-browser'));
      expect(outdatedBrowserAlert).toBeTruthy();
    }

    function expectNoOutdatedBrowserAlert() {
      const outdatedBrowserAlert = fixture.debugElement.query(By.css('#outdated-browser'));
      expect(outdatedBrowserAlert).toBeFalsy();
    }

  });

});
