import { ItemGold } from './item-gold';

/**
 * Item of LoL.
 */
export interface Item {
  id: number;
  name: string;
  group: string;
  consumed: boolean;
  description: string;
  from: number[];
  into: number[];
  requiredChampion: string;
  requiredAlly: string;
  maps: ItemMap;
  gold: ItemGold;
}

interface ItemMap {
  [key: string]: boolean;
}
