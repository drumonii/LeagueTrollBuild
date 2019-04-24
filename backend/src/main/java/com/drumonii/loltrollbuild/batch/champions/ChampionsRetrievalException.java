package com.drumonii.loltrollbuild.batch.champions;

class ChampionsRetrievalException extends RuntimeException {

    ChampionsRetrievalException() {
        super("Unable to retrieval Champions from Riot");
    }

}
