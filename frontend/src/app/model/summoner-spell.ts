import { SummonerSpellImage } from './summoner-spell-image';

/**
 * Summoner Spell of LoL.
 */
export class SummonerSpell {
  id: number;
  name: string;
  description: string;
  image: SummonerSpellImage;
  cooldown: number[];
  key: string;
  modes: string[];
}
