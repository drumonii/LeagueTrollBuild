import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'championsTagsFilter'
})
export class ChampionsTagsFilterPipe implements PipeTransform {

  transform(value: any, args?: any): any {
    return null;
  }

}
