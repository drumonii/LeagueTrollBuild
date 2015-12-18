package com.drumonii.loltrollbuild.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.drumonii.loltrollbuild.config.WebMvcConfig.TEMP_DIR;

/**
 * Utility methods relating to {@link Path}s (files).
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class FileUtil {

	/**
	 * Copies a URL to a file and returns the number of file that was saved or overwritten. If a previous file with the
	 * same file name already exists and the overwrite flag is {@code false}, then the file is unaltered.
	 *
	 * @param url the source {@link URL} to copy from
	 * @param file the specified {@link Path} to copy the URL as a file
	 * @param overwrite if {@code true}, then will overwrite any previous file with the same file name
	 * @return the number of file saved or overwritten
	 */
	public static int copyURLToFile(URL url, Path file, boolean overwrite) {
		int saved = 0;
		if (Files.exists(file)) {
			if (overwrite) {
				try {
					FileUtils.copyURLToFile(url, file.toFile(), 15000, 15000);
					log.info("Replaced the existing " + file);
					saved++;
				} catch (IOException e) {
					log.warn("Unable to overwrite existing " + file + " from URL: " + url + " because " +
							ExceptionUtils.getRootCauseMessage(e));
				}
			}
		} else {
			try {
				FileUtils.copyURLToFile(url, file.toFile(), 15000, 15000);
				log.info("Copied " + url + " to " + file);
				saved++;
			} catch (IOException e) {
				log.warn("Didn't copy " + file + " from URL: " + url + " because " +
						ExceptionUtils.getRootCauseMessage(e));
			}
		}
		return saved;
	}

	/**
	 * Creates a new directory with the specified parameter in the temp folder and returns whether it was successfully
	 * created.
	 *
	 * @param dir the name of the directory to create in the temp folder
	 * @return {@code true} if the directory was successfully created or already exists, else {@code false}
	 */
	public static boolean createTempResourceDir(String dir) {
		boolean success = true;
		Path resourceDir;
		try {
			resourceDir = Paths.get(TEMP_DIR, dir);
		} catch (InvalidPathException e) {
			log.warn("Unable to create the " + dir + " because " + ExceptionUtils.getRootCauseMessage(e));
			return false;
		}
		if (!Files.exists(resourceDir)) {
			try {
				Files.createDirectory(resourceDir);
			} catch (IOException e) {
				log.warn("Unable to create the " + resourceDir + " because " + ExceptionUtils.getRootCauseMessage(e));
				success = false;
			}
		}
		return success;
	}

	/**
	 * Deletes a file and returns the number of file that was deleted.
	 *
	 * @param file the specified {@link Path} to delete
	 * @return the number of file deleted
	 */
	public static int deleteFile(Path file) {
		int saved = 0;
		try {
			Files.delete(file);
			log.info("Deleted " + file);
			saved++;
		} catch (IOException e) {
			log.warn("Unable to delete path " + file + " because " + ExceptionUtils.getRootCauseMessage(e));
		}
		return saved;
	}

}
