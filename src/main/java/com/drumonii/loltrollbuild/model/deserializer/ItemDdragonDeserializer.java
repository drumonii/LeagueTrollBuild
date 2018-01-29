package com.drumonii.loltrollbuild.model.deserializer;

import com.drumonii.loltrollbuild.config.Profiles.Ddragon;
import com.drumonii.loltrollbuild.model.Item;
import com.drumonii.loltrollbuild.model.ItemGold;
import com.drumonii.loltrollbuild.model.builder.ItemBuilder;
import com.drumonii.loltrollbuild.model.image.ItemImage;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.boot.jackson.JsonObjectDeserializer;

import java.io.IOException;
import java.util.SortedMap;

@JsonComponent
@Ddragon
public class ItemDdragonDeserializer extends JsonObjectDeserializer<Item> {

	@Override
	protected Item deserializeObject(JsonParser jsonParser, DeserializationContext context, ObjectCodec codec,
			JsonNode tree) throws IOException {
		ItemBuilder itemBuilder = new ItemBuilder();

		JsonNode nameNode = tree.get("name");
		itemBuilder.withName(nullSafeValue(nameNode, String.class));

		JsonNode groupNode = tree.get("group");
		itemBuilder.withGroup(nullSafeValue(groupNode, String.class));

		JsonNode consumedNode = tree.get("consumed");
		itemBuilder.withConsumed(nullSafeValue(consumedNode, Boolean.class));

		JsonNode descriptionNode = tree.get("description");
		itemBuilder.withDescription(nullSafeValue(descriptionNode, String.class));

		JsonNode fromNode = tree.get("from");
		if (fromNode != null) {
			JsonParser fromParser = fromNode.traverse();
			fromParser.setCodec(codec);
			TreeNode fromTreeNode = fromParser.readValueAsTree();
			itemBuilder.withFrom(codec.treeToValue(fromTreeNode, Integer[].class));
		}

		JsonNode intoNode = tree.get("into");
		if (intoNode != null) {
			JsonParser intoParser = intoNode.traverse();
			intoParser.setCodec(codec);
			TreeNode intoTreeNode = intoParser.readValueAsTree();
			itemBuilder.withInto(codec.treeToValue(intoTreeNode, Integer[].class));
		}

		JsonNode requiredChampionNode = tree.get("requiredChampion");
		itemBuilder.withRequiredChampion(nullSafeValue(requiredChampionNode, String.class));

		JsonNode requiredAllyNode = tree.get("requiredAlly");
		itemBuilder.withRequiredAlly(nullSafeValue(requiredAllyNode, String.class));

		JsonParser mapsParser = tree.get("maps").traverse();
		mapsParser.setCodec(codec);
		itemBuilder.withMaps(mapsParser.readValueAs(new TypeReference<SortedMap<Integer, Boolean>>(){}));

		JsonParser imageParser = tree.get("image").traverse();
		imageParser.setCodec(codec);
		ItemImage image = imageParser.readValueAs(ItemImage.class);
		itemBuilder.withImage(image);

		// Ddragon doesn't have an id so have to parse it from the image full
		itemBuilder.withId(image.getFull().substring(0, image.getFull().lastIndexOf('.')));

		JsonParser goldParser = tree.get("gold").traverse();
		goldParser.setCodec(codec);
		ItemGold gold = goldParser.readValueAs(ItemGold.class);
		itemBuilder.withGold(gold);

		return itemBuilder.build();
	}

}
