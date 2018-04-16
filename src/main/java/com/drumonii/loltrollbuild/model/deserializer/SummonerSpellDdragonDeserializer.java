package com.drumonii.loltrollbuild.model.deserializer;

import com.drumonii.loltrollbuild.config.Profiles.Ddragon;
import org.springframework.boot.jackson.JsonComponent;

@JsonComponent
@Ddragon
public class SummonerSpellDdragonDeserializer extends SummonerSpellDeserializer {

	@Override
	protected String getIdFieldName() {
		return "key";
	}

	@Override
	protected String getKeyFieldName() {
		return "id";
	}

}
