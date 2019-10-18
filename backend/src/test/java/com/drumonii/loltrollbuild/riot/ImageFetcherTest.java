package com.drumonii.loltrollbuild.riot;

import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.model.image.Image;
import org.junit.jupiter.api.Test;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class ImageFetcherTest {

	private ImageFetcher imageFetcher = new ImageFetcher();

	@Test
	void setsImagesSourcesWithInvalidUrl() {
		Image image1 = new Image("Aatrox.png", "champion0.png", "champion", new byte[0], 0, 0, 48, 48);
		Image image2 = new Image("Akali.png", "champion0.png", "champion", new byte[0], 96, 0, 48, 48);

		UriComponentsBuilder builder = UriComponentsBuilder
				.fromPath("/cdn/{latestVersion}/img/champion/{champion}");

		Version latestVersion = Version.patch("6.24.1");

		assertThat(imageFetcher.setImgsSrcs(Arrays.asList(image1, image2), builder, latestVersion)).isEqualTo(0);
	}

	@Test
	void setsImagesSourcesWithValidUrl() {
		Image image1 = new Image("Aatrox.png", "champion0.png", "champion", new byte[0], 0, 0, 48, 48);
		Image image2 = new Image("Akali.png", "champion0.png", "champion", new byte[0], 96, 0, 48, 48);

		UriComponentsBuilder builder = UriComponentsBuilder
				.fromHttpUrl("http://ddragon.leagueoflegends.com/cdn/{latestVersion}/img/champion/{champion}");

		Version latestVersion = Version.patch("6.24.1");

		assertThat(imageFetcher.setImgsSrcs(Arrays.asList(image1, image2), builder, latestVersion)).isEqualTo(2);
	}

	@Test
	void setImagesSourcesWithNullVersion() {
		Image image1 = new Image("Aatrox.png", "champion0.png", "champion", new byte[0], 0, 0, 48, 48);
		Image image2 = new Image("Akali.png", "champion0.png", "champion", new byte[0], 96, 0, 48, 48);

		UriComponentsBuilder builder = UriComponentsBuilder
				.fromHttpUrl("http://ddragon.leagueoflegends.com/cdn/{latestVersion}/img/champion/{champion}");

		assertThat(imageFetcher.setImgsSrcs(Arrays.asList(image1, image2), builder, null)).isEqualTo(0);
	}

	@Test
	void setsImageSourceWithInvalidUrl() {
		Image image = new Image("Alistar.png", "champion0.png", "champion", new byte[0], 144, 0, 48, 48);

		UriComponentsBuilder builder = UriComponentsBuilder
				.fromPath("/cdn/{latestVersion}/img/champion/{champion}");

		Version latestVersion = Version.patch("6.24.1");

		assertThat(imageFetcher.setImgSrc(image, builder, latestVersion)).isEqualTo(0);
	}

	@Test
	void setsImageSourceWithValidUrl() {
		Image image = new Image("Alistar.png", "champion0.png", "champion", new byte[0], 144, 0, 48, 48);

		UriComponentsBuilder builder = UriComponentsBuilder
				.fromHttpUrl("http://ddragon.leagueoflegends.com/cdn/{latestVersion}/img/champion/{champion}");

		Version latestVersion = Version.patch("6.24.1");

		assertThat(imageFetcher.setImgSrc(image, builder, latestVersion)).isOne();
	}

	@Test
	void setImageSourceWithNullVersion() {
		UriComponentsBuilder builder = UriComponentsBuilder
				.fromHttpUrl("http://ddragon.leagueoflegends.com/cdn/{latestVersion}/img/champion/{champion}");

		Image image = new Image("Alistar.png", "champion0.png", "champion", new byte[0], 144, 0, 48, 48);

		assertThat(imageFetcher.setImgSrc(image, builder, null)).isEqualTo(0);
	}

}
