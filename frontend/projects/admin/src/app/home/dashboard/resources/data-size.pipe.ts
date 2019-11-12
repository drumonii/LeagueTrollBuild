import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'dataSize'
})
export class DataSizePipe implements PipeTransform {

  transform(bytes: number, unit = 'byte'): number {
    switch (unit) {
      case 'mb':
        return this.getMegabytes(bytes);
      case 'gb':
        return this.getGigabytes(bytes);
      default:
        return bytes;
    }
  }

  private getMegabytes(bytes: number): number {
    const megabytes = bytes / 1_000_000;
    return +megabytes.toFixed(2);
  }

  private getGigabytes(bytes: number): number {
    const megabytes = this.getMegabytes(bytes) / 1_000;
    return +megabytes.toFixed(2);
  }

}
