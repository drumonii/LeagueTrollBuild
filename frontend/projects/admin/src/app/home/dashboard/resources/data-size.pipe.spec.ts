import { DataSizePipe } from './data-size.pipe';

describe('DataSizePipe', () => {

  const pipe = new DataSizePipe();

  describe('unknown', () => {

    it('should return', () => {
      expect(pipe.transform(89478152, 'unknown')).toBe(89478152);
    });

  });

  describe('mb', () => {

    it('should convert bytes to megabytes', () => {
      expect(pipe.transform(1000000, 'mb')).toBe(1);
    });

  });

  describe('gb', () => {

    it('should convert bytes to gigabytes', () => {
      expect(pipe.transform(1000000000, 'gb')).toBe(1);
    });

  });
});
