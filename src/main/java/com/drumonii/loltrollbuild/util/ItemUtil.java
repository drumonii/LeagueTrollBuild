package com.drumonii.loltrollbuild.util;

import com.drumonii.loltrollbuild.model.Item;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility methods relating to {@link Item}s.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemUtil {

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
