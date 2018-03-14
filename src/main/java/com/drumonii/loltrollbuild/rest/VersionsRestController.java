package com.drumonii.loltrollbuild.rest;

import com.drumonii.loltrollbuild.model.Version;
import com.drumonii.loltrollbuild.repository.VersionsRepository;
import com.drumonii.loltrollbuild.rest.status.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * Repository REST controller for {@link Version}s.
 */
@RestController
@RequestMapping("${api.base-path}/versions")
public class VersionsRestController {

	@Autowired
	private VersionsRepository versionsRepository;

	/**
	 * Gets a {@link List} of {@link Version}s from the sort and search parameters.
	 *
	 * @param sort the {@link Sort}
	 * @param version the search {@link Version} to define as the QBE
	 * @return the {@link List} of {@link Version}s
	 */
	@GetMapping
	public List<Version> getVersions(
			@SortDefault(sort =  { "major", "minor", "revision" }, direction = Direction.DESC) Sort sort, Version version) {
		ExampleMatcher exampleMatcher = ExampleMatcher.matching()
				.withIgnorePaths("major", "minor", "revision");
		Example<Version> example = Example.of(version, exampleMatcher);
		return versionsRepository.findAll(example, sort);
	}

	/**
	 * Gets a {@link Version} by its patch. If not found, returns a 404, otherwise a 200.
	 *
	 * @param patch the patch to lookup the {@link Version}
	 * @return the {@link Version}
	 */
	@GetMapping(path = "/{patch}")
	public Version getVersion(@PathVariable String patch) {
		Optional<Version> version = versionsRepository.findById(patch);
		return version.orElseThrow(() -> new ResourceNotFoundException("Unable to find a Version with patch: " + patch));
	}

}
