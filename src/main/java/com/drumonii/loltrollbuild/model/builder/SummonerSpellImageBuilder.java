package com.drumonii.loltrollbuild.model.builder;

import com.drumonii.loltrollbuild.model.image.SummonerSpellImage;

/**
 * Builder for {@link SummonerSpellImage}s.
 */
public final class SummonerSpellImageBuilder extends ImageBuilder<SummonerSpellImageBuilder> {

	@Override
	public SummonerSpellImage build() {
		SummonerSpellImage summonerSpellImage = new SummonerSpellImage();
		summonerSpellImage.setFull(full);
		summonerSpellImage.setSprite(sprite);
		summonerSpellImage.setGroup(group);
		summonerSpellImage.setX(x);
		summonerSpellImage.setY(y);
		summonerSpellImage.setW(w);
		summonerSpellImage.setH(h);
		return summonerSpellImage;
	}

}
