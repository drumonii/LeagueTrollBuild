package com.drumonii.loltrollbuild.util;

import com.drumonii.loltrollbuild.model.GameMap;
import com.drumonii.loltrollbuild.model.SummonerSpell.GameMode;

import static com.drumonii.loltrollbuild.model.SummonerSpell.GameMode.ARAM;
import static com.drumonii.loltrollbuild.model.SummonerSpell.GameMode.CLASSIC;

/**
 * Utility methods relating to {@link GameMap}s.
 */
public class GameMapUtil {

	private GameMapUtil() {}

	public static final int CRYSTAL_SCAR_ID = 8;
	public static final String CRYSTAL_SCAR_SID = "8";
	public static final int TWISTED_TREELINE_ID = 10;
	public static final String TWISTED_TREELINE_SID = "10";
	public static final int SUMMONERS_RIFT_ID = 11;
	public static final String SUMMONERS_RIFT_SID = "11";
	public static final int HOWLING_ABYSS_ID = 12;
	public static final String HOWLING_ABYSS_SID = "12";

	public static final String CRYSTAL_SCAR = "The Crystal Scar";
	public static final String TWISTED_TREELINE = "The Twisted Treeline";
	public static final String SUMMONERS_RIFT = "Summoner's Rift";
	public static final String HOWLING_ABYSS = "Howling Abyss";

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