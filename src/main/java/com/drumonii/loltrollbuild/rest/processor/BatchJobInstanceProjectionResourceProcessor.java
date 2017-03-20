package com.drumonii.loltrollbuild.rest.processor;

import com.drumonii.loltrollbuild.rest.BatchJobInstancesRestController;
import com.drumonii.loltrollbuild.rest.BatchStepExecutionsRestController;
import com.drumonii.loltrollbuild.rest.projection.BatchJobInstanceProjection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * {@link ResourceProcessor} to add only a single self {@link Link} to a {@link BatchJobInstanceProjection} {@link Resource}.
 */
@Component
public class BatchJobInstanceProjectionResourceProcessor implements ResourceProcessor<Resource<BatchJobInstanceProjection>> {

	@Value("${spring.data.rest.base-path}")
	private String apiPath;

	@Override
	public Resource<BatchJobInstanceProjection> process(Resource<BatchJobInstanceProjection> resource) {
		String api = apiPath.replace("/", "");

		String jobInstanceUrl = linkTo(methodOn(BatchJobInstancesRestController.class)
				.getBatchJobInstance(resource.getContent().getId())).toString().replace("$", "");
		UriComponents jobInstanceUriComponents = UriComponentsBuilder.fromHttpUrl(jobInstanceUrl).buildAndExpand(api);

		String stepExecutionsUrl = linkTo(methodOn(BatchStepExecutionsRestController.class)
				.getBatchStepExecutions(resource.getContent().getId())).toString().replace("$", "");
		UriComponents stepExecutionsUriComponents = UriComponentsBuilder.fromHttpUrl(stepExecutionsUrl).buildAndExpand(api);
		return new Resource<>(resource.getContent(), new Link(jobInstanceUriComponents.toString()).withSelfRel(),
				new Link(stepExecutionsUriComponents.toString()).withRel("stepExecutions"));
	}

}
