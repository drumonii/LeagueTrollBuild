package com.drumonii.loltrollbuild.riot.api;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.model.image.Image;
import com.drumonii.loltrollbuild.repository.VersionsRepository;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static com.drumonii.loltrollbuild.config.WebMvcConfig.RESOURCE_DIR;
import static com.drumonii.loltrollbuild.config.WebMvcConfig.TEMP_DIR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class ImageSaverTest extends BaseSpringTestRunner {

	@Autowired
	private ImageSaver imageSaver;

	@Mock
	private UriComponentsBuilder builder;

	@Mock
	private UriComponents uriComponents;

	@Autowired
	private VersionsRepository versionsRepository;

	@Before
	public void before() {
		MockitoAnnotations.initMocks(this);
		versionsRepository.save(new Version("latest patch version"));
	}

	@After
	public void after() {
		versionsRepository.deleteAll();
		try {
			FileUtils.cleanDirectory(Paths.get(TEMP_DIR, RESOURCE_DIR).toFile());
		} catch (IOException e) {
			fail("Unable to clean the " + RESOURCE_DIR + " in the temp directory after a test.");
		}
	}

	@Test
	public void imagesAreSavedToTemp() throws Exception {
		when(builder.buildAndExpand(anyString(), anyString()))
				.thenReturn(uriComponents);

		List<Image> images = new ArrayList<>();
		Image image1 = new Image("arctichare.png", "arctichare.png", "group", 144, 48, 48, 48);
		images.add(image1);
		when(uriComponents.toUriString())
				.thenReturn("http://homepages.cae.wisc.edu/~ece533/images/" + image1.getFull());

		assertThat(imageSaver.copyImagesFromURLs(images, false, builder)).isEqualTo(1);

		images = new ArrayList<>();
		Image image2 = new Image("fruits.png", "fruits.png", "group", 144, 48, 48, 48);
		images.add(image2);
		when(uriComponents.toUriString())
				.thenReturn("http://homepages.cae.wisc.edu/~ece533/images/" + image2.getFull());

		assertThat(imageSaver.copyImagesFromURLs(images, true, builder)).isEqualTo(1);
	}

	@Test
	public void imageIsSavedToTemp() throws Exception {
		when(builder.buildAndExpand(anyString(), anyString()))
				.thenReturn(uriComponents);

		Image image = new Image("monarch.png", "monarch0.png", "group", 144, 48, 48, 48);
		when(uriComponents.toUriString())
				.thenReturn("http://homepages.cae.wisc.edu/~ece533/images/" + image.getFull());

		assertThat(imageSaver.copyImageFromURL(image, builder)).isEqualTo(1);
	}

}