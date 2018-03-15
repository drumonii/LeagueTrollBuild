import { GameMapImage } from './game-map-image';

/**
 * Game map of LoL.
 */
export class GameMap {
  static crystalScarId = 8;
  static twistedTreelineId = 10;
  static summonersRiftId = 11;
  static howlingAbyssId = 12;

  mapId: number;
  mapName: string;
  image: GameMapImage;
}
