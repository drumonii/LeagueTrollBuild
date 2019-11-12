import { async, ComponentFixture, fakeAsync, inject, TestBed, tick } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { By } from '@angular/platform-browser';

import { of } from 'rxjs';
import { delay } from 'rxjs/operators';

import { EnvModule } from './env.module';
import { EnvComponent } from './env.component';
import { EnvInfo, EnvService } from './env.service';

describe('EnvComponent', () => {
  let component: EnvComponent;
  let fixture: ComponentFixture<EnvComponent>;

  const cardContentSets = 3;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, EnvModule]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EnvComponent);
    component = fixture.componentInstance;
  });

  describe('loaded env', () => {

    const envInfo: EnvInfo = {
      jdk: {
        version: '11.0.2',
        vendor: 'OpenJDK Runtime Environment'
      },
      os: {
        name: 'Windows 10',
        arch: 'amd64'
      },
      tz: {
        name: 'America/New_York'
      }
    };

    beforeEach(inject([EnvService], (service: EnvService) => {
      spyOn(service, 'getEnv').and.returnValue(of(envInfo));
    }));

    afterEach(inject([EnvService], (service: EnvService) => {
      expect(service.getEnv).toHaveBeenCalled();
    }));

    it('should show the environment', () => {
      fixture.detectChanges();

      expectCardContentSets();

      const jdkInfo = fixture.debugElement.query(By.css('[data-e2e="jdk-info"]'));
      expect(jdkInfo.nativeElement.textContent.trim()).toBe(`${envInfo.jdk.vendor} ${envInfo.jdk.version}`);

      const osInfo = fixture.debugElement.query(By.css('[data-e2e="os-info"]'));
      expect(osInfo.nativeElement.textContent.trim()).toBe(`${envInfo.os.name} (${envInfo.os.arch})`);

      const tzInfo = fixture.debugElement.query(By.css('[data-e2e="tz-info"]'));
      expect(tzInfo.nativeElement.textContent.trim()).toBe(`${envInfo.tz.name}`);
    });

  });

  describe('loading env', () => {

    const networkDelay = 2500;

    beforeEach(inject([EnvService], (service: EnvService) => {
      spyOn(service, 'getEnv').and.returnValue(of({
        jdk: null,
        os: null,
        tz: null
      }).pipe(delay(networkDelay)));
    }));

    afterEach(inject([EnvService], (service: EnvService) => {
      expect(service.getEnv).toHaveBeenCalled();
    }));

    it('should show loading indicator', fakeAsync(() => {
      fixture.detectChanges();

      expect(fixture.debugElement.query(By.css('.progress'))).toBeTruthy();

      expectCardContentSets();

      tick(networkDelay);
    }));

  });

  describe('error loading env', () => {

    beforeEach(inject([EnvService], (service: EnvService) => {
      spyOn(service, 'getEnv').and.returnValue(of({
        jdk: null,
        os: null,
        tz: null
      }));
    }));

    afterEach(inject([EnvService], (service: EnvService) => {
      expect(service.getEnv).toHaveBeenCalled();
    }));

    it('should show error alert', () => {
      fixture.detectChanges();

      expect(fixture.debugElement.query(By.css('[data-e2e="error-env-alert"]'))).toBeTruthy();
    });

  });

  function expectCardContentSets() {
    expect(fixture.debugElement.queryAll(By.css('.card-title')).length).toBe(cardContentSets);
    expect(fixture.debugElement.queryAll(By.css('.card-text')).length).toBe(cardContentSets);
  }
});
