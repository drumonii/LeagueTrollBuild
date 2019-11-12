import { inject, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController, RequestMatch } from '@angular/common/http/testing';

import { of } from 'rxjs';

import { EnvService } from './env.service';
import { EnvResponse } from '../actuator-env-response';

describe('EnvService', () => {
  const getJavaVersionRequestMatch: RequestMatch = {
    method: 'GET',
    url: '/actuator/env/java.version'
  };

  const getJavaRuntimeRequestMatch: RequestMatch = {
    method: 'GET',
    url: '/actuator/env/java.runtime.name'
  };

  const getOsNameRequestMatch: RequestMatch = {
    method: 'GET',
    url: '/actuator/env/os.name'
  };

  const getOsArchRequestMatch: RequestMatch = {
    method: 'GET',
    url: '/actuator/env/os.arch'
  };

  const getTzRequestMatch: RequestMatch = {
    method: 'GET',
    url: '/actuator/env/user.timezone'
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [EnvService]
    });
  });

  afterEach(inject([HttpTestingController], (httpMock: HttpTestingController) => {
    httpMock.verify();
  }));

  describe('getEnv', () => {

    it('should get the environment', inject([EnvService], (service: EnvService) => {
      spyOn(service, 'getJavaVersion').and.returnValue(of(''));
      spyOn(service, 'getJavaRuntime').and.returnValue(of(''));
      spyOn(service, 'getOsName').and.returnValue(of(''));
      spyOn(service, 'getOsArch').and.returnValue(of(''));
      spyOn(service, 'getTimezone').and.returnValue(of(''));

      service.getEnv().subscribe(env => {
        expect(env).not.toEqual({
          jdk: null,
          os: null,
          tz: null
        });
      });
    }));

    it('should get the environment with REST error', inject([EnvService], (service: EnvService) => {
      spyOn(service, 'getJavaVersion').and.returnValue(of(null));
      spyOn(service, 'getJavaRuntime').and.returnValue(of(null));
      spyOn(service, 'getOsName').and.returnValue(of(null));
      spyOn(service, 'getOsArch').and.returnValue(of(null));
      spyOn(service, 'getTimezone').and.returnValue(of(null));

      service.getEnv().subscribe(env => {
        expect(env).toEqual({
          jdk: null,
          os: null,
          tz: null
        });
      });
    }));

  });

  describe('getJavaVersion', () => {

    it('should get the java version', inject([EnvService, HttpTestingController],
      (service: EnvService, httpMock: HttpTestingController) => {
      const mockJavaVersion: EnvResponse = {
        property: {
          source: 'systemProperties',
          value: '11.0.2'
        },
      };

      service.getJavaVersion().subscribe(javaVersion => {
        expect(javaVersion).toBe(mockJavaVersion.property.value);
      });

      const testReq = httpMock.expectOne(getJavaVersionRequestMatch);

      testReq.flush(mockJavaVersion);
    }));

    it('should get the java version with REST error', inject([EnvService, HttpTestingController],
      (service: EnvService, httpMock: HttpTestingController) => {
      service.getJavaVersion().subscribe(javaVersion => {
        expect(javaVersion).toBeNull();
      });

      const testReq = httpMock.expectOne(getJavaVersionRequestMatch);

      testReq.error(new ErrorEvent('An unexpected error occurred'));
    }));

  });

  describe('getJavaRuntime', () => {

    it('should get the java runtime', inject([EnvService, HttpTestingController],
      (service: EnvService, httpMock: HttpTestingController) => {
      const mockJavaRuntime: EnvResponse = {
        property: {
          source: 'systemProperties',
          value: 'OpenJDK Runtime Environment'
        },
      };

      service.getJavaRuntime().subscribe(javaRuntime => {
        expect(javaRuntime).toBe(mockJavaRuntime.property.value);
      });

      const testReq = httpMock.expectOne(getJavaRuntimeRequestMatch);

      testReq.flush(mockJavaRuntime);
    }));

    it('should get the java runtime with REST error', inject([EnvService, HttpTestingController],
      (service: EnvService, httpMock: HttpTestingController) => {
      service.getJavaRuntime().subscribe(javaRuntime => {
        expect(javaRuntime).toBeNull();
      });

      const testReq = httpMock.expectOne(getJavaRuntimeRequestMatch);

      testReq.error(new ErrorEvent('An unexpected error occurred'));
    }));

  });

  describe('getOsName', () => {

    it('should get the OS name', inject([EnvService, HttpTestingController],
      (service: EnvService, httpMock: HttpTestingController) => {
      const mockOsName: EnvResponse = {
        property: {
          source: 'systemProperties',
          value: 'Windows 10'
        },
      };

      service.getOsName().subscribe(osName => {
        expect(osName).toBe(mockOsName.property.value);
      });

      const testReq = httpMock.expectOne(getOsNameRequestMatch);

      testReq.flush(mockOsName);
    }));

    it('should get the OS name with REST error', inject([EnvService, HttpTestingController],
      (service: EnvService, httpMock: HttpTestingController) => {
      service.getOsName().subscribe(osName => {
        expect(osName).toBeNull();
      });

      const testReq = httpMock.expectOne(getOsNameRequestMatch);

      testReq.error(new ErrorEvent('An unexpected error occurred'));
    }));

  });

  describe('getOsArch', () => {

    it('should get the OS architecture', inject([EnvService, HttpTestingController],
      (service: EnvService, httpMock: HttpTestingController) => {
      const mockOsArch: EnvResponse = {
        property: {
          source: 'systemProperties',
          value: 'amd64'
        },
      };

      service.getOsArch().subscribe(osArch => {
        expect(osArch).toBe(mockOsArch.property.value);
      });

      const testReq = httpMock.expectOne(getOsArchRequestMatch);

      testReq.flush(mockOsArch);
    }));

    it('should get the OS architecture with REST error', inject([EnvService, HttpTestingController],
      (service: EnvService, httpMock: HttpTestingController) => {
      service.getOsArch().subscribe(osArch => {
        expect(osArch).toBeNull();
      });

      const testReq = httpMock.expectOne(getOsArchRequestMatch);

      testReq.error(new ErrorEvent('An unexpected error occurred'));
    }));

  });

  describe('getTimezone', () => {

    it('should get the timezone', inject([EnvService, HttpTestingController],
      (service: EnvService, httpMock: HttpTestingController) => {
      const mockOsArch: EnvResponse = {
        property: {
          source: 'systemProperties',
          value: 'America/New_York'
        },
      };

      service.getTimezone().subscribe(tz => {
        expect(tz).toBe(mockOsArch.property.value);
      });

      const testReq = httpMock.expectOne(getTzRequestMatch);

      testReq.flush(mockOsArch);
    }));

    it('should get the timezone with REST error', inject([EnvService, HttpTestingController],
      (service: EnvService, httpMock: HttpTestingController) => {
      service.getTimezone().subscribe(tz => {
        expect(tz).toBeNull();
      });

      const testReq = httpMock.expectOne(getTzRequestMatch);

      testReq.error(new ErrorEvent('An unexpected error occurred'));
    }));

  });
});
