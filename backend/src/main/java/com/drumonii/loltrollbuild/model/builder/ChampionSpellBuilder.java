package com.drumonii.loltrollbuild.model.builder;

import com.drumonii.loltrollbuild.model.ChampionSpell;
import com.drumonii.loltrollbuild.model.image.ChampionSpellImage;

/**
 * Builder for {@link ChampionSpell}s.
 */
public final class ChampionSpellBuilder {

	private String key;
	private String name;
	private String description;
	private String tooltip = "";
	private ChampionSpellImage image;

	public ChampionSpellBuilder withKey(String key) {
		this.key = key;
		return this;
	}

	public ChampionSpellBuilder withName(String name) {
		this.name = name;
		return this;
	}

	public ChampionSpellBuilder withDescription(String description) {
		this.description = description;
		return this;
	}

	public ChampionSpellBuilder withTooltip(String tooltip) {
		this.tooltip = tooltip;
		return this;
	}

	public ChampionSpellBuilder withImage(ChampionSpellImage image) {
		this.image = image;
		return this;
	}

	public ChampionSpell build() {
		ChampionSpell championSpell = new ChampionSpell();
		championSpell.setKey(key);
		championSpell.setName(name);
		championSpell.setDescription(description);
		championSpell.setTooltip(tooltip);
		championSpell.setImage(image);
		return championSpell;
	}

}
