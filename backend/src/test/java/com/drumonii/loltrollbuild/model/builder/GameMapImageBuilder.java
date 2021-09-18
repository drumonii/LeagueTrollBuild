package com.drumonii.loltrollbuild.model.builder;

import com.drumonii.loltrollbuild.model.image.GameMapImage;

/**
 * Builder for {@link GameMapImage}s.
 */
public final class GameMapImageBuilder extends ImageBuilder<GameMapImageBuilder> {

	@Override
	public GameMapImage build() {
		GameMapImage gameMapImage = new GameMapImage();
		gameMapImage.setFull(full);
		gameMapImage.setSprite(sprite);
		gameMapImage.setGroup(group);
		gameMapImage.setX(x);
		gameMapImage.setY(y);
		gameMapImage.setW(w);
		gameMapImage.setH(h);
		return gameMapImage;
	}

}
