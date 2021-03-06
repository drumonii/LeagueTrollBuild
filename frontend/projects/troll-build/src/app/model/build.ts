import { Champion } from '@ltb-model/champion';
import { GameMap } from '@ltb-model/game-map';
import { Item } from '@ltb-model/item';
import { SummonerSpell } from '@ltb-model/summoner-spell';

/**
 * Saved Troll Build.
 */
export interface Build {
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

/**
 * Builder for Builds.
 */
export class BuildBuilder {

  private championId: number;
  private item1Id: number;
  private item2Id: number;
  private item3Id: number;
  private item4Id: number;
  private item5Id: number;
  private item6Id: number;
  private summonerSpell1Id: number;
  private summonerSpell2Id: number;
  private trinketId: number;
  private mapId: number;

  withChampion(champion: Champion): BuildBuilder {
    this.championId = champion.id;
    return this;
  }

  withItems(items: Item[]): BuildBuilder {
    for (let i = 0; i < items.length; i++) {
      switch (i) {
        case 0:
          this.item1Id = items[i].id;
          break;
        case 1:
          this.item2Id = items[i].id;
          break;
        case 2:
          this.item3Id = items[i].id;
          break;
        case 3:
          this.item4Id = items[i].id;
          break;
        case 4:
          this.item5Id = items[i].id;
          break;
        case 5:
          this.item6Id = items[i].id;
          break;
      }
    }
    return this;
  }

  withSummonerSpells(summonerSpells: SummonerSpell[]): BuildBuilder {
    for (let i = 0; i < summonerSpells.length; i++) {
      switch (i) {
        case 0:
          this.summonerSpell1Id = summonerSpells[i].id;
          break;
        case 1:
          this.summonerSpell2Id = summonerSpells[i].id;
          break;
      }
    }
    return this;
  }

  withTrinket(trinket: Item): BuildBuilder {
    this.trinketId = trinket.id;
    return this;
  }

  withGameMap(gameMap: GameMap): BuildBuilder {
    this.mapId = gameMap.mapId;
    return this;
  }

  build(): Build {
    return {
      championId: this.championId,
      item1Id: this.item1Id,
      item2Id: this.item2Id,
      item3Id: this.item3Id,
      item4Id: this.item4Id,
      item5Id: this.item5Id,
      item6Id: this.item6Id,
      mapId: this.mapId,
      summonerSpell1Id: this.summonerSpell1Id,
      summonerSpell2Id: this.summonerSpell2Id,
      trinketId: this.trinketId
    };
  }

}

/**
 * Build type enumeration depending of what build was returned from the API.
 */
export enum BuildType {

  Ok = 'Ok', NotFound = 'NotFound', InvalidItems = 'InvalidItems', InvalidSummonerSpells = 'InvalidSummonerSpells',
  InvalidTrinket = 'InvalidTrinket'

}
