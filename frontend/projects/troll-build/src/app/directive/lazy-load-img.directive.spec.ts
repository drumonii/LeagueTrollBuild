import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { Component } from '@angular/core';
import { By } from '@angular/platform-browser';

import { LazyLoadImgModule } from './lazy-load-img.module';

@Component({
  template: `<img ltbLazyLoadImg src="defaultSrc.png" [dataSrc]="'dataSrc.png'">`
})
class TestLazyLoadImgComponent {
}

describe('LazyLoadImgDirective', () => {
  let component: TestLazyLoadImgComponent;
  let fixture: ComponentFixture<TestLazyLoadImgComponent>;
  let supportsIntersectionObserver;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [LazyLoadImgModule],
      declarations: [TestLazyLoadImgComponent]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TestLazyLoadImgComponent);
    component = fixture.componentInstance;

    if ('IntersectionObserver' in window) {
      supportsIntersectionObserver = true;
      spyOn(IntersectionObserver.prototype, 'observe');
    } else {
      supportsIntersectionObserver = false;
    }

    fixture.detectChanges();
  });

  it('should observe via IntersectionObserver if supported', () => {
    const img = fixture.debugElement.query(By.css('img'));

    if (supportsIntersectionObserver) {
      expect(IntersectionObserver.prototype.observe).toHaveBeenCalled();
    } else {
      expect(img.nativeElement.src).toContain('dataSrc.png');
    }
  });

});
