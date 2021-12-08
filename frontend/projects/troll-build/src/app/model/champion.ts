import { ChampionInfo } from './champion-info';
import { ChampionSpell } from './champion-spell';
import { ChampionPassive } from './champion-passive';

/**
 * Champion of LoL.
 */
export interface Champion {
  id: number;
  key: string;
  name: string;
  title: string;
  partype: string;
  info?: ChampionInfo;
  spells?: ChampionSpell[];
  passive?: ChampionPassive;
  tags: string[];
}
