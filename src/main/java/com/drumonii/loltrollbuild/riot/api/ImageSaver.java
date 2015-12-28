package com.drumonii.loltrollbuild.riot.api;

import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.model.image.Image;
import com.drumonii.loltrollbuild.repository.VersionsRepository;
import com.drumonii.loltrollbuild.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.drumonii.loltrollbuild.config.WebMvcConfig.RESOURCE_DIR;
import static com.drumonii.loltrollbuild.config.WebMvcConfig.TEMP_DIR;

/**
 * Component for saving images of a model, copied from Riot, to the file system.
 */
@Component
@Slf4j
public class ImageSaver {

	@Autowired
	private VersionsRepository versionsRepository;

	/**
	 * Saves a {@link List} of {@link Image} from a {@link UriComponentsBuilder} and returns the number of images that
	 * were saved or overwritten. If the overwrite parameter is {@code true} and a file with the image's filename
	 * already exists, then the existing file will be overwritten. Otherwise, the existing file will remain and no image
	 * is saved. Note, a patch version must exist in the database, else a 404 response status will be thrown.
	 *
	 * @param images the {@link List} of a model's {@link Image}
	 * @param overwrite flag if set to {@code true}, will overwrite the existing file (if one exists)
	 * @param builder the {@link UriComponentsBuilder} to build the URI from a patch version and the image's filename
	 * @return the number of images saved or overwritten
	 */
	public int copyImagesFromURLs(List<Image> images, boolean overwrite, UriComponentsBuilder builder) {
		boolean success = FileUtil.createTempResourceDir(RESOURCE_DIR);
		int saved = 0;
		Version latestVersion = versionsRepository.latestVersion();
		if (latestVersion == null) {
			throw new ResourceNotFoundException("No latest patch version in the database.");
		}
		for (Image image : images) {
			UriComponents uriComponents = builder.buildAndExpand(latestVersion.getPatch(), image.getFull());
			URL url = null;
			try {
				url = new URL(uriComponents.toUriString());
			} catch (MalformedURLException e) {
				log.error("Unable to create the URL with " + uriComponents.toString());
			}
			if (url != null && success) {
				Path imagePath = Paths.get(TEMP_DIR, RESOURCE_DIR, image.getFull());
				saved += FileUtil.copyURLToFile(url, imagePath, overwrite);
			}
		}
		return saved;
	}

	/**
	 * Saves a {@link Image} from a {@link UriComponentsBuilder} and returns the number of image that was saved or
	 * overwritten. If a file with the image's filename already exists, then the existing file will be overwritten.
	 * Note, a patch version must exist in the database, else a 404 response status will be thrown.
	 *
	 * @param image the model's {@link Image}
	 * @param builder the {@link UriComponentsBuilder} to build the URI from a patch version and the image's filename
	 * @return the number of image saved or overwritten
	 */
	public int copyImageFromURL(Image image, UriComponentsBuilder builder) {
		boolean success = FileUtil.createTempResourceDir(RESOURCE_DIR);
		int saved = 0;
		Version latestVersion = versionsRepository.latestVersion();
		if (latestVersion == null) {
			throw new ResourceNotFoundException("No latest patch version in the database.");
		}
		UriComponents uriComponents = builder.buildAndExpand(latestVersion.getPatch(), image.getFull());
		URL url = null;
		try {
			url = new URL(uriComponents.toUriString());
		} catch (MalformedURLException e) {
			log.error("Unable to create the URL with " + uriComponents.toString());
		}
		if (url != null && success) {
			Path imagePath = Paths.get(TEMP_DIR, RESOURCE_DIR, image.getFull());
			saved += FileUtil.copyURLToFile(url, imagePath, true);
		}
		return saved;
	}

	/**
	 * Deletes a {@link List} of {@link Image} and returns the number of images that were deleted.
	 *
	 * @param images the {@link List} of a model's {@link Image}
	 * @return the number of images deleted
	 */
	public int deleteImages(List<Image> images) {
		int saved = 0;
		for (Image image : images) {
			Path path = Paths.get(TEMP_DIR, RESOURCE_DIR, image.getFull());
			saved += FileUtil.deleteFile(path);
		}
		return saved;
	}

	/**
	 * Deletes a {@link Image} and returns the number of image that was deleted.
	 *
	 * @param image model's {@link Image}
	 * @return number of image deleted
	 */
	public int deleteImage(Image image) {
		Path path = Paths.get(TEMP_DIR, RESOURCE_DIR, image.getFull());
		return FileUtil.deleteFile(path);
	}

}
