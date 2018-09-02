import { TestBed, inject, fakeAsync, tick } from '@angular/core/testing';

import { LoadingBarModule } from './loading-bar.module';
import { LoadingBarService } from './loading-bar.service';

describe('LoadingBarService', () => {

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [LoadingBarModule]
    });
  });

  describe('multiple requests', () => {

    it('should set initial progress as 1 after started', inject([LoadingBarService], (service: LoadingBarService) => {
      service.start();
      service.start();

      expect(service.progress.getValue()).toBe(1);

      service.complete();
      service.complete();
    }));

    it('should set progress as 100 when completed', inject([LoadingBarService], (service: LoadingBarService) => {
      service.start();
      service.start();

      service.complete();
      service.complete();

      expect(service.progress.getValue()).toBe(100);
    }));

    it('should auto increment progress after started', fakeAsync(inject([LoadingBarService], (service: LoadingBarService) => {
      service.start();
      service.start();

      tick();

      expect(service.progress.getValue()).toBe(1);

      tick(1000);

      expect(service.progress.getValue()).toBeLessThan(25);

      tick(2000);

      expect(service.progress.getValue()).toBeLessThan(65);

      tick(3000);

      expect(service.progress.getValue()).toBeLessThan(65);

      tick(4000);

      expect(service.progress.getValue()).toBeLessThan(90);

      tick(5000);

      expect(service.progress.getValue()).toBeLessThan(99);

      tick(6000);

      expect(service.progress.getValue()).toBeGreaterThan(99);

      tick(7000);

      expect(service.progress.getValue()).toBeGreaterThan(99);

      tick(8000);

      expect(service.progress.getValue()).toBeGreaterThan(99);

      tick(9000);

      expect(service.progress.getValue()).toBeGreaterThan(99);

      service.complete();
      service.complete();

      tick(500);

      expect(service.progress.getValue()).toBe(0);
    })));

  });

  describe('single request', () => {

    it('should set initial progress as 1 after started', inject([LoadingBarService], (service: LoadingBarService) => {
      service.start();

      expect(service.progress.getValue()).toBe(1);

      service.complete();
    }));

    it('should set progress as 100 when completed', inject([LoadingBarService], (service: LoadingBarService) => {
      service.start();

      service.complete();

      expect(service.progress.getValue()).toBe(100);
    }));

    it('should auto increment progress after started', fakeAsync(inject([LoadingBarService], (service: LoadingBarService) => {
      service.start();

      tick();

      expect(service.progress.getValue()).toBe(1);

      tick(1000);

      expect(service.progress.getValue()).toBeLessThan(25);

      tick(2000);

      expect(service.progress.getValue()).toBeLessThan(65);

      tick(3000);

      expect(service.progress.getValue()).toBeLessThan(65);

      tick(4000);

      expect(service.progress.getValue()).toBeLessThan(90);

      tick(5000);

      expect(service.progress.getValue()).toBeLessThan(99);

      tick(6000);

      expect(service.progress.getValue()).toBeGreaterThan(99);

      tick(7000);

      expect(service.progress.getValue()).toBeGreaterThan(99);

      tick(8000);

      expect(service.progress.getValue()).toBeGreaterThan(99);

      tick(9000);

      expect(service.progress.getValue()).toBeGreaterThan(99);

      service.complete();

      tick(500);

      expect(service.progress.getValue()).toBe(0);
    })));

  });

  describe('no requests', () => {

    it('should ignore complete', inject([LoadingBarService], (service: LoadingBarService) => {
      service.complete();

      expect(service.progress.getValue()).toBe(1);
    }));

  });

});
