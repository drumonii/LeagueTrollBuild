package com.drumonii.loltrollbuild.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.boot.test.json.ObjectContent;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;

@RunWith(SpringRunner.class)
@JsonTest
public class BuildTest {

	@Autowired
	private JacksonTester<Build> jacksonTester;

	@Test
	public void serializesIntoJson() {
		Build build = new Build();
		build.setId(1);
		build.setCreatedDate(LocalDateTime.now());
		build.setChampionId(1);
		build.setItem1Id(1);
		build.setItem2Id(2);
		build.setItem3Id(3);
		build.setItem4Id(4);
		build.setItem5Id(5);
		build.setItem6Id(6);
		build.setSummonerSpell1Id(1);
		build.setSummonerSpell2Id(2);
		build.setTrinketId(7);
		build.setMapId(1);

		JsonContent<Build> jsonContent = null;
		try {
			jsonContent = jacksonTester.write(build);
		} catch (IOException e) {
			fail("Unable to serialize Build into JSON", e);
		}

		assertThat(jsonContent).hasJsonPathNumberValue("$.champion");
		assertThat(jsonContent).hasJsonPathNumberValue("$.item1");
		assertThat(jsonContent).hasJsonPathNumberValue("$.item2");
		assertThat(jsonContent).hasJsonPathNumberValue("$.item3");
		assertThat(jsonContent).hasJsonPathNumberValue("$.item4");
		assertThat(jsonContent).hasJsonPathNumberValue("$.item5");
		assertThat(jsonContent).hasJsonPathNumberValue("$.item6");
		assertThat(jsonContent).hasJsonPathNumberValue("$.summonerSpell1");
		assertThat(jsonContent).hasJsonPathNumberValue("$.summonerSpell2");
		assertThat(jsonContent).hasJsonPathNumberValue("$.trinket");
		assertThat(jsonContent).hasJsonPathNumberValue("$.map");
	}

	@Test
	public void deserializesFromJson() {
		String json =
				"{ " +
				"  \"createdDate\": \"2017-09-14T22:55:26.47\", " +
				"  \"id\": 1, " +
				"  \"champion\": 1, " +
				"  \"item1\": 1, " +
				"  \"item2\": 2, " +
				"  \"item3\": 3, " +
				"  \"item4\": 4, " +
				"  \"item5\": 5, " +
				"  \"item6\": 6, " +
				"  \"summonerSpell1\": 1, " +
				"  \"summonerSpell2\": 2, " +
				"  \"trinket\": 7, " +
				"  \"map\": 1 " +
				"}";

		ObjectContent<Build> build = null;
		try {
			build = jacksonTester.parse(json);
		} catch (IOException e) {
			fail("Unable to deserialize Build from JSON", e);
		}

		assertThat(build.getObject()).isNotNull();
		assertThat(build.getObject().getCreatedDate()).isNotNull();
		assertThat(build.getObject().getId()).isOne();
		assertThat(build.getObject().getChampionId()).isOne();
		assertThat(build.getObject().getItem1Id()).isOne();
		assertThat(build.getObject().getItem2Id()).isEqualTo(2);
		assertThat(build.getObject().getItem3Id()).isEqualTo(3);
		assertThat(build.getObject().getItem4Id()).isEqualTo(4);
		assertThat(build.getObject().getItem5Id()).isEqualTo(5);
		assertThat(build.getObject().getItem6Id()).isEqualTo(6);
		assertThat(build.getObject().getTrinketId()).isEqualTo(7);
		assertThat(build.getObject().getSummonerSpell1Id()).isOne();
		assertThat(build.getObject().getSummonerSpell2Id()).isEqualTo(2);
		assertThat(build.getObject().getMapId()).isOne();
	}

	@Test
	public void equals() {
		Build build1 = new Build();
		build1.setChampionId(1);
		build1.setItem1Id(1);
		build1.setItem2Id(2);
		build1.setItem3Id(3);
		build1.setItem4Id(4);
		build1.setItem5Id(5);
		build1.setItem6Id(6);
		build1.setSummonerSpell1Id(1);
		build1.setSummonerSpell2Id(2);
		build1.setTrinketId(7);
		build1.setMapId(1);

		Build build2 = new Build();
		build2.setChampionId(build1.getChampionId());
		build2.setItem1Id(build1.getItem1Id());
		build2.setItem2Id(build1.getItem2Id());
		build2.setItem3Id(build1.getItem3Id());
		build2.setItem4Id(build1.getItem4Id());
		build2.setItem5Id(build1.getItem5Id());
		build2.setItem6Id(build1.getItem6Id());
		build2.setSummonerSpell1Id(build1.getSummonerSpell1Id());
		build2.setSummonerSpell2Id(build1.getSummonerSpell2Id());
		build2.setTrinketId(build1.getTrinketId());
		build2.setMapId(build1.getMapId());

		assertThat(build1).isEqualTo(build2);

		build2.setItem2Id(100);
		assertThat(build1).isNotEqualTo(build2);
	}

}