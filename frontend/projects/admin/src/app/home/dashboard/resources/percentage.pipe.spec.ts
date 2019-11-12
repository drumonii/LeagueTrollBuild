import { PercentagePipe } from './percentage.pipe';

describe('PercentagePipe', () => {
  const pipe = new PercentagePipe();

  it('should get the percentage', () => {
    expect(pipe.transform(0.254783261)).toBe('25.48%');
  });

  it('should not get percentage from null', () => {
    expect(pipe.transform(null)).toBe('0%');
  });
});
