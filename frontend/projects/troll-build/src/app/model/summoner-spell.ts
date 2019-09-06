/**
 * Summoner Spell of LoL.
 */
export interface SummonerSpell {
  id: number;
  name: string;
  description: string;
  cooldown: number[];
  key: string;
  modes: string[];
}
