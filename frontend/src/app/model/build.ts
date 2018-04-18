import { Champion } from '@model/champion';
import { GameMap } from '@model/game-map';
import { Item } from '@model/item';
import { SummonerSpell } from '@model/summoner-spell';

/**
 * Saved Troll Build.
 */
export class Build {
  id?: number;
  championId: number;
  item1Id: number; // boots
  item2Id: number;
  item3Id: number;
  item4Id: number;
  item5Id: number;
  item6Id: number;
  summonerSpell1Id: number;
  summonerSpell2Id: number;
  trinketId: number;
  mapId: number;
  champion?: Champion;
  item1?: Item; // boots
  item2?: Item;
  item3?: Item;
  item4?: Item;
  item5?: Item;
  item6?: Item;
  summonerSpell1?: SummonerSpell;
  summonerSpell2?: SummonerSpell;
  trinket?: Item;
  map?: GameMap;
  selfRef?: string;
}
