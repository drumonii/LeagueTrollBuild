import { async, ComponentFixture, fakeAsync, inject, TestBed, tick } from '@angular/core/testing';
import { By } from '@angular/platform-browser';

import { LoadingBarModule } from './loading-bar.module';
import { LoadingBarComponent } from './loading-bar.component';
import { LoadingBarService } from './loading-bar.service';

describe('LoadingBarComponent', () => {
  let component: LoadingBarComponent;
  let fixture: ComponentFixture<LoadingBarComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [LoadingBarModule]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LoadingBarComponent);
    component = fixture.componentInstance;
  });

  it('should be visible when new progress value is emitted', inject([LoadingBarService], (service: LoadingBarService) => {
    service.start();

    fixture.detectChanges();

    const progressBar = fixture.debugElement.query(By.css('#loading-bar'));
    expect(progressBar).toBeTruthy();

    service.complete();
  }));

  it('should increase width based on new progress emitted', inject([LoadingBarService], (service: LoadingBarService) => {
    service.start();

    fixture.detectChanges();

    const progressBar = fixture.debugElement.query(By.css('#loading-bar'));
    expect(progressBar.nativeElement.value).toBeGreaterThan(0);

    service.complete();
  }));

  it('should be invisible when completed progress', fakeAsync(inject([LoadingBarService], (service: LoadingBarService) => {
    service.start();
    service.complete();

    tick(501);
    fixture.detectChanges();

    const progressBar = fixture.debugElement.query(By.css('#loading-bar'));
    expect(progressBar).toBeFalsy();
  })));

});
