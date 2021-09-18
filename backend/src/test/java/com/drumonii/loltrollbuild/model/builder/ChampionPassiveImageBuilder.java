package com.drumonii.loltrollbuild.model.builder;

import com.drumonii.loltrollbuild.model.image.ChampionPassiveImage;

/**
 * Builder for {@link ChampionPassiveImage}s.
 */
public final class ChampionPassiveImageBuilder extends ImageBuilder<ChampionPassiveImageBuilder> {

	@Override
	public ChampionPassiveImage build() {
		ChampionPassiveImage championPassiveImage = new ChampionPassiveImage();
		championPassiveImage.setFull(full);
		championPassiveImage.setSprite(sprite);
		championPassiveImage.setGroup(group);
		championPassiveImage.setX(x);
		championPassiveImage.setY(y);
		championPassiveImage.setW(w);
		championPassiveImage.setH(h);
		return championPassiveImage;
	}

}
