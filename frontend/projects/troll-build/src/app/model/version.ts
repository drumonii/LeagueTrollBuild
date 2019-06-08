/**
 * LoL patch version.
 */
export class Version {
  patch: string;
  major: number;
  minor: number;
  revision: number;

  constructor(patch: string) {
    const versioning: string[] = patch.split('.');
    this.patch = patch;
    this.major = +versioning[0];
    this.minor = +versioning[1];
    this.revision = +versioning[2];
  }

}
