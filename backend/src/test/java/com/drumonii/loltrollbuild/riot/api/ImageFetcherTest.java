package com.drumonii.loltrollbuild.riot.api;

import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.model.image.Image;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnit4.class)
public class ImageFetcherTest {

	private ImageFetcher imageFetcher = new ImageFetcher();

	@Test
	public void setsImagesSourcesWithInvalidUrl() {
		Image image1 = new Image("Aatrox.png", "champion0.png", "champion", new byte[0], 0, 0, 48, 48);
		Image image2 = new Image("Akali.png", "champion0.png", "champion", new byte[0], 96, 0, 48, 48);

		UriComponentsBuilder builder = UriComponentsBuilder
				.fromPath("/cdn/{latestVersion}/img/champion/{champion}");

		Version latestVersion = new Version("6.24.1");

		assertThat(imageFetcher.setImgsSrcs(Arrays.asList(image1, image2), builder, latestVersion)).isEqualTo(0);
	}

	@Test
	public void setsImagesSourcesWithValidUrl() {
		Image image1 = new Image("Aatrox.png", "champion0.png", "champion", new byte[0], 0, 0, 48, 48);
		Image image2 = new Image("Akali.png", "champion0.png", "champion", new byte[0], 96, 0, 48, 48);

		UriComponentsBuilder builder = UriComponentsBuilder
				.fromHttpUrl("http://ddragon.leagueoflegends.com/cdn/{latestVersion}/img/champion/{champion}");

		Version latestVersion = new Version("6.24.1");

		assertThat(imageFetcher.setImgsSrcs(Arrays.asList(image1, image2), builder, latestVersion)).isEqualTo(2);
	}

	@Test
	public void setImagesSourcesWithNullVersion() {
		Image image1 = new Image("Aatrox.png", "champion0.png", "champion", new byte[0], 0, 0, 48, 48);
		Image image2 = new Image("Akali.png", "champion0.png", "champion", new byte[0], 96, 0, 48, 48);

		UriComponentsBuilder builder = UriComponentsBuilder
				.fromHttpUrl("http://ddragon.leagueoflegends.com/cdn/{latestVersion}/img/champion/{champion}");

		assertThat(imageFetcher.setImgsSrcs(Arrays.asList(image1, image2), builder, null)).isEqualTo(0);
	}

	@Test
	public void setsImageSourceWithInvalidUrl() {
		Image image = new Image("Alistar.png", "champion0.png", "champion", new byte[0], 144, 0, 48, 48);

		UriComponentsBuilder builder = UriComponentsBuilder
				.fromPath("/cdn/{latestVersion}/img/champion/{champion}");

		Version latestVersion = new Version("6.24.1");

		assertThat(imageFetcher.setImgSrc(image, builder, latestVersion)).isEqualTo(0);
	}

	@Test
	public void setsImageSourceWithValidUrl() {
		Image image = new Image("Alistar.png", "champion0.png", "champion", new byte[0], 144, 0, 48, 48);

		UriComponentsBuilder builder = UriComponentsBuilder
				.fromHttpUrl("http://ddragon.leagueoflegends.com/cdn/{latestVersion}/img/champion/{champion}");

		Version latestVersion = new Version("6.24.1");

		assertThat(imageFetcher.setImgSrc(image, builder, latestVersion)).isOne();
	}

	@Test
	public void setImageSourceWithNullVersion() {
		UriComponentsBuilder builder = UriComponentsBuilder
				.fromHttpUrl("http://ddragon.leagueoflegends.com/cdn/{latestVersion}/img/champion/{champion}");

		Image image = new Image("Alistar.png", "champion0.png", "champion", new byte[0], 144, 0, 48, 48);

		assertThat(imageFetcher.setImgSrc(image, builder, null)).isEqualTo(0);
	}

}