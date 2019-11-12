import { Inject, LOCALE_ID, Pipe, PipeTransform } from '@angular/core';
import { DatePipe } from '@angular/common';

@Pipe({
  name: 'nextScheduledFromNow'
})
export class NextScheduledFromNowPipe implements PipeTransform {

  constructor(@Inject(LOCALE_ID) private locale: string) {}

  transform(date: Date, format = 'medium'): string {
    if (!date) {
      return '';
    }

    const datePipe = new DatePipe(this.locale);
    const dateFormatted = datePipe.transform(date, format);

    const now = new Date();
    const difference = Math.abs(date.getTime() - now.getTime());

    const seconds = Math.round((new Date(difference)).getTime() / 1000);
    const hours = Math.floor(seconds % (3600 * 24) / 3600);
    const minutes = Math.floor(seconds % 3600 / 60);

    const hoursDisplay = hours > 0 ? hours + (hours === 1 ? ' hour, ' : ' hours, ') : '';
    const minutesDisplay = minutes >= 0 ? minutes + (minutes === 1 ? ' minute' : ' minutes') : '';

    return `${dateFormatted} (${hoursDisplay + minutesDisplay} from now)`;
  }

}
