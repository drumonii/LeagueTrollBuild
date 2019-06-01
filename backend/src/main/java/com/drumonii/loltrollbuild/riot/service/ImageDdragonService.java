package com.drumonii.loltrollbuild.riot.service;

import com.drumonii.loltrollbuild.model.image.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;

@Service
public class ImageDdragonService implements ImageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageDdragonService.class);

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Image getItemImage(int itemId) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("itemId", itemId);
        String sql =
                "SELECT img.IMG_FULL, img.IMG_GROUP, img.IMG_SRC " +
                "FROM ITEM_IMAGE img " +
                "WHERE img.ITEM_ID = :itemId";
        return getImage(sql, parameterSource);
    }

    @Override
    public Image getChampionImage(int championId) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("championId", championId);
        String sql =
                "SELECT img.IMG_FULL, img.IMG_GROUP, img.IMG_SRC " +
                "FROM CHAMPION_IMAGE img " +
                "WHERE img.CHAMPION_ID = :championId";
        return getImage(sql, parameterSource);
    }

    @Override
    public Image getSummonerSpellImage(int summonerSpellId) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("summonerSpellId", summonerSpellId);
        String sql =
                "SELECT img.IMG_FULL, img.IMG_GROUP, img.IMG_SRC " +
                "FROM SUMMONER_SPELL_IMAGE img " +
                "WHERE img.SUMMONER_SPELL_ID = :summonerSpellId";
        return getImage(sql, parameterSource);
    }

    @Override
    public Image getMapImage(int mapId) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("mapId", mapId);
        String sql =
                "SELECT img.IMG_FULL, img.IMG_GROUP, img.IMG_SRC " +
                "FROM MAP_IMAGE img " +
                "WHERE img.MAP_ID = :mapId";
        return getImage(sql, parameterSource);
    }

    @Override
    public Image getChampionSpellImage(int championId, String spellKey) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("championId", championId)
                .addValue("spellKey", spellKey);
        String sql =
                "SELECT img.IMG_FULL, img.IMG_GROUP, img.IMG_SRC " +
                "FROM CHAMPION_SPELL_IMAGE img, CHAMPION_SPELL spell " +
                "WHERE img.CHAMPION_SPELL_KEY = :spellKey " +
                "AND spell.KEY = img.CHAMPION_SPELL_KEY " +
                "AND spell.CHAMPION_ID = :championId";
        return getImage(sql, parameterSource);
    }

    @Override
    public Image getChampionPassiveImage(int championId) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("championId", championId);
        String sql =
                "SELECT img.IMG_FULL, img.IMG_GROUP, img.IMG_SRC " +
                "FROM CHAMPION_PASSIVE_IMAGE img " +
                "WHERE img.CHAMPION_ID = :championId";
        return getImage(sql, parameterSource);
    }

    private Image getImage(String sql, SqlParameterSource parameterSource) {
        try {
            return jdbcTemplate.queryForObject(sql, parameterSource, new ImageRowMapper());
        } catch (EmptyResultDataAccessException e) {
            LOGGER.debug("Caught EmptyResultDataAccessException", e);
            return null;
        }
    }

    private class ImageRowMapper implements RowMapper<Image> {

        @Override
        public Image mapRow(ResultSet rs, int rowNum) throws SQLException {
            Image image = new Image();
            image.setFull(rs.getString("IMG_FULL"));
            image.setGroup(rs.getString("IMG_GROUP"));
            image.setImgSrc(rs.getBytes("IMG_SRC"));
            return image;
        }

    }

}
