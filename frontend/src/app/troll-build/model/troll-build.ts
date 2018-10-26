import { Item } from '@model/item';
import { SummonerSpell } from '@model/summoner-spell';

/**
 * Troll Build (random build).
 */
export class TrollBuild {
  items: Item[];
  summonerSpells: SummonerSpell[];
  trinket: Item;
}
