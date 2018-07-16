package com.drumonii.loltrollbuild.model.deserializer;

import com.drumonii.loltrollbuild.config.Profiles.StaticData;
import com.drumonii.loltrollbuild.util.ItemUtil;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
@StaticData
public class ItemStaticDataDeserializer extends ItemDeserializer {

	@Override
	protected int getId(ObjectCodec codec, JsonNode tree) {
		JsonNode idNode = tree.get("id");
		return idNode.asInt();
	}

	@Override
	protected String getRequiredAlly(ObjectCodec codec, JsonNode tree) throws IOException {
		JsonNode colloqNode = tree.get("colloq");
		String colloq = nullSafeValue(colloqNode, String.class);
		return ItemUtil.requiresAllyOrnn(colloq) ? "Ornn" : null;
	}

}
