import { NextScheduledFromNowPipe } from './next-scheduled-from-now.pipe';

describe('NextScheduledFromNowPipe', () => {
  const pipe = new NextScheduledFromNowPipe('en-US');

  it('should not get from null date', () => {
    expect(pipe.transform(null)).toBe('');
  });

  it('should get from date', () => {
    const now = new Date();
    now.setHours(now.getHours() + 1);

    expect(pipe.transform(now)).toContain('(1 hour, 0 minutes from now)');
  });
});
