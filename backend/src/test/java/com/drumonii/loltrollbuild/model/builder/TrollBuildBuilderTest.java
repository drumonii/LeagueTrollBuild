package com.drumonii.loltrollbuild.model.builder;

import com.drumonii.loltrollbuild.model.Item;
import com.drumonii.loltrollbuild.model.SummonerSpell;
import com.drumonii.loltrollbuild.model.TrollBuild;
import com.drumonii.loltrollbuild.test.json.JsonTestFilesUtil;
import com.drumonii.loltrollbuild.util.RandomizeUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class TrollBuildBuilderTest {

    @Autowired
    private ObjectMapper objectMapper;

    private List<Item> items;
    private List<SummonerSpell> summonerSpells;

    @BeforeEach
    void beforeEach() {
        JsonTestFilesUtil jsonTestFilesUtil = new JsonTestFilesUtil(objectMapper);

        items = new ArrayList<>(jsonTestFilesUtil.getItemsResponse().getItems().values());
        summonerSpells = new ArrayList<>(jsonTestFilesUtil.getSummonerSpellsResponse().getSummonerSpells().values());
    }

    @Test
    void buildsTrollBuild() {
        Collection<Item> boots = RandomizeUtil.getRandoms(items, 2); // get some "boots" (don't care they aren't actually boots)
        items.removeAll(boots);

        TrollBuild trollBuild = new TrollBuildBuilder()
                .withItems(items)
                .withBoots(boots)
                .withSummonerSpells(summonerSpells)
                .withTrinket(items)
                .build();

        assertThat(trollBuild).satisfies(new FullTrollBuild(boots));
    }

    @Test
    void buildsTrollBuildWithoutBoots() {
        TrollBuild trollBuild = new TrollBuildBuilder()
                .withBoots(null)
                .build();

        assertThat(trollBuild).satisfies(new EmptyTrollBuild());

        trollBuild = new TrollBuildBuilder()
                .withBoots(new ArrayList<>())
                .build();

        assertThat(trollBuild).satisfies(new EmptyTrollBuild());
    }

    @Test
    void buildsTrollBuildWithoutItems() {
        TrollBuild trollBuild = new TrollBuildBuilder()
                .withItems(null)
                .build();

        assertThat(trollBuild).satisfies(new EmptyTrollBuild());

        trollBuild = new TrollBuildBuilder()
                .withItems(new ArrayList<>())
                .build();

        assertThat(trollBuild).satisfies(new EmptyTrollBuild());
    }

    @Test
    void buildsTrollBuildWithoutSummonerSpells() {
        TrollBuild trollBuild = new TrollBuildBuilder()
                .withSummonerSpells(null)
                .build();

        assertThat(trollBuild).satisfies(new EmptyTrollBuild());

        trollBuild = new TrollBuildBuilder()
                .withSummonerSpells(new ArrayList<>())
                .build();

        assertThat(trollBuild).satisfies(new EmptyTrollBuild());
    }

    @Test
    void buildsTrollBuildWithoutTrinket() {
        TrollBuild trollBuild = new TrollBuildBuilder()
                .withTrinket(null)
                .build();

        assertThat(trollBuild).satisfies(new EmptyTrollBuild());

        trollBuild = new TrollBuildBuilder()
                .withTrinket(new ArrayList<>())
                .build();

        assertThat(trollBuild).satisfies(new EmptyTrollBuild());
    }

    private class EmptyTrollBuild implements Consumer<TrollBuild> {

        @Override
        public void accept(TrollBuild trollBuild) {
            assertThat(trollBuild.getItems()).isEmpty();
            assertThat(trollBuild.getTotalGold()).isZero();
            assertThat(trollBuild.getSummonerSpells()).isEmpty();
            assertThat(trollBuild.getTrinket()).isNull();
        }

    }

    private class FullTrollBuild implements Consumer<TrollBuild> {

        private Collection<Item> boots;

        FullTrollBuild(Collection<Item> boots) {
            this.boots = boots;
        }

        @Override
        public void accept(TrollBuild trollBuild) {
            assertThat(trollBuild.getItems()).element(0).isIn(boots);
            assertThat(trollBuild.getItems()).hasSize(TrollBuildBuilder.ITEMS_SIZE);
            assertThat(trollBuild.getTotalGold()).isNotZero();
            assertThat(trollBuild.getSummonerSpells()).hasSize(TrollBuildBuilder.SPELLS_SIZE);
            assertThat(trollBuild.getTrinket()).isNotNull();
        }

    }

}
