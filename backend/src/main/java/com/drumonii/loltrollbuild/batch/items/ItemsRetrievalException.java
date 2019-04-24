package com.drumonii.loltrollbuild.batch.items;

class ItemsRetrievalException extends RuntimeException {

    ItemsRetrievalException() {
        super("Unable to retrieval Items from Riot");
    }

}
