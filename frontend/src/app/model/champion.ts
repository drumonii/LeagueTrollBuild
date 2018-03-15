import { ChampionInfo } from './champion-info';
import { ChampionImage } from './champion-image';
import { ChampionSpell } from './champion-spell';
import { ChampionPassive } from './champion-passive';

/**
 * Champion of LoL.
 */
export class Champion {
  id: number;
  key: string;
  name: string;
  title: string;
  partype: string;
  info: ChampionInfo;
  spells: ChampionSpell[];
  passive: ChampionPassive;
  image: ChampionImage;
  tags: string[];
}
