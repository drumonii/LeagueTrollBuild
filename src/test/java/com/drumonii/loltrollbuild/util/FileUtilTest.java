package com.drumonii.loltrollbuild.util;

import com.drumonii.loltrollbuild.BaseUnitTestRunner;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.drumonii.loltrollbuild.util.FileUtil.copyURLToFile;
import static com.drumonii.loltrollbuild.util.FileUtil.createTempResourceDir;
import static org.assertj.core.api.Assertions.assertThat;

public class FileUtilTest extends BaseUnitTestRunner {

	@Mock
	private UriComponentsBuilder builder;

	@Mock
	private UriComponents uriComponents;

	@Before
	public void before() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void fileIsCopiedFromURL() throws Exception {
		String image = "fruits.png";
		URL url = new URL("http://homepages.cae.wisc.edu/~ece533/images/" + image);

		String dirInTemp = RandomStringUtils.random(RandomUtils.nextInt(1, 11), true, false);
		Path tempResourcePath = Paths.get(FileUtils.getTempDirectoryPath(), dirInTemp);

		Files.createDirectory(tempResourcePath);

		// No previous image, don't overwrite
		assertThat(copyURLToFile(url, tempResourcePath.resolve(image), false)).isEqualTo(1);
		Files.delete(tempResourcePath.resolve(image)); // cleanup

		// No previous image, overwrite
		assertThat(copyURLToFile(url, tempResourcePath.resolve(image), true)).isEqualTo(1);

		// With previous image, don't overwrite
		assertThat(copyURLToFile(url, tempResourcePath.resolve(image), false)).isEqualTo(0);

		// With previous image, overwrite
		assertThat(copyURLToFile(url, tempResourcePath.resolve(image), true)).isEqualTo(1);
		FileUtils.cleanDirectory(tempResourcePath.toFile()); // cleanup
	}

	@Test
	public void resourceDirIsCreatedInTemp() throws Exception {
		String dirInTemp = RandomStringUtils.random(RandomUtils.nextInt(1, 11), true, false);
		Path tempResourcePath = Paths.get(FileUtils.getTempDirectoryPath(), dirInTemp);

		assertThat(createTempResourceDir(dirInTemp)).isTrue();
		assertThat(tempResourcePath).exists();
		assertThat(tempResourcePath).isDirectory();
		assertThat(tempResourcePath).startsWithRaw(Paths.get(FileUtils.getTempDirectoryPath()))
				.endsWith(Paths.get(dirInTemp));

		// Directory already exists
		assertThat(createTempResourceDir(dirInTemp)).isTrue();

		// Invalid folder name
		assertThat(createTempResourceDir("|||")).isFalse();

		Files.delete(tempResourcePath); // cleanup
	}

}