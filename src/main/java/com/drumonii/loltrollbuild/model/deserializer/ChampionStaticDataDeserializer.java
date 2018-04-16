package com.drumonii.loltrollbuild.model.deserializer;

import com.drumonii.loltrollbuild.config.Profiles.StaticData;
import com.drumonii.loltrollbuild.model.ChampionPassive;
import com.drumonii.loltrollbuild.model.ChampionSpell;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
@StaticData
public class ChampionStaticDataDeserializer extends ChampionDeserializer {

	@Override
	protected String getIdFieldName() {
		return "id";
	}

	@Override
	protected String getKeyFieldName() {
		return "key";
	}

	@Override
	protected ChampionSpell[] getSpells(ObjectCodec codec, JsonNode tree) throws IOException {
		JsonParser spellsParser = tree.get("spells").traverse();
		spellsParser.setCodec(codec);
		TreeNode spellsTreeNode = spellsParser.readValueAsTree();
		return codec.treeToValue(spellsTreeNode, ChampionSpell[].class);
	}

	@Override
	protected ChampionPassive getPassive(ObjectCodec codec, JsonNode tree) throws IOException {
		JsonParser passiveParser = tree.get("passive").traverse();
		passiveParser.setCodec(codec);
		return passiveParser.readValueAs(ChampionPassive.class);
	}

}
