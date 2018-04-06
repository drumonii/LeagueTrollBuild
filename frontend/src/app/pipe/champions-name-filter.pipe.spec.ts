import { ChampionsNameFilterPipe } from './champions-name-filter.pipe';

import { Champion } from '@model/champion';

describe('ChampionsNameFilterPipe', () => {
  const pipe = new ChampionsNameFilterPipe();

  const heimer: Champion = {
    id: 74,
    key: 'Heimerdinger',
    name: 'Heimerdinger',
    title: 'the Revered Inventor',
    partype: 'Mana',
    info: {
      attack: 2,
      defense: 6,
      magic: 8,
      difficulty: 8
    },
    spells: [], // omitted for brevity
    passive: {
      name: 'Hextech Affinity',
      description: 'Gain Movement Speed while near allied towers and turrets deployed by Heimerdinger.',
      image: {
        full: 'Heimerdinger_Passive.png',
        sprite: 'passive1.png',
        group: 'passive',
        imgSrc: [],
        x: 336,
        y: 0,
        w: 48,
        h: 48
      }
    },
    image: {
      full: 'Heimerdinger.png',
      sprite: 'champion1.png',
      group: 'champion',
      imgSrc: [],
      x: 336,
      y: 0,
      w: 48,
      h: 48
    },
    tags: [
      'Mage',
      'Support'
    ]
  };
  const malphite: Champion = {
    id: 54,
    key: 'Malphite',
    name: 'Malphite',
    title: 'Shard of the Monolith',
    partype: 'Mana',
    info: {
      attack: 5,
      defense: 9,
      magic: 7,
      difficulty: 2
    },
    spells: [], // omitted for brevity
    passive: {
      name: 'Granite Shield',
      description: 'Malphite is shielded by a layer of rock which absorbs damage up to 10% of his maximum Health. ' +
        'If Malphite has not been hit for 10 seconds, this effect recharges.',
      image: {
        full: 'Malphite_GraniteShield.png',
        sprite: 'passive2.png',
        group: 'passive',
        imgSrc: [],
        x: 336,
        y: 0,
        w: 48,
        h: 48
      }
    },
    image: {
      full: 'Malphite.png',
      sprite: 'champion2.png',
      group: 'champion',
      imgSrc: [],
      x: 336,
      y: 0,
      w: 48,
      h: 48
    },
    tags: [
      'Fighter',
      'Tank'
    ]
  };
  const malzahar: Champion = {
    id: 90,
    key: 'Malzahar',
    name: 'Malzahar',
    title: 'the Prophet of the Void',
    partype: 'Mana',
    info: {
      attack: 2,
      defense: 2,
      magic: 9,
      difficulty: 6
    },
    spells: [], // omitted for brevity
    passive: {
      name: 'Void Shift',
      description: 'When he hasn\'t recently taken damage or been crowd controlled, Malzahar gains massive damage ' +
        'reduction and crowd control immunity, lingering for a short period after taking damage.<br><br>Damage from lane minions is unaffected.',
      image: {
        full: 'Malzahar_Passive.png',
        sprite: 'passive2.png',
        group: 'passive',
        imgSrc: [],
        x: 384,
        y: 0,
        w: 48,
        h: 48
      }
    },
    image: {
      full: 'Malzahar.png',
      sprite: 'champion2.png',
      group: 'champion',
      imgSrc: [],
      x: 384,
      y: 0,
      w: 48,
      h: 48
    },
    tags: [
      'Assassin',
      'Mage'
    ]
  };
  const champions: Champion[] = [
    heimer, malphite, malzahar
  ];

  it('should not filter Champions with empty name', () => {
    expect(pipe.transform(champions, '')).toEqual(champions);
  });

  it('should not filter null Champions', () => {
    expect(pipe.transform(null, 'Name')).toEqual(null);
  });

  it('should filter Champions by exact name', () => {
    expect(pipe.transform(champions, 'Heimerdinger')).toEqual([heimer]);
  });

  it('should filter Champions by name ignoring case', () => {
    expect(pipe.transform(champions, 'malphite')).toEqual([malphite]);
  });

  it('should filter Champions by substring name', () => {
    expect(pipe.transform(champions, 'mal')).toEqual([malphite, malzahar]);
  });

});
