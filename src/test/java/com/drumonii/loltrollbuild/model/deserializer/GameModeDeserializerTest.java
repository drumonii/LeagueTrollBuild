package com.drumonii.loltrollbuild.model.deserializer;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.SummonerSpell;
import com.drumonii.loltrollbuild.model.SummonerSpell.GameMode;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.assertj.core.api.iterable.Extractor;
import org.junit.Test;

import java.util.Collection;
import java.util.SortedSet;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GameModeDeserializerTest extends BaseSpringTestRunner {

	@Test
	public void deserializesGameMode() throws Exception {
		assertThat(objectMapper.readValue(
				"{" +
				"   \"name\":\"Barrier\"," +
				"   \"description\":\"Shields your champion from 115-455 damage (depending on champion level) for 2 seconds.\"," +
				"   \"image\":{" +
				"      \"full\":\"SummonerBarrier.png\"," +
				"      \"sprite\":\"spell0.png\"," +
				"      \"group\":\"spell\"," +
				"      \"x\":0," +
				"      \"y\":0," +
				"      \"w\":48," +
				"      \"h\":48" +
				"   }," +
				"   \"cooldown\":[" +
				"      180.0" +
				"   ]," +
				"   \"summonerLevel\":4," +
				"   \"id\":21," +
				"   \"key\":\"SummonerBarrier\"," +
				"   \"modes\":[" +
				"      \"ARAM\"," +
				"      \"CLASSIC\"," +
				"      \"TUTORIAL\"," +
				"      \"ODIN\"," +
				"      \"ASCENSION\"," +
				"      \"FIRSTBLOOD\"," +
				"      \"ASSASSINATE\"," +
				"      \"URF\"," +
				"      \"KINGPORO\"" +
				"   ]" +
				"}", SummonerSpell.class).getModes()).containsOnly(GameMode.ARAM, GameMode.CLASSIC, GameMode.TUTORIAL);
	}

}