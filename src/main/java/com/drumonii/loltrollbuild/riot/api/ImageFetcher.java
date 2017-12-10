package com.drumonii.loltrollbuild.riot.api;

import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.model.image.Image;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Component for fetching {@link Image}s of a model from Riot.
 */
@Component
@Slf4j
public class ImageFetcher {

	/**
	 * Sets a {@link List} of {@link Image} sources from a {@link UriComponentsBuilder}.
	 *
	 * @param images the {@link List} of a model's {@link Image}
	 * @param builder the {@link UriComponentsBuilder} to build the URI from a patch version and the image's filename
	 * @param latestVersion the latest {@link Version} from Riot
	 * @return number of image sources set
	 */
	public int setImgsSrcs(List<Image> images, UriComponentsBuilder builder, Version latestVersion) {
		int count = 0;
		for (Image image : images) {
			if (setImgSrc(image, builder, latestVersion) == 1) {
				count++;
			}
		}
		return count;
	}

	/**
	 * Sets a single {@link Image} source from a {@link UriComponentsBuilder}.
	 *
	 * @param image the model's {@link Image}
	 * @param builder the {@link UriComponentsBuilder} to build the URI from a patch version and the image's filename
	 * @param latestVersion the latest {@link Version} from Riot
	 * @return number of image sources set
	 */
	public int setImgSrc(Image image, UriComponentsBuilder builder, Version latestVersion) {
		int count = 0;
		UriComponents uriComponents = null;
		if (latestVersion != null) {
			uriComponents = builder.buildAndExpand(latestVersion.getPatch(), image.getFull());
		}
		URL url = null;
		try {
			url = uriComponents == null ? null : new URL(uriComponents.toUriString());
		} catch (MalformedURLException e) {
			log.error("Unable to create the URL with " + uriComponents.toString());
		}
		if (url != null) {
			try {
				image.setImgSrc(IOUtils.toByteArray(url.openStream()));
				count++;
			} catch (IOException e) {
				log.warn("Unable to retrieve the image from URL: " + url + " because: ", e);
			}
		}
		return count;
	}

}
