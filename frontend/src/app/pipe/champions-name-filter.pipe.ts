import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'championsNameFilter'
})
export class ChampionsNameFilterPipe implements PipeTransform {

  transform(value: any, args?: any): any {
    return null;
  }

}
