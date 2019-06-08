import { Injectable, isDevMode } from '@angular/core';

/**
 * Logging service that delegates to the console depending on log level: info, warn, error and whether if in
 * development mode.
 */
@Injectable({
  providedIn: 'root'
})
export class AdminLogger {

  private readonly isDev: boolean;

  constructor() {
    this.isDev = isDevMode();
  }

  /**
   * Logs the message and params, if present, as a console.log only if in development mode.
   *
   * @param message the log message
   * @param optionalParams the optional params
   */
  info(message: any, ...optionalParams: any[]): void {
    if (this.isDev) {
      console.log(message, ...optionalParams);
    }
  }

  /**
   * Logs the message and params, if present, as a console.warn only if in development mode.
   *
   * @param message the warn message
   * @param optionalParams the optional params
   */
  warn(message: any, ...optionalParams: any[]): void {
    if (this.isDev) {
      console.warn(message, ...optionalParams);
    }
  }

  /**
   * Logs the message and params, if present, as a console.error regardless of if in development mode.
   *
   * @param message the error message
   * @param optionalParams the optional params
   */
  error(message: any, ...optionalParams: any[]): void {
    console.error(message, ...optionalParams);
  }

}
