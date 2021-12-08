import { Item } from '@ltb-model/item';
import { SummonerSpell } from '@ltb-model/summoner-spell';

/**
 * Troll Build (random build).
 */
export interface TrollBuild {
  items: Item[] | null;
  summonerSpells: SummonerSpell[] | null;
  trinket: Item | null;
}
