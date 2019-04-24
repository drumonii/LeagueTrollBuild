package com.drumonii.loltrollbuild.batch.summonerSpells;

class SummonerSpellsRetrievalException extends RuntimeException {

    SummonerSpellsRetrievalException() {
        super("Unable to retrieval Summoner Spells from Riot");
    }

}
