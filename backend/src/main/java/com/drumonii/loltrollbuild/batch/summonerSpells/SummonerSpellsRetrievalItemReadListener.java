package com.drumonii.loltrollbuild.batch.summonerSpells;

import com.drumonii.loltrollbuild.model.SummonerSpell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemReadListener;

public class SummonerSpellsRetrievalItemReadListener implements ItemReadListener<SummonerSpell> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SummonerSpellsRetrievalItemReadListener.class);

    @Override
    public void beforeRead() {
        // nothing to do before read
    }

    @Override
    public void afterRead(SummonerSpell summonerSpell) {
        LOGGER.info("Finished reading Summoner Spell: {}", summonerSpell.getName());
    }

    @Override
    public void onReadError(Exception ex) {
        // nothing to do on read error
    }

}
