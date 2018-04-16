package com.drumonii.loltrollbuild.model.deserializer;

import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.model.ChampionInfo;
import com.drumonii.loltrollbuild.model.ChampionPassive;
import com.drumonii.loltrollbuild.model.ChampionSpell;
import com.drumonii.loltrollbuild.model.builder.ChampionBuilder;
import com.drumonii.loltrollbuild.model.image.ChampionImage;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.boot.jackson.JsonObjectDeserializer;

import java.io.IOException;

public abstract class ChampionDeserializer extends JsonObjectDeserializer<Champion> {

	protected abstract String getIdFieldName();

	protected abstract String getKeyFieldName();

	protected abstract ChampionSpell[] getSpells(ObjectCodec codec, JsonNode tree) throws IOException;

	protected abstract ChampionPassive getPassive(ObjectCodec codec, JsonNode tree) throws IOException;

	@Override
	protected Champion deserializeObject(JsonParser jsonParser, DeserializationContext context, ObjectCodec codec,
			JsonNode tree) throws IOException {
		ChampionBuilder championBuilder = new ChampionBuilder();

		JsonNode idNode = tree.get(getIdFieldName());
		championBuilder.withId(idNode.asInt());

		JsonNode keyNode = tree.get(getKeyFieldName());
		championBuilder.withKey(keyNode.asText());

		JsonNode nameNode = tree.get("name");
		championBuilder.withName(nullSafeValue(nameNode, String.class));

		JsonNode titleNode = tree.get("title");
		championBuilder.withTitle(nullSafeValue(titleNode, String.class));

		JsonNode partypeNode = tree.get("partype");
		championBuilder.withPartype(nullSafeValue(partypeNode, String.class));

		JsonParser infoParser = tree.get("info").traverse();
		infoParser.setCodec(codec);
		ChampionInfo info = infoParser.readValueAs(ChampionInfo.class);
		championBuilder.withInfo(info);

		championBuilder.withSpells(getSpells(codec, tree));

		championBuilder.withPassive(getPassive(codec, tree));

		JsonParser imageParser = tree.get("image").traverse();
		imageParser.setCodec(codec);
		ChampionImage image = imageParser.readValueAs(ChampionImage.class);
		championBuilder.withImage(image);

		JsonParser tagsParser = tree.get("tags").traverse();
		tagsParser.setCodec(codec);
		TreeNode tagsTreeNode = tagsParser.readValueAsTree();
		championBuilder.withTags(codec.treeToValue(tagsTreeNode, String[].class));

		return championBuilder.build();
	}

}
