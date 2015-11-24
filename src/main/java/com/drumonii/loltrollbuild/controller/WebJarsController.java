package com.drumonii.loltrollbuild.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.webjars.MultipleMatchesException;
import org.webjars.WebJarAssetLocator;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.web.servlet.HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE;

/**
 * Maps a WebJar asset request, without its version number, to its {@link ClassPathResource} as a {@link ResponseEntity}
 * using the webjars-locator library.
 *
 * @see <a href="http://www.webjars.org/documentation#springmvc">WebJars and Spring MVC</a>
 */
@RestController
@RequestMapping("/webjars")
public class WebJarsController {

	@Autowired
	private WebJarAssetLocator assetLocator;

	@RequestMapping(value = "/{webjar}/**", method = RequestMethod.GET)
	public ResponseEntity<ClassPathResource> locateWebjar(@PathVariable String webjar, HttpServletRequest request) {
		String mvcPrefix = "/webjars/" + webjar + "/";
		String mvcPath = (String) request.getAttribute(PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		String fullPath;
		try {
			fullPath = assetLocator.getFullPath(webjar, mvcPath.substring(mvcPrefix.length()));
		} catch (MultipleMatchesException e) {
			fullPath = assetLocator.getFullPathExact(webjar, mvcPath.substring(mvcPrefix.length()));
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(new ClassPathResource(fullPath), HttpStatus.OK);
	}

}
