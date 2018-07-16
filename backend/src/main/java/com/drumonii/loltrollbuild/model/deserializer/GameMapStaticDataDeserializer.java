package com.drumonii.loltrollbuild.model.deserializer;

import com.drumonii.loltrollbuild.config.Profiles.StaticData;
import org.springframework.boot.jackson.JsonComponent;

@JsonComponent
@StaticData
public class GameMapStaticDataDeserializer extends GameMapDeserializer {

	@Override
	protected String getMapIdField() {
		return "mapId";
	}

}
