package com.drumonii.loltrollbuild.model.deserializer;

import com.drumonii.loltrollbuild.config.Profiles.Ddragon;
import com.drumonii.loltrollbuild.model.ChampionPassive;
import com.drumonii.loltrollbuild.model.ChampionSpell;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
@Ddragon
public class ChampionDdragonDeserializer extends ChampionDeserializer {

	@Override
	protected String getIdFieldName() {
		return "key";
	}

	@Override
	protected String getKeyFieldName() {
		return "id";
	}

	@Override
	protected ChampionSpell[] getSpells(ObjectCodec codec, JsonNode tree) throws IOException {
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
			return spells;
		}
		return new ChampionSpell[] {};
	}

	@Override
	protected ChampionPassive getPassive(ObjectCodec codec, JsonNode tree) throws IOException {
		JsonNode passiveNode = tree.get("passive");
		if (passiveNode != null) {
			JsonParser passiveJsonParser = passiveNode.traverse();
			passiveJsonParser.setCodec(codec);
			return passiveJsonParser.readValueAs(ChampionPassive.class);
		}
		return null;
	}

}
