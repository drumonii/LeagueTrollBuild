import { ComponentFixture, fakeAsync, inject, TestBed, tick } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { By } from '@angular/platform-browser';

import { of } from 'rxjs';
import { delay } from 'rxjs/operators';

import { ResourcesModule } from './resources.module';
import { ResourcesComponent } from './resources.component';
import { ResourceConsumption, ResourcesService } from './resources.service';

describe('ResourcesComponent', () => {
  let component: ResourcesComponent;
  let fixture: ComponentFixture<ResourcesComponent>;

  const cardContentSets = 3;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, ResourcesModule]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ResourcesComponent);
    component = fixture.componentInstance;
  });

  describe('loaded resources', () => {

    const resourceConsumption: ResourceConsumption = {
      cpuUsage: {
        percentage:  0.22810254618086526,
        cpus: 8
      },
      memoryUsage: {
        used: 257671752,
        max: 9900654589
      },
      diskUsage: {
        used: 253441654784,
        max: 510969704448
      }
    };

    beforeEach(inject([ResourcesService], (service: ResourcesService) => {
      spyOn(service, 'getResourceConsumption').and.returnValue(of(resourceConsumption));
    }));

    afterEach(inject([ResourcesService], (service: ResourcesService) => {
      expect(service.getResourceConsumption).toHaveBeenCalled();
    }));

    it('should show the system resources', () => {
      fixture.detectChanges();

      expectCardContentSets();

      const cpuUsage = fixture.debugElement.query(By.css('[data-e2e="cpu-usage"]'));
      expect(cpuUsage.nativeElement.textContent.trim()).toBe(`22.81% of ${resourceConsumption.cpuUsage.cpus} CPUs`);

      const memoryUsage = fixture.debugElement.query(By.css('[data-e2e="memory-usage"]'));
      expect(memoryUsage.nativeElement.textContent.trim()).toBe('257.67 MB of 9900.65 MB');

      const diskUsage = fixture.debugElement.query(By.css('[data-e2e="disk-usage"]'));
      expect(diskUsage.nativeElement.textContent.trim()).toBe('253.44 GB of 510.97 GB');
    });

  });

  describe('loading resources', () => {

    const networkDelay = 2500;

    beforeEach(inject([ResourcesService], (service: ResourcesService) => {
      spyOn(service, 'getResourceConsumption').and.returnValue(of({
        cpuUsage: null,
        memoryUsage: null,
        diskUsage: null
      }).pipe(delay(networkDelay)));
    }));

    afterEach(inject([ResourcesService], (service: ResourcesService) => {
      expect(service.getResourceConsumption).toHaveBeenCalled();
    }));

    it('should show loading indicator', fakeAsync(() => {
      fixture.detectChanges();

      expect(fixture.debugElement.query(By.css('.progress'))).toBeTruthy();

      expectCardContentSets();

      tick(networkDelay);
    }));

  });

  describe('error loading resources', () => {

    beforeEach(inject([ResourcesService], (service: ResourcesService) => {
      spyOn(service, 'getResourceConsumption').and.returnValue(of({
        cpuUsage: null,
        memoryUsage: null,
        diskUsage: null
      }));
    }));

    afterEach(inject([ResourcesService], (service: ResourcesService) => {
      expect(service.getResourceConsumption).toHaveBeenCalled();
    }));

    it('should show error alert', () => {
      fixture.detectChanges();

      expect(fixture.debugElement.query(By.css('[data-e2e="error-resources-alert"]'))).toBeTruthy();
    });

  });

  function expectCardContentSets() {
    expect(fixture.debugElement.queryAll(By.css('.card-title')).length).toBe(cardContentSets);
    expect(fixture.debugElement.queryAll(By.css('.card-text')).length).toBe(cardContentSets);
  }
});
