package com.drumonii.loltrollbuild.repository;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.Item;
import com.drumonii.loltrollbuild.riot.api.ItemsResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemsRepositoryTest extends BaseSpringTestRunner {

	@Autowired
	private ItemsRepository itemsRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void crudOperations() throws IOException {
		String responseBody = "{\"type\":\"item\",\"version\":\"5.16.1\",\"data\":{\"10001\":{\"id\":10001,\"name\":" +
				"\"Test Item\",\"group\":\"TestItems\",\"description\":\"Really concise item description\"," +
				"\"plaintext\":\"Ultra awesome test item bro\",\"from\":[\"9998\", \"9999\"],\"into\":[\"10002\"," +
				"\"10003\"],\"image\":{\"full\":\"10001.png\",\"sprite\":\"item0.png\",\"group\":\"item\",\"x\":432," +
				"\"y\":192,\"w\":48,\"h\":48},\"gold\":{\"base\":150,\"total\":1500,\"sell\":875,\"purchasable\":" +
				"true}}}}";
		ItemsResponse itemsResponse = objectMapper.readValue(responseBody, ItemsResponse.class);

		Item unmarshalledItem = itemsResponse.getItems().get("10001");

		// Create
		itemsRepository.save(unmarshalledItem);

		// Select
		Item itemFromDb = itemsRepository.findOne(10001);
		assertThat(itemFromDb).isNotNull();
		itemFromDb.setMaps(null); // It's empty and not null from the db
		assertThat(itemFromDb).isEqualToIgnoringGivenFields(unmarshalledItem, "from", "gold");

		// Update
		itemFromDb.setConsumed(true);
		itemsRepository.save(itemFromDb);
		itemFromDb = itemsRepository.findOne(10001);
		assertThat(itemFromDb.getConsumed()).isTrue();

		// Delete
		itemsRepository.delete(itemFromDb);
		assertThat(itemsRepository.findOne(10001)).isNull();
	}

}
