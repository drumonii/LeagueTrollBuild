package com.drumonii.loltrollbuild.riot.service;

import com.drumonii.loltrollbuild.model.image.Image;

/**
 * {@code @Service} for {@link Image}s.
 */
public interface ImageService {

    /**
     * Gets the item's {@link Image} by the item's id.
     *
     * @param itemId the item's id
     * @return the {@link Image} if found, otherwise {@code null}
     */
    Image getItemImage(int itemId);

    /**
     * Gets the champion's {@link Image} by the champion's id.
     *
     * @param championId the champion's id
     * @return the {@link Image} if found, otherwise {@code null}
     */
    Image getChampionImage(int championId);

    /**
     * Gets the champion's spell image {@link Image} by the champion's id and spell key.
     *
     * @param championId the champion's id
     * @param spellKey the spell key
     * @return the {@link Image} if found, otherwise {@code null}
     */
    Image getChampionSpellImage(int championId, String spellKey);

    /**
     * Gets the champion's passive {@link Image} by the champion's id.
     *
     * @param championId the champion's id
     * @return the {@link Image} if found, otherwise {@code null}
     */
    Image getChampionPassiveImage(int championId);

    /**
     * Gets the summoner spell's {@link Image} by the summoner spell's id.
     *
     * @param summonerSpellId the summoner spell's id
     * @return the {@link Image} if found, otherwise {@code null}
     */
    Image getSummonerSpellImage(int summonerSpellId);

    /**
     * Gets the map's {@link Image} by the map's id.
     *
     * @param mapId the item's id
     * @return the {@link Image} if found, otherwise {@code null}
     */
    Image getMapImage(int mapId);

}
