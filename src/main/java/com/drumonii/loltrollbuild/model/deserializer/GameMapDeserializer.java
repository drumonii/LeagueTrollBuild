package com.drumonii.loltrollbuild.model.deserializer;

import com.drumonii.loltrollbuild.model.GameMap;
import com.drumonii.loltrollbuild.model.builder.GameMapBuilder;
import com.drumonii.loltrollbuild.model.image.GameMapImage;
import com.drumonii.loltrollbuild.util.GameMapUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.boot.jackson.JsonObjectDeserializer;

import java.io.IOException;

public abstract class GameMapDeserializer extends JsonObjectDeserializer<GameMap> {

	protected abstract String getMapIdField();

	@Override
	protected GameMap deserializeObject(JsonParser jsonParser, DeserializationContext context, ObjectCodec codec,
			JsonNode tree) throws IOException {
		GameMapBuilder gameMapBuilder = new GameMapBuilder();

		JsonNode mapIdNode = tree.get(getMapIdField());
		gameMapBuilder.withMapId(mapIdNode.asInt());

		gameMapBuilder.withMapName(GameMapUtil.getNameFromId(mapIdNode.asInt()));

		JsonParser imageParser = tree.get("image").traverse();
		imageParser.setCodec(codec);
		GameMapImage image = imageParser.readValueAs(GameMapImage.class);
		gameMapBuilder.withImage(image);

		return gameMapBuilder.build();
	}

}
