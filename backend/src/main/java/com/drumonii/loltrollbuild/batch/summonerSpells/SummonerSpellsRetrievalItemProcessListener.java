package com.drumonii.loltrollbuild.batch.summonerSpells;

import com.drumonii.loltrollbuild.model.SummonerSpell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemProcessListener;

public class SummonerSpellsRetrievalItemProcessListener implements ItemProcessListener<SummonerSpell, SummonerSpell> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SummonerSpellsRetrievalItemProcessListener.class);

    @Override
    public void beforeProcess(SummonerSpell summonerSpell) {
        LOGGER.info("Processing Summoner Spell: {}", summonerSpell.getName());
    }

    @Override
    public void afterProcess(SummonerSpell summonerSpell, SummonerSpell result) {
        if (result == null) {
            LOGGER.info("Ignoring Summoner Spell: {} due to no changes from previous patch", summonerSpell.getName());
        }
    }

    @Override
    public void onProcessError(SummonerSpell summonerSpell, Exception e) {
        // nothing to do on process error
    }

}
