package com.drumonii.loltrollbuild.model.builder;

import com.drumonii.loltrollbuild.model.ChampionPassive;
import com.drumonii.loltrollbuild.model.image.ChampionPassiveImage;

/**
 * Builder for {@link ChampionPassive}.
 */
public final class ChampionPassiveBuilder {

	private String name;
	private String description;
	private ChampionPassiveImage image;

	public ChampionPassiveBuilder withName(String name) {
		this.name = name;
		return this;
	}

	public ChampionPassiveBuilder withDescription(String description) {
		this.description = description;
		return this;
	}

	public ChampionPassiveBuilder withImage(ChampionPassiveImage image) {
		this.image = image;
		return this;
	}

	public ChampionPassive build() {
		ChampionPassive championPassive = new ChampionPassive();
		championPassive.setName(name);
		championPassive.setDescription(description);
		championPassive.setImage(image);
		return championPassive;
	}

}
