package com.drumonii.loltrollbuild.model.deserializer;

import com.drumonii.loltrollbuild.config.Profiles.StaticData;
import org.springframework.boot.jackson.JsonComponent;

@JsonComponent
@StaticData
public class SummonerSpellStaticDataDeserializer extends SummonerSpellDeserializer {

	@Override
	protected String getIdFieldName() {
		return "id";
	}

	@Override
	protected String getKeyFieldName() {
		return "key";
	}

}
