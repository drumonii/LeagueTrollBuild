package com.drumonii.loltrollbuild.riot.api;

import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.model.image.Image;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;

@RunWith(MockitoJUnitRunner.class)
public class ImageFetcherTest {

	@InjectMocks
	private ImageFetcher imageFetcher;

	@Mock
	private UriComponentsBuilder builder;

	@Mock
	private UriComponents uriComponents;

	@Test
	public void setsImagesSourcesWithInvalidUrl() {
		Image image1 = new Image("arctichare.png", "arctichare.png", "group", new byte[0], 144, 48, 48, 48);
		List<Image> images = Arrays.asList(image1);

		Version latestVersion = new Version("1.0.0");

		given(builder.buildAndExpand(eq(latestVersion.getPatch()), eq(image1.getFull())))
				.willReturn(uriComponents);
		given(uriComponents.toUriString())
				.willReturn(image1.getFull());

		assertThat(imageFetcher.setImgsSrcs(images, builder, latestVersion)).isEqualTo(0);
	}

	@Test
	public void setsImagesSourcesWithValidUrl() {
		Image image1 = new Image("arctichare.png", "arctichare.png", "group", new byte[0], 144, 48, 48, 48);
		List<Image> images = Arrays.asList(image1);

		Version latestVersion = new Version("1.0.0");

		given(builder.buildAndExpand(eq(latestVersion.getPatch()), eq(image1.getFull())))
				.willReturn(uriComponents);
		given(uriComponents.toUriString())
				.willReturn("http://homepages.cae.wisc.edu/~ece533/images/" + image1.getFull());

		assertThat(imageFetcher.setImgsSrcs(images, builder, latestVersion)).isEqualTo(1);
	}

	@Test
	public void setImagesSourcesWithNullVersion() {
		Image image1 = new Image("arctichare.png", "arctichare.png", "group", new byte[0], 144, 48, 48, 48);
		List<Image> images = Arrays.asList(image1);

		given(uriComponents.toUriString())
				.willReturn(image1.getFull());

		assertThat(imageFetcher.setImgsSrcs(images, builder, null)).isEqualTo(0);
	}

	@Test
	public void setsImageSourceWithInvalidUrl() {
		Image image = new Image("monarch.png", "monarch0.png", "group", new byte[0], 144, 48, 48, 48);

		Version latestVersion = new Version("1.0.0");

		given(builder.buildAndExpand(eq(latestVersion.getPatch()), eq(image.getFull())))
				.willReturn(uriComponents);
		given(uriComponents.toUriString())
				.willReturn(image.getFull());

		assertThat(imageFetcher.setImgSrc(image, builder, latestVersion)).isEqualTo(0);
	}

	@Test
	public void setsImageSourceWithValidUrl() {
		Image image = new Image("monarch.png", "monarch0.png", "group", new byte[0], 144, 48, 48, 48);

		Version latestVersion = new Version("1.0.0");

		given(builder.buildAndExpand(eq(latestVersion.getPatch()), eq(image.getFull())))
				.willReturn(uriComponents);
		given(uriComponents.toUriString())
				.willReturn("http://homepages.cae.wisc.edu/~ece533/images/" + image.getFull());

		assertThat(imageFetcher.setImgSrc(image, builder, latestVersion)).isOne();
	}

	@Test
	public void setImageSourceWithNullVersion() {
		Image image = new Image("monarch.png", "monarch0.png", "group", new byte[0], 144, 48, 48, 48);

		given(uriComponents.toUriString())
				.willReturn(image.getFull());

		assertThat(imageFetcher.setImgSrc(image, builder, null)).isEqualTo(0);
	}

}