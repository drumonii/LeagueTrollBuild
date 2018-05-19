package com.drumonii.loltrollbuild.model.builder;

import com.drumonii.loltrollbuild.model.image.ChampionSpellImage;

/**
 * Builder for {@link ChampionSpellImage}s.
 */
public final class ChampionSpellImageBuilder extends ImageBuilder<ChampionSpellImageBuilder> {

	@Override
	public ChampionSpellImage build() {
		ChampionSpellImage championSpellImage = new ChampionSpellImage();
		championSpellImage.setFull(full);
		championSpellImage.setSprite(sprite);
		championSpellImage.setGroup(group);
		championSpellImage.setX(x);
		championSpellImage.setY(y);
		championSpellImage.setW(w);
		championSpellImage.setH(h);
		return championSpellImage;
	}

}
