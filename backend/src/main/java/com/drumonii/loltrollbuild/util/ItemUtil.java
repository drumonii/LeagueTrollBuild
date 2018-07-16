package com.drumonii.loltrollbuild.util;

import com.drumonii.loltrollbuild.model.Item;

/**
 * Utility methods relating to {@link Item}s.
 */
public class ItemUtil {

	private ItemUtil() {}

	/**
	 * Determines if the Item requires Ornn as an ally.
	 *
	 * @param colloq the colloq to inspect
	 * @return {@code true} if requires Ornn, otherwise {@code false}
	 */
	public static boolean requiresAllyOrnn(String colloq) {
		if (colloq == null) {
			return false;
		}
		return colloq.contains("Ornn");
	}

}
