package com.drumonii.loltrollbuild.riot.api;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.model.image.Image;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class ImageFetcherTest extends BaseSpringTestRunner {

	@Autowired
	private ImageFetcher imageFetcher;

	@Mock
	private UriComponentsBuilder builder;

	@Mock
	private UriComponents uriComponents;

	@Before
	public void before() {
		super.before();
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void setImgsSrcs() throws Exception {
		Image image1 = new Image("arctichare.png", "arctichare.png", "group", new byte[0], 144, 48, 48, 48);
		Image image2 = new Image("fruits.png", "fruits.png", "group", new byte[0], 144, 48, 48, 48);
		List<Image> images = Arrays.asList(image1, image2);

		Version latestVersion = versions.get(0);

		when(builder.buildAndExpand(anyString(), anyString()))
				.thenReturn(uriComponents);
		for (Image image : images) {
			when(uriComponents.toUriString())
					.thenReturn(image.getFull());
		}
		assertThat(imageFetcher.setImgsSrcs(images, builder, latestVersion)).isEqualTo(0);

		when(builder.buildAndExpand(anyString(), anyString()))
				.thenReturn(uriComponents);
		for (Image image : images) {
			when(uriComponents.toUriString())
					.thenReturn("http://homepages.cae.wisc.edu/~ece533/images/" + image.getFull());
		}
		assertThat(imageFetcher.setImgsSrcs(images, builder, latestVersion)).isEqualTo(2);
	}

	@Test
	public void setImgSrc() throws Exception {
		Image image = new Image("monarch.png", "monarch0.png", "group", new byte[0], 144, 48, 48, 48);

		Version latestVersion = versions.get(0);

		when(builder.buildAndExpand(anyString(), anyString()))
				.thenReturn(uriComponents);
		when(uriComponents.toUriString())
				.thenReturn(image.getFull());
		assertThat(imageFetcher.setImgSrc(image, builder, latestVersion)).isEqualTo(0);

		when(builder.buildAndExpand(anyString(), anyString()))
				.thenReturn(uriComponents);
		when(uriComponents.toUriString())
				.thenReturn("http://homepages.cae.wisc.edu/~ece533/images/" + image.getFull());
		assertThat(imageFetcher.setImgSrc(image, builder, latestVersion)).isEqualTo(1);
	}

}