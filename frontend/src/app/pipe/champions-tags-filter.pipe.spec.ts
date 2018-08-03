import { ChampionsTagsFilterPipe } from './champions-tags-filter.pipe';

import { Champion } from '@model/champion';

describe('ChampionsTagsFilterPipe', () => {
  const pipe = new ChampionsTagsFilterPipe();

  const nocture: Champion = {
    id: 56,
    key: 'Nocturne',
    name: 'Nocturne',
    title: 'the Eternal Nightmare',
    partype: 'Mana',
    info: {
      attack: 9,
      defense: 5,
      magic: 2,
      difficulty: 4
    },
    spells: [], // omitted for brevity
    passive: {
      name: 'Umbra Blades',
      description: 'Every 10 seconds, Nocturne\'s next attack strikes surrounding enemies for 120% physical damage and ' +
        'heals himself. <br><br>Nocturne\'s basic attacks reduce this cooldown by 1 second (2 against champions).'
    },
    tags: [
      'Assassin',
      'Fighter'
    ]
  };
  const syndra: Champion = {
    id: 134,
    key: 'Syndra',
    name: 'Syndra',
    title: 'the Dark Sovereign',
    partype: 'Mana',
    info: {
      attack: 2,
      defense: 3,
      magic: 9,
      difficulty: 8
    },
    spells: [], // omitted for brevity
    passive: {
      name: 'Transcendent',
      description: 'Spells gain extra effects at max rank.<br><br><font color=\'#FF9900\'>Dark Sphere</font>: ' +
        'Sphere\'s lifetime is increased to 8 seconds.<br><font color=\'#FF9900\'>Force of Will</font>: ' +
        'Deals 20% bonus true damage.<br><font color=\'#FF9900\'>Scatter the Weak</font>: Spell width increased by 50%. ' +
        '<br><font color=\'#FF9900\'>Unleashed Power</font>: Range increased by 75.'
    },
    tags: [
      'Mage',
      'Support'
    ]
  };
  const champions: Champion[] = [
    nocture, syndra
  ];

  it('should not filter Champions with empty tag', () => {
    expect(pipe.transform(champions, '')).toEqual(champions);
  });

  it('should not filter null Champions', () => {
    expect(pipe.transform(null, 'Tag')).toEqual(null);
  });

  it('should filter Champions by tag', () => {
    expect(pipe.transform(champions, 'Fighter')).toEqual([nocture]);
  });

});
