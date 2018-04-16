package com.drumonii.loltrollbuild.model.deserializer;

import com.drumonii.loltrollbuild.config.Profiles.Ddragon;
import org.springframework.boot.jackson.JsonComponent;

@JsonComponent
@Ddragon
public class GameMapDdragonDeserializer extends GameMapDeserializer {

	@Override
	protected String getMapIdField() {
		return "MapId";
	}

}
