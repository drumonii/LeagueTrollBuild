package com.drumonii.loltrollbuild.util;

import com.drumonii.loltrollbuild.BaseUnitTestRunner;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.drumonii.loltrollbuild.util.MapUtil.*;
import static org.assertj.core.api.Assertions.assertThat;

public class MapUtilTest extends BaseUnitTestRunner {

	@Test
	public void getsListOfElementsFromMap() {
		Map<String, Integer> map = new HashMap<>();
		map.put("Single", 1);
		map.put("Double", 2);
		map.put("Triple", 3);
		assertThat(getElementsFromMap(map)).containsAll(Arrays.asList(1, 2, 3));
	}

}
