import { LoadingBarModule } from './loading-bar.module';

describe('LoadingBarModule', () => {
  let loadingBarModule: LoadingBarModule;

  beforeEach(() => {
    loadingBarModule = new LoadingBarModule();
  });

  it('should create an instance', () => {
    expect(loadingBarModule).toBeTruthy();
  });
});
