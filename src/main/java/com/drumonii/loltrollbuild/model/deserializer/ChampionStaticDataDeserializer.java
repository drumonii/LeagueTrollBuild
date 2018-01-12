package com.drumonii.loltrollbuild.model.deserializer;

import com.drumonii.loltrollbuild.config.Profiles.StaticData;
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
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.boot.jackson.JsonObjectDeserializer;

import java.io.IOException;

@JsonComponent
@StaticData
public class ChampionStaticDataDeserializer extends JsonObjectDeserializer<Champion> {

	@Override
	protected Champion deserializeObject(JsonParser jsonParser, DeserializationContext context, ObjectCodec codec,
			JsonNode tree) throws IOException {
		ChampionBuilder championBuilder = new ChampionBuilder();

		JsonNode idNode = tree.get("id");
		championBuilder.withId(idNode.asInt());

		JsonNode keyNode = tree.get("key");
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

		JsonParser spellsParser = tree.get("spells").traverse();
		spellsParser.setCodec(codec);
		TreeNode spellsTreeNode = spellsParser.readValueAsTree();
		championBuilder.withSpells(codec.treeToValue(spellsTreeNode, ChampionSpell[].class));

		JsonParser passiveParser = tree.get("passive").traverse();
		passiveParser.setCodec(codec);
		ChampionPassive passive = passiveParser.readValueAs(ChampionPassive.class);
		championBuilder.withPassive(passive);

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
