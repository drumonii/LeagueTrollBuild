package com.drumonii.loltrollbuild.riot.api;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.model.image.Image;
import com.drumonii.loltrollbuild.repository.VersionsRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
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
		super.before();
		MockitoAnnotations.initMocks(this);
		versionsRepository.save(new Version("latest patch version"));
	}

	@After
	public void after() {
		versionsRepository.deleteAll();
	}

	@Test
	public void imagesAreSavedAndDeleted() throws Exception {
		when(builder.buildAndExpand(anyString(), anyString()))
				.thenReturn(uriComponents);
		Image image1 = new Image("arctichare.png", "arctichare.png", "group", 144, 48, 48, 48);
		when(uriComponents.toUriString())
				.thenReturn("http://homepages.cae.wisc.edu/~ece533/images/" + image1.getFull());
		assertThat(imageSaver.copyImagesFromURLs(Arrays.asList(image1), false, builder)).isEqualTo(1);

		Image image2 = new Image("fruits.png", "fruits.png", "group", 144, 48, 48, 48);
		when(uriComponents.toUriString())
				.thenReturn("http://homepages.cae.wisc.edu/~ece533/images/" + image2.getFull());
		assertThat(imageSaver.copyImagesFromURLs(Arrays.asList(image2), true, builder)).isEqualTo(1);

		assertThat(imageSaver.deleteImages(Arrays.asList(image1, image2))).isEqualTo(2);
	}

	@Test
	public void imageIsSavedAndDeleted() throws Exception {
		when(builder.buildAndExpand(anyString(), anyString()))
				.thenReturn(uriComponents);
		Image image = new Image("monarch.png", "monarch0.png", "group", 144, 48, 48, 48);
		when(uriComponents.toUriString())
				.thenReturn("http://homepages.cae.wisc.edu/~ece533/images/" + image.getFull());
		assertThat(imageSaver.copyImageFromURL(image, builder)).isEqualTo(1);

		assertThat(imageSaver.deleteImage(image)).isEqualTo(1);
	}

}