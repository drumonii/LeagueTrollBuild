package com.drumonii.loltrollbuild.model.builder;

import com.drumonii.loltrollbuild.model.GameMap;
import com.drumonii.loltrollbuild.model.image.GameMapImage;

/**
 * Builder for {@link GameMap}s.
 */
public final class GameMapBuilder {

	private int mapId;
	private String mapName;
	private GameMapImage image;

	public GameMapBuilder withMapId(int mapId) {
		this.mapId = mapId;
		return this;
	}

	public GameMapBuilder withMapName(String mapName) {
		this.mapName = mapName;
		return this;
	}

	public GameMapBuilder withImage(GameMapImage image) {
		this.image = image;
		return this;
	}

	public GameMap build() {
		GameMap gameMap = new GameMap();
		gameMap.setMapId(mapId);
		gameMap.setMapName(mapName);
		gameMap.setImage(image);
		if (image != null) {
			image.setMap(gameMap);
		}
		return gameMap;
	}

}
