import { Item } from '@ltb-model/item';
import { SummonerSpell } from '@ltb-model/summoner-spell';

/**
 * Troll Build (random build).
 */
export class TrollBuild {
  items: Item[];
  summonerSpells: SummonerSpell[];
  trinket: Item;
}
