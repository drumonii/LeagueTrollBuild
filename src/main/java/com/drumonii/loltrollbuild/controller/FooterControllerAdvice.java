package com.drumonii.loltrollbuild.controller;

import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.repository.VersionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * {@link ControllerAdvice} to add the current saved patch to the footer.
 */
@ControllerAdvice
public class FooterControllerAdvice {

	@Autowired
	private VersionsRepository versionsRepository;

	@ModelAttribute("latestSavedPatch")
	public String latestSavedPatch() {
		Version version = versionsRepository.latestVersion();
		return version == null ? null : version.getPatch();
	}

}
