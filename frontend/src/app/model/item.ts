import { ItemImage } from './item-image';
import { ItemGold } from './item-gold';

/**
 * Item of LoL.
 */
export class Item {
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
  image: ItemImage;
  gold: ItemGold;
}

interface ItemMap {
  [key: string]: boolean;
}
