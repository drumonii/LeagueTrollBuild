package com.drumonii.loltrollbuild.util;

import com.drumonii.loltrollbuild.model.GameMap;
import com.drumonii.loltrollbuild.model.SummonerSpell.GameMode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.drumonii.loltrollbuild.model.SummonerSpell.GameMode.ARAM;
import static com.drumonii.loltrollbuild.model.SummonerSpell.GameMode.CLASSIC;

/**
 * Utility methods relating to {@link GameMap}s.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GameMapUtil {

	public static final int CRYSTAL_SCAR_ID = 8;
	public static final int TWISTED_TREELINE_ID = 10;
	public static final int SUMMONERS_RIFT_ID = 11;
	public static final int HOWLING_ABYSS_ID = 12;

	public static final String CRYSTAL_SCAR = "The Crystal Scar";
	public static final String TWISTED_TREELINE = "The Twisted Treeline";
	public static final String SUMMONERS_RIFT = "Summoner's Rift";
	public static final String HOWLING_ABYSS = "Howling Abyss";

	/**
	 * Gets {@link List} of all {@link GameMap}s that are eligible for the troll build. Map names are transformed into a
	 * more legible format. Eligible maps: Twisted Treeline, Summoner's Rift, and Proving Grounds.
	 *
	 * @param maps the {@link List} of {@link GameMap}s
	 * @return only the eligible {@link List} of {@link GameMap}s
	 */
	public static List<GameMap> eligibleMaps(List<GameMap> maps) {
		return maps.stream()
				.filter(map -> map.getMapId() == TWISTED_TREELINE_ID ||
						map.getMapId() == SUMMONERS_RIFT_ID || map.getMapId() == HOWLING_ABYSS_ID)
				.sorted(Comparator.comparing(GameMap::getMapName))
				.collect(Collectors.toList());
	}

	/**
	 * Gets a {@link GameMode} from a {@link GameMap}'s ID. If the map isn't the Howling Abyss (Proving Grounds) then
	 * the game mode is considered as "CLASSIC", otherwise "ARAM" is returned.
	 *
	 * @param map the {@link GameMap}
	 * @return the {@link GameMode}
	 * @see <a href="http://leagueoflegends.wikia.com/wiki/Category:Game_modes">Game Modes</a>
	 */
	public static GameMode getModeFromMap(GameMap map) {
		GameMode mode = CLASSIC;
		if (map == null) {
			return mode;
		}
		if (map.getMapName().contains("Howling")) {
			mode = ARAM;
		}
		return mode;
	}

	/**
	 * Gets the map name from the provided map Id.
	 *
	 * @param mapId the map Id
	 * @return the map name
	 */
	public static String getNameFromId(int mapId) {
		switch (mapId) {
		case CRYSTAL_SCAR_ID:
			return CRYSTAL_SCAR;
		case TWISTED_TREELINE_ID:
			return TWISTED_TREELINE;
		case SUMMONERS_RIFT_ID:
			return SUMMONERS_RIFT;
		case HOWLING_ABYSS_ID:
			return HOWLING_ABYSS;
		default:
			return "";
		}
	}

}