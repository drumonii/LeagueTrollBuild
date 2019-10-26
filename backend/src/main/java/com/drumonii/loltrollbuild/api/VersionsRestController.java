package com.drumonii.loltrollbuild.api;

import com.drumonii.loltrollbuild.api.service.VersionsApiService;
import com.drumonii.loltrollbuild.api.status.ResourceNotFoundException;
import com.drumonii.loltrollbuild.model.Version;
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
	private VersionsApiService versionsApiService;

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
		return versionsApiService.qbe(example, sort);
	}

	/**
	 * Gets a {@link Version} by its patch. If not found, returns a 404, otherwise a 200.
	 *
	 * @param patch the patch to lookup the {@link Version}
	 * @return the {@link Version}
	 */
	@GetMapping("/{patch}")
	public Version getVersion(@PathVariable String patch) {
		return versionsApiService.findById(patch)
				.orElseThrow(() -> new ResourceNotFoundException("Unable to find a Version with patch: " + patch));
	}

	/**
	 * Gets the latest saved {@link Version}. If there's no versions, returns 404, otherwise a 200.
	 *
	 * @return the {@link Version}
	 */
	@GetMapping("/latest")
	public Version getLatestVersion() {
		return Optional.ofNullable(versionsApiService.latestVersion())
				.orElseThrow(() -> new ResourceNotFoundException("Unable to get the latest Version with no saved Versions"));
	}

}
