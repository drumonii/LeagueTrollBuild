package com.drumonii.loltrollbuild.model.builder;

import com.drumonii.loltrollbuild.model.image.ChampionImage;

/**
 * Builder for {@link ChampionImage}s.
 */
public final class ChampionImageBuilder extends ImageBuilder<ChampionImageBuilder> {

	public ChampionImage build() {
		ChampionImage championImage = new ChampionImage();
		championImage.setFull(full);
		championImage.setSprite(sprite);
		championImage.setGroup(group);
		championImage.setX(x);
		championImage.setY(y);
		championImage.setW(w);
		championImage.setH(h);
		return championImage;
	}

}
