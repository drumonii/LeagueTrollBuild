package com.drumonii.loltrollbuild.riot.api;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.Champion;
import com.drumonii.loltrollbuild.model.image.ChampionImage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.StrictAssertions.entry;

public class ChampionsResponseTest extends BaseSpringTestRunner {

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void championsResponseFromRiotIsMapped() throws IOException {
		String responseBody = "{\"type\":\"champion\",\"version\":\"5.16.1\",\"data\":{\"Thresh\":{\"id\":412,\"" +
				"key\":\"Thresh\",\"name\":\"Thresh\",\"title\":\"the Chain Warden\",\"image\":{\"full\":" +
				"\"Thresh.png\",\"sprite\":\"champion3.png\",\"group\":\"champion\",\"x\":336,\"y\":0,\"w\":48," +
				"\"h\":48},\"tags\":[\"Support\",\"Fighter\"],\"partype\":\"Mana\"}}}";
		ChampionsResponse championsResponse = objectMapper.readValue(responseBody, ChampionsResponse.class);

		assertThat(championsResponse).isNotNull();
		assertThat(championsResponse).isEqualToComparingOnlyGivenFields(new ChampionsResponse("champion", "5.16.1"),
				"type", "version");
		assertThat(championsResponse.getChampions()).hasSize(1);
		Champion champion = new Champion(412, "Thresh", "Thresh", "the Chain Warden", new ChampionImage("Thresh.png",
				"champion3.png", "champion", 336, 0, 48, 48), new HashSet<>(Arrays.asList("Support", "Fighter")),
				"Mana");
		assertThat(championsResponse.getChampions()).containsExactly(entry("Thresh", champion));
	}

}
