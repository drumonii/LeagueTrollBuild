import { Pipe, PipeTransform } from '@angular/core';

import { Champion } from '@ltb-model/champion';

@Pipe({
  name: 'championsNameFilter'
})
export class ChampionsNameFilterPipe implements PipeTransform {

  transform(champions: Champion[], championsSearchName: string): Champion[] {
    if (championsSearchName && champions) {
      return champions.filter(champion =>
        champion.name.toLowerCase().includes(championsSearchName.toLowerCase()));
    }
    return champions;
  }

}
