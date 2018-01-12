package com.drumonii.loltrollbuild.model.deserializer;

import com.drumonii.loltrollbuild.config.Profiles.StaticData;
import com.drumonii.loltrollbuild.model.SummonerSpell;
import com.drumonii.loltrollbuild.model.builder.SummonerSpellBuilder;
import com.drumonii.loltrollbuild.model.image.SummonerSpellImage;
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
public class SummonerSpellStaticDataDeserializer extends JsonObjectDeserializer<SummonerSpell> {

	@Override
	protected SummonerSpell deserializeObject(JsonParser jsonParser, DeserializationContext context, ObjectCodec codec,
			JsonNode tree) throws IOException {
		SummonerSpellBuilder summonerSpellBuilder = new SummonerSpellBuilder();

		JsonNode idNode = tree.get("id");
		summonerSpellBuilder.withId(idNode.asInt());

		JsonNode nameNode = tree.get("name");
		summonerSpellBuilder.withName(nullSafeValue(nameNode, String.class));

		JsonNode descriptionNode = tree.get("description");
		summonerSpellBuilder.withDescription(nullSafeValue(descriptionNode, String.class));

		JsonNode keyNode = tree.get("key");
		summonerSpellBuilder.withKey(keyNode.asText());

		JsonParser imageParser = tree.get("image").traverse();
		imageParser.setCodec(codec);
		SummonerSpellImage image = imageParser.readValueAs(SummonerSpellImage.class);
		summonerSpellBuilder.withImage(image);

		JsonParser cooldownParser = tree.get("cooldown").traverse();
		cooldownParser.setCodec(codec);
		TreeNode cooldownsTreeNode = cooldownParser.readValueAsTree();
		summonerSpellBuilder.withCooldown(codec.treeToValue(cooldownsTreeNode, Integer[].class));

		JsonNode modesNode = tree.get("modes");
		if (modesNode != null) {
			JsonParser modesParser = tree.get("modes").traverse();
			modesParser.setCodec(codec);
			TreeNode modesTreeNode = modesParser.readValueAsTree();
			summonerSpellBuilder.withModes(codec.treeToValue(modesTreeNode, String[].class));
		}

		return summonerSpellBuilder.build();
	}

}
