import { TestBed } from '@angular/core/testing';

import { Logger } from './logger.service';

describe('Logger', () => {
  let logger: Logger;
  let debugSpy: jasmine.Spy;
  let logSpy: jasmine.Spy;
  let warnSpy: jasmine.Spy;
  let errorSpy: jasmine.Spy;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    logger = TestBed.get(Logger);

    debugSpy = spyOn(console, 'debug');
    logSpy = spyOn(console, 'log');
    warnSpy = spyOn(console, 'warn');
    errorSpy = spyOn(console, 'error');
  });

  describe('debug', () => {

    it('should delegate to console.debug', () => {
      logger.debug('message');
      expect(debugSpy).toHaveBeenCalledWith('message');

      logger.debug('message', 'param1', 'param2');
      expect(debugSpy).toHaveBeenCalledWith('message', 'param1', 'param2');
    });

  });

  describe('info', () => {

    it('should delegate to console.log', () => {
      logger.info('message');
      expect(logSpy).toHaveBeenCalledWith('message');

      logger.info('message', 'param1', 'param2');
      expect(logSpy).toHaveBeenCalledWith('message', 'param1', 'param2');
    });

  });

  describe('warn', () => {

    it('should delegate to console.warn', () => {
      logger.warn('message');
      expect(warnSpy).toHaveBeenCalledWith('message');

      logger.warn('message', 'param1', 'param2');
      expect(warnSpy).toHaveBeenCalledWith('message', 'param1', 'param2');
    });

  });

  describe('error', () => {

    it('should delegate to console.error', () => {
      logger.error('message');
      expect(errorSpy).toHaveBeenCalledWith('message');

      logger.error('message', 'param1', 'param2');
      expect(errorSpy).toHaveBeenCalledWith('message', 'param1', 'param2');
    });

  });

});
