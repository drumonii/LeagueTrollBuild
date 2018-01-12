package com.drumonii.loltrollbuild.model.deserializer;

import com.drumonii.loltrollbuild.config.Profiles.Ddragon;
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
@Ddragon
public class ChampionDdragonDeserializer extends JsonObjectDeserializer<Champion> {

	@Override
	protected Champion deserializeObject(JsonParser jsonParser, DeserializationContext context, ObjectCodec codec,
			JsonNode tree) throws IOException {
		ChampionBuilder championBuilder = new ChampionBuilder();

		JsonNode idNode = tree.get("key");
		championBuilder.withId(idNode.asInt());

		JsonNode keyNode = tree.get("id");
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

		JsonNode spellsNode = tree.get("spells");
		if (spellsNode != null) {
			JsonParser spellsParser = spellsNode.traverse();
			spellsParser.setCodec(codec);
			TreeNode spellsTreeNode = spellsParser.readValueAsTree();
			ChampionSpell[] spells = codec.treeToValue(spellsTreeNode, ChampionSpell[].class);
			for (int i = 0; i < spells.length; i++) {
				TreeNode keyTreeNode = spellsTreeNode.get(i).get("id");
				String key = codec.treeToValue(keyTreeNode, String.class);
				ChampionSpell spell = spells[i];
				spell.setKey(key);
			}
			championBuilder.withSpells(spells);
		}

		JsonNode passiveNode = tree.get("passive");
		if (passiveNode != null) {
			JsonParser passiveJsonParser = passiveNode.traverse();
			passiveJsonParser.setCodec(codec);
			ChampionPassive passive = passiveJsonParser.readValueAs(ChampionPassive.class);
			championBuilder.withPassive(passive);
		}

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
