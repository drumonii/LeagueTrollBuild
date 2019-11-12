import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';

import { forkJoin, Observable, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';

import { AdminLogger } from '@admin-service/admin-logger.service';
import { EnvResponse } from '../actuator-env-response';

@Injectable()
export class EnvService {

  constructor(private logger: AdminLogger, private httpClient: HttpClient) {}

  getEnv(): Observable<EnvInfo> {
    return forkJoin([this.getJavaVersion(), this.getJavaRuntime(), this.getOsName(), this.getOsArch(), this.getTimezone()])
      .pipe(
        map((envs) => {
          if (envs.some(env => env === null)) {
            return {
              jdk: null,
              os: null,
              tz: null
            };
          }
          return {
            jdk: {
              version: envs[0],
              vendor: envs[1]
            },
            os: {
              name: envs[2],
              arch: envs[3]
            },
            tz: {
              name: envs[4]
            }
          };
        })
      );
  }

  private getEnvProp(prop: string): Observable<string> {
    return this.httpClient.get<EnvResponse>(`/actuator/env/${prop}`)
      .pipe(
        map((env) => env.property.value),
        catchError((error: HttpErrorResponse) => {
          this.logger.error(`Caught error while GETing /actuator/env/${prop}: ${JSON.stringify(error)}`);
          return of(null);
        })
      );
  }

  getJavaVersion(): Observable<string> {
    return this.getEnvProp('java.version');
  }

  getJavaRuntime(): Observable<string> {
    return this.getEnvProp('java.runtime.name');
  }

  getOsName(): Observable<string> {
    return this.getEnvProp('os.name');
  }

  getOsArch(): Observable<string> {
    return this.getEnvProp('os.arch');
  }

  getTimezone(): Observable<string> {
    return this.getEnvProp('user.timezone');
  }

}

export interface EnvInfo {
  jdk: JdkInfo;
  os: OsInfo;
  tz: TimezoneInfo;
}

interface JdkInfo {
  version: string;
  vendor: string;
}

interface OsInfo {
  name: string;
  arch: string;
}

interface TimezoneInfo {
  name: string;
}
