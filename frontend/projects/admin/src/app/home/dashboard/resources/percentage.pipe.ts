import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'percentage'
})
export class PercentagePipe implements PipeTransform {

  transform(value: number): string {
    if (!value) {
      return '0%';
    }
    return (value * 100).toFixed(2) + '%';
  }

}
