package com.drumonii.loltrollbuild.riot.api;

import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.model.image.Image;
import com.drumonii.loltrollbuild.repository.VersionsRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Component for fetching {@link Image}s of a model from Riot.
 */
@Component
@Slf4j
public class ImageFetcher {

	@Autowired
	private VersionsRepository versionsRepository;

	/**
	 * Sets a {@link List} of {@link Image} sources from a {@link UriComponentsBuilder}. Note, a patch version must
	 * exist in the database, else a 404 response status will be thrown.
	 *
	 * @param images the {@link List} of a model's {@link Image}
	 * @param builder the {@link UriComponentsBuilder} to build the URI from a patch version and the image's filename
	 * @return number of image sources set
	 */
	public int setImgsSrcs(List<Image> images, UriComponentsBuilder builder) {
		Version latestVersion = versionsRepository.latestVersion();
		if (latestVersion == null) {
			throw new ResourceNotFoundException("No latest patch version in the database.");
		}
		int count = 0;
		for (Image image : images) {
			UriComponents uriComponents = builder.buildAndExpand(latestVersion.getPatch(), image.getFull());
			InputStream inputStream;
			URL url = null;
			try {
				url = new URL(uriComponents.toUriString());
			} catch (MalformedURLException e) {
				log.error("Unable to create the URL with " + uriComponents.toString());
			}
			if (url != null) {
				try {
					inputStream = url.openStream();
					image.setImgSrc(IOUtils.toByteArray(inputStream));
					count++;
				} catch (IOException e) {
					log.warn("Unable to retrieve the image from URL: " + url + " because " +
							ExceptionUtils.getRootCauseMessage(e));
				}
			}
		}
		return count;
	}

	/**
	 * Sets a single {@link Image} source from a {@link UriComponentsBuilder}. Note, a patch version must exist in the
	 * database, else a 404 response status will be thrown.
	 *
	 * @param image the model's {@link Image}
	 * @param builder the {@link UriComponentsBuilder} to build the URI from a patch version and the image's filename
	 * @return number of image sources set
	 */
	public int setImgSrc(Image image, UriComponentsBuilder builder) {
		Version latestVersion = versionsRepository.latestVersion();
		if (latestVersion == null) {
			throw new ResourceNotFoundException("No latest patch version in the database.");
		}
		int count = 0;
		UriComponents uriComponents = builder.buildAndExpand(latestVersion.getPatch(), image.getFull());
		InputStream inputStream;
		URL url = null;
		try {
			url = new URL(uriComponents.toUriString());
		} catch (MalformedURLException e) {
			log.error("Unable to create the URL with " + uriComponents.toString());
		}
		if (url != null) {
			try {
				inputStream = url.openStream();
				image.setImgSrc(IOUtils.toByteArray(inputStream));
				count++;
			} catch (IOException e) {
				log.warn("Unable to retrieve the image from URL: " + url + " because " +
						ExceptionUtils.getRootCauseMessage(e));
			}
		}
		return count;
	}

}
