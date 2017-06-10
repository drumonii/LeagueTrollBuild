package com.drumonii.loltrollbuild.riot.api;

import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.model.image.Image;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;

import static com.drumonii.loltrollbuild.config.Profiles.TESTING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, properties = { "riot.api.key=API_KEY", "spring.cache.type=simple" })
@ActiveProfiles(TESTING)
public class ImageFetcherTest {

	@Autowired
	private ImageFetcher imageFetcher;

	@Mock
	private UriComponentsBuilder builder;

	@Mock
	private UriComponents uriComponents;

	@Before
	public void before() {

		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void setImgsSrcs() throws Exception {
		Image image1 = new Image("arctichare.png", "arctichare.png", "group", new byte[0], 144, 48, 48, 48);
		Image image2 = new Image("fruits.png", "fruits.png", "group", new byte[0], 144, 48, 48, 48);
		List<Image> images = Arrays.asList(image1, image2);

		Version latestVersion = new Version("1.0.0");

		given(builder.buildAndExpand(anyString(), anyString()))
				.willReturn(uriComponents);
		for (Image image : images) {
			given(uriComponents.toUriString())
					.willReturn(image.getFull());
		}
		assertThat(imageFetcher.setImgsSrcs(images, builder, latestVersion)).isEqualTo(0);

		given(builder.buildAndExpand(anyString(), anyString()))
				.willReturn(uriComponents);
		for (Image image : images) {
			given(uriComponents.toUriString())
					.willReturn("http://homepages.cae.wisc.edu/~ece533/images/" + image.getFull());
		}
		assertThat(imageFetcher.setImgsSrcs(images, builder, latestVersion)).isEqualTo(2);
	}

	@Test
	public void setImgSrc() throws Exception {
		Image image = new Image("monarch.png", "monarch0.png", "group", new byte[0], 144, 48, 48, 48);

		Version latestVersion = new Version("1.0.0");

		given(builder.buildAndExpand(anyString(), anyString()))
				.willReturn(uriComponents);
		given(uriComponents.toUriString())
				.willReturn(image.getFull());
		assertThat(imageFetcher.setImgSrc(image, builder, latestVersion)).isEqualTo(0);

		given(builder.buildAndExpand(anyString(), anyString()))
				.willReturn(uriComponents);
		given(uriComponents.toUriString())
				.willReturn("http://homepages.cae.wisc.edu/~ece533/images/" + image.getFull());
		assertThat(imageFetcher.setImgSrc(image, builder, latestVersion)).isOne();
	}

}