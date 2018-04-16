package com.drumonii.loltrollbuild.model.deserializer;

import com.drumonii.loltrollbuild.config.Profiles.Ddragon;
import com.drumonii.loltrollbuild.model.image.ItemImage;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
@Ddragon
public class ItemDdragonDeserializer extends ItemDeserializer {

	@Override
	protected int getId(ObjectCodec codec, JsonNode tree) throws IOException {
		JsonParser imageParser = tree.get("image").traverse();
		imageParser.setCodec(codec);
		ItemImage image = imageParser.readValueAs(ItemImage.class);

		// Ddragon doesn't have an id so have to parse it from the image full
		return Integer.parseInt(image.getFull().substring(0, image.getFull().lastIndexOf('.')));
	}

	@Override
	protected String getRequiredAlly(ObjectCodec codec, JsonNode tree) throws IOException {
		JsonNode requiredAllyNode = tree.get("requiredAlly");
		return nullSafeValue(requiredAllyNode, String.class);
	}

}
