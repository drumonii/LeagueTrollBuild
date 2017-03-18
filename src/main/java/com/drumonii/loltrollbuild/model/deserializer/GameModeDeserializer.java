package com.drumonii.loltrollbuild.model.deserializer;

import com.drumonii.loltrollbuild.model.SummonerSpell.GameMode;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * A {@link JsonDeserializer} which evaluates a Summoner Spell's {@link GameMode}s, It will ignore non Troll Build
 * related game modes and return it as {@code OTHER}.
 */
public class GameModeDeserializer extends JsonDeserializer<SortedSet<GameMode>> {

	@Override
	public SortedSet<GameMode> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
		SortedSet<GameMode> gameModes = new TreeSet<>();
		ObjectCodec oc = p.getCodec();
		JsonNode jsonNodes = oc.readTree(p);
		for (JsonNode jsonNode : jsonNodes) {
			String gameModeText = jsonNode.asText();
			GameMode gameMode;
			try {
				gameMode = GameMode.valueOf(gameModeText);
			} catch (IllegalArgumentException e) {
				gameMode = GameMode.OTHER;
			}
			gameModes.add(gameMode);
		}
		return gameModes;
	}

}
