package com.drumonii.loltrollbuild.riot;

import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.model.image.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Component for fetching {@link Image}s of a model from Riot.
 */
@Component
public class ImageFetcher {

	private static final Logger LOGGER = LoggerFactory.getLogger(ImageFetcher.class);

	private static final int EOF = -1;
	private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

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
			LOGGER.error("Unable to create the URL with {}", uriComponents.toString(), e);
		}
		if (url != null) {
			try {
				image.setImgSrc(toByteArray(url.openStream()));
				count++;
			} catch (IOException e) {
				LOGGER.warn("Unable to retrieve the image from URL: {} because: ", url, e);
			}
		}
		return count;
	}

	/**
	 * Gets the contents of the {@link InputStream} as an array of {@code byte}s.
	 *
	 * @param input the {@link InputStream} to read
	 * @return the array of {@code byte}s.
	 * @throws IOException if an I/O error occurs
	 */
	private byte[] toByteArray(InputStream input) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		int bytes;
		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		while (EOF != (bytes = input.read(buffer))) {
			output.write(buffer, 0, bytes);
		}
		return output.toByteArray();
	}

}
