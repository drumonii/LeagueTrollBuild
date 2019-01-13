import { AfterViewInit, Directive, ElementRef, Input } from '@angular/core';

@Directive({
  selector: '[ltbLazyLoadImg]'
})
export class LazyLoadImgDirective implements AfterViewInit {

  @Input() dataSrc: string;

  constructor(private elementRef: ElementRef) {}

  ngAfterViewInit() {
    this.canLazyLoad() ? this.lazyLoadImage() : this.loadImage();
  }

  private canLazyLoad() {
    return window && 'IntersectionObserver' in window;
  }

  private lazyLoadImage() {
    const observer = new IntersectionObserver(this.handleIntersect.bind(this));
    observer.observe(this.elementRef.nativeElement);
  }

  private handleIntersect(entries, observer): void {
    entries.forEach((entry: IntersectionObserverEntry) => {
      if (entry.isIntersecting) {
        this.loadImage();
        observer.unobserve(this.elementRef.nativeElement);
      }
    });
  }

  private loadImage() {
    this.elementRef.nativeElement.src = this.dataSrc;
  }

}
