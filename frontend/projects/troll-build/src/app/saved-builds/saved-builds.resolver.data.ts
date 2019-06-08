import { Build } from '@ltb-model/build';

export interface SavedBuildsResolverData {

  /**
   * The Build Id.
   */
  id: number;

  /**
   * The saved Build fetched from the service.
   */
  savedBuild: Build;

}
