package com.drumonii.loltrollbuild.api.service;

import com.drumonii.loltrollbuild.config.JpaConfig;
import com.drumonii.loltrollbuild.model.*;
import com.drumonii.loltrollbuild.model.image.Image;
import com.drumonii.loltrollbuild.repository.ChampionsRepository;
import com.drumonii.loltrollbuild.repository.ItemsRepository;
import com.drumonii.loltrollbuild.repository.MapsRepository;
import com.drumonii.loltrollbuild.repository.SummonerSpellsRepository;
import com.drumonii.loltrollbuild.riot.api.ChampionsResponse;
import com.drumonii.loltrollbuild.riot.api.ItemsResponse;
import com.drumonii.loltrollbuild.riot.api.MapsResponse;
import com.drumonii.loltrollbuild.riot.api.SummonerSpellsResponse;
import com.drumonii.loltrollbuild.test.json.JsonTestFilesUtil;
import com.drumonii.loltrollbuild.util.GameMapUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.function.Consumer;

import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@RestClientTest(ImageApiService.class)
@Transactional
@AutoConfigureDataJpa
@Import(JpaConfig.class)
@ActiveProfiles({ TESTING })
class ImageApiServiceTest {

    @Autowired
    private ImageApiService imageApiService;

    @Autowired
    private ChampionsRepository championsRepository;

    @Autowired
    private ItemsRepository itemsRepository;

    @Autowired
    private MapsRepository mapsRepository;

    @Autowired
    private SummonerSpellsRepository summonerSpellsRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private JsonTestFilesUtil jsonTestFilesUtil;

    @BeforeEach
    void beforeEach() {
        jsonTestFilesUtil = new JsonTestFilesUtil(objectMapper);
    }

    @Test
    void getsItemImage() {
        ItemsResponse itemsResponse = jsonTestFilesUtil.getItemsResponse();
        Item bladeOfTheRuinedKing = itemsResponse.getItems().get("3153");
        itemsRepository.saveAndFlush(bladeOfTheRuinedKing);

        Image itemImage = imageApiService.getItemImage(bladeOfTheRuinedKing.getId());
        assertThat(itemImage).satisfies(new FetchedImage(bladeOfTheRuinedKing.getImage()));
    }

    @Test
    void doesNotGetItemImage() {
        Image itemImage = imageApiService.getItemImage(0);
        assertThat(itemImage).isNull();
    }

    @Test
    void getsChampionImage() {
        ChampionsResponse championsResponse = jsonTestFilesUtil.getChampionsResponse();
        Champion nidalee = championsResponse.getChampions().get("Nidalee");
        championsRepository.saveAndFlush(nidalee);

        Image championImage = imageApiService.getChampionImage(nidalee.getId());
        assertThat(championImage).satisfies(new FetchedImage(nidalee.getImage()));
    }

    @Test
    void doesNotGetChampionImage() {
        Image championImage = imageApiService.getChampionImage(0);
        assertThat(championImage).isNull();
    }

    @Test
    void getsChampionPassiveImage() {
        ChampionsResponse championsResponse = jsonTestFilesUtil.getFullChampionsResponse();
        Champion lux = championsResponse.getChampions().get("Lux");
        championsRepository.saveAndFlush(lux);

        Image championPassiveImage = imageApiService.getChampionPassiveImage(lux.getId());
        assertThat(championPassiveImage).satisfies(new FetchedImage(lux.getPassive().getImage()));
    }

    @Test
    void doesNotGetChampionPassiveImage() {
        Image championPassiveImage = imageApiService.getChampionPassiveImage(0);
        assertThat(championPassiveImage).isNull();
    }

    @Test
    void getsChampionSpellImage() {
        ChampionsResponse championsResponse = jsonTestFilesUtil.getFullChampionsResponse();
        Champion bard = championsResponse.getChampions().get("Bard");
        championsRepository.saveAndFlush(bard);

        Image championSpellImage = imageApiService.getChampionSpellImage(bard.getId(), "BardQ");
        Optional<ChampionSpell> bardQ = bard.getSpells().stream()
                .filter(bardSpells -> bardSpells.getKey().equals("BardQ"))
                .findFirst();
        if (bardQ.isEmpty()) {
            fail("Unable to find a spell with key 'BardQ'");
        }
        assertThat(championSpellImage).satisfies(new FetchedImage(bardQ.get().getImage()));
    }

    @Test
    void doesNotGetChampionSpellImage() {
        Image championSpellImage = imageApiService.getChampionSpellImage(0, "");
        assertThat(championSpellImage).isNull();

        ChampionsResponse championsResponse = jsonTestFilesUtil.getChampionsResponse();
        Champion shaco = championsResponse.getChampions().get("Shaco");
        championsRepository.saveAndFlush(shaco);

        championSpellImage = imageApiService.getChampionSpellImage(shaco.getId(), "");
        assertThat(championSpellImage).isNull();
    }

    @Test
    void getsSummonerSpellImage() {
        SummonerSpellsResponse summonerSpellsResponse = jsonTestFilesUtil.getSummonerSpellsResponse();
        SummonerSpell exhaust = summonerSpellsResponse.getSummonerSpells().get("SummonerExhaust");
        summonerSpellsRepository.saveAndFlush(exhaust);

        Image summonerSpellImage = imageApiService.getSummonerSpellImage(exhaust.getId());
        assertThat(summonerSpellImage).satisfies(new FetchedImage(exhaust.getImage()));
    }

    @Test
    void doesNotGetSummonerSpellImage() {
        Image summonerSpellImage = imageApiService.getSummonerSpellImage(0);
        assertThat(summonerSpellImage).isNull();
    }

    @Test
    void getsMapImage() {
        MapsResponse mapsResponse = jsonTestFilesUtil.getMapsResponse();
        GameMap summonersRift = mapsResponse.getMaps().get(GameMapUtil.SUMMONERS_RIFT_SID);
        mapsRepository.saveAndFlush(summonersRift);

        Image mapImage = imageApiService.getMapImage(summonersRift.getMapId());
        assertThat(mapImage).satisfies(new FetchedImage(summonersRift.getImage()));
    }

    @Test
    void doesNotGetMapImage() {
        Image mapImage = imageApiService.getMapImage(0);
        assertThat(mapImage).isNull();
    }

    private class FetchedImage implements Consumer<Image> {

        private Image image;

        FetchedImage(Image image) {
            this.image = image;
        }

        @Override
        public void accept(Image fetchedImage) {
            assertThat(fetchedImage.getFull()).as("Full").isEqualTo(image.getFull());
            assertThat(fetchedImage.getGroup()).as("Group").isEqualTo(image.getGroup());
            assertThat(fetchedImage.getImgSrc()).as("Src").isEqualTo(image.getImgSrc());
        }

    }

}
