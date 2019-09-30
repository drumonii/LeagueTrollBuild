package com.drumonii.loltrollbuild.batch.summonerSpells;

import com.drumonii.loltrollbuild.model.SummonerSpell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemWriteListener;

import java.util.List;

public class SummonerSpellsRetrievalItemWriteListener implements ItemWriteListener<SummonerSpell> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SummonerSpellsRetrievalItemWriteListener.class);

    @Override
    public void beforeWrite(List<? extends SummonerSpell> summonerSpells) {
        LOGGER.info("Preparing to write {} Summoner Spells", summonerSpells.size());
    }

    @Override
    public void afterWrite(List<? extends SummonerSpell> summonerSpells) {
        LOGGER.info("Finished writing {} Summoner Spells", summonerSpells.size());
    }

    @Override
    public void onWriteError(Exception exception, List<? extends SummonerSpell> summonerSpells) {
        // nothing to do on write error
    }

}
