package com.drumonii.loltrollbuild.util;

import com.drumonii.loltrollbuild.model.GameMap;
import com.drumonii.loltrollbuild.model.SummonerSpell.GameMode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.drumonii.loltrollbuild.model.SummonerSpell.GameMode.ARAM;
import static com.drumonii.loltrollbuild.model.SummonerSpell.GameMode.CLASSIC;

/**
 * Utility methods relating to {@link GameMap}s.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GameMapUtil {

	private static final String CRYSTAL_SCAR = "Crystal Scar";
	private static final String HOWLING_ABYSS = "Howling Abyss";
	private static final String SUMMONERS_RIFT = "Summoner's Rift";
	private static final String TWISTED_TREELINE = "Twisted Treeline";

	/**
	 * Gets the {@link GameMap}'s actual map name.
	 *
	 * @param mapName the map name from Riot to infer
	 * @return the actual formatted map name
	 */
	public static String getActualMapName(String mapName) {
		try {
			int mapId = Integer.valueOf(mapName);
			switch (mapId) {
			case 1:
				return SUMMONERS_RIFT;
			case 8:
				return CRYSTAL_SCAR;
			case 10:
				return TWISTED_TREELINE;
			case 11:
				return SUMMONERS_RIFT;
			case 12:
				return HOWLING_ABYSS;
			}
		} catch (NumberFormatException e) {
			switch (mapName) {
			case "SummonersRift":
				return SUMMONERS_RIFT;
			case "CrystalScar":
				return CRYSTAL_SCAR;
			case "NewTwistedTreeline":
				return TWISTED_TREELINE;
			case "SummonersRiftNew":
				return SUMMONERS_RIFT;
			case "ProvingGroundsNew":
				return HOWLING_ABYSS;
			default:
				return mapName;
			}
		}
		return null; // Shouldn't happen
	}

	/**
	 * Gets {@link List} of all {@link GameMap}s that are eligible for the troll build. Map names are transformed into a
	 * more legible format. Eligible maps: Twisted Treeline, Summoner's Rift, and Proving Grounds.
	 *
	 * @param maps the {@link List} of {@link GameMap}s
	 * @return only the eligible {@link List} of {@link GameMap}s
	 */
	public static List<GameMap> eligibleMaps(List<GameMap> maps) {
		for (GameMap map : maps) {
			map.setMapName(GameMapUtil.getActualMapName(map.getMapName()));
		}
		return maps.stream()
				.filter(map -> map.getMapName().equals("Twisted Treeline") ||
						map.getMapName().equals("Summoner's Rift") || map.getMapName().equals("Howling Abyss"))
				.sorted((map1, map2) -> map1.getMapName().compareTo(map2.getMapName()))
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
		if (map.getMapName().equals("ProvingGroundsNew")) {
			mode = ARAM;
		}
		return mode;
	}

	/**
	 * Gets a {@link List} of String actual map names that are available, or listed as {@code true} based on its
	 * particular map ID.
	 *
	 * @param maps the {@link Map} of game maps
	 * @return the {@link List} of available game map (actual) names
	 */
	public static List<String> getAvailableMaps(Map<String, Boolean> maps) {
		List<String> availableMaps = new ArrayList<>();
		maps.forEach((k, v) -> {
			if (v) {
				availableMaps.add(getActualMapName(k));
			}
		});
		return availableMaps;
	}

}
