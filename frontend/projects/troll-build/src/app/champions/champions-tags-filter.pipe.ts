import { Pipe, PipeTransform } from '@angular/core';

import { Champion } from '@ltb-model/champion';

@Pipe({
  name: 'championsTagsFilter'
})
export class ChampionsTagsFilterPipe implements PipeTransform {

  transform(champions: Champion[], filterTag: string): Champion[] {
    if (filterTag && champions) {
      return champions.filter(champion => champion.tags.some(tag => tag === filterTag));
    }
    return champions;
  }

}
