package com.drumonii.loltrollbuild.model.builder;

import com.drumonii.loltrollbuild.model.Item;
import com.drumonii.loltrollbuild.model.SummonerSpell;
import com.drumonii.loltrollbuild.model.TrollBuild;
import com.drumonii.loltrollbuild.test.json.JsonTestFilesUtil;
import com.drumonii.loltrollbuild.util.RandomizeUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@JsonTest
public class TrollBuildBuilderTest {

    @Autowired
    private ObjectMapper objectMapper;

    private List<Item> items;
    private List<SummonerSpell> summonerSpells;

    @Before
    public void before() {
        JsonTestFilesUtil jsonTestFilesUtil = new JsonTestFilesUtil(objectMapper);

        items = new ArrayList<>(jsonTestFilesUtil.getItemsResponse().getItems().values());
        summonerSpells = new ArrayList<>(jsonTestFilesUtil.getSummonerSpellsResponse().getSummonerSpells().values());
    }

    @Test
    public void buildsTrollBuild() {
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
    public void buildsTrollBuildWithViktor() {
        List<Item> viktorOnlyItems = items.stream()
                .filter(item -> "Viktor".equals(item.getRequiredChampion()))
                .collect(Collectors.toList());

        Collection<Item> boots = RandomizeUtil.getRandoms(items, 2); // get some "boots" (don't care they aren't actually boots)
        items.removeAll(boots);

        TrollBuild trollBuild = new TrollBuildBuilder()
                .withItems(items)
                .withBoots(boots)
                .withSummonerSpells(summonerSpells)
                .withTrinket(items)
                .withViktor(viktorOnlyItems)
                .build();

        assertThat(trollBuild).satisfies(new FullViktorTrollBuild(boots, viktorOnlyItems));
    }

    @Test
    public void buildsTrollBuildWithoutBoots() {
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
    public void buildsTrollBuildWithoutItems() {
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
    public void buildsTrollBuildWithoutSummonerSpells() {
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
    public void buildsTrollBuildWithoutTrinket() {
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
            assertThat(trollBuild.getItems().get(0)).isIn(boots);
            assertThat(trollBuild.getItems()).hasSize(TrollBuildBuilder.ITEMS_SIZE);
            assertThat(trollBuild.getTotalGold()).isNotZero();
            assertThat(trollBuild.getSummonerSpells()).hasSize(TrollBuildBuilder.SPELLS_SIZE);
            assertThat(trollBuild.getTrinket()).isNotNull();
        }

    }

    private class FullViktorTrollBuild extends FullTrollBuild {

        private Collection<Item> viktorOnlyItems;

        FullViktorTrollBuild(Collection<Item> boots, Collection<Item> viktorOnlyItems) {
            super(boots);
            this.viktorOnlyItems = viktorOnlyItems;
        }

        @Override
        public void accept(TrollBuild trollBuild) {
            super.accept(trollBuild);

            assertThat(trollBuild.getItems().get(1)).isIn(viktorOnlyItems);
        }

    }

}