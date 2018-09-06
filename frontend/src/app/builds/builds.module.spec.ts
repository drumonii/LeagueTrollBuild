import { BuildsModule } from './builds.module';

describe('BuildsModule', () => {
  let buildsModule: BuildsModule;

  beforeEach(() => {
    buildsModule = new BuildsModule();
  });

  it('should create an instance', () => {
    expect(buildsModule).toBeTruthy();
  });
});
