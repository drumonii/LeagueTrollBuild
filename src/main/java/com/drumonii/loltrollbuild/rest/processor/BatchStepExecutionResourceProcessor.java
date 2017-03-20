package com.drumonii.loltrollbuild.rest.processor;

import com.drumonii.loltrollbuild.model.BatchStepExecution;
import com.drumonii.loltrollbuild.rest.BatchJobInstancesRestController;
import com.drumonii.loltrollbuild.rest.BatchStepExecutionsRestController;
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
 * {@link ResourceProcessor} to add only a single self {@link Link} to a {@link BatchStepExecution} {@link Resource}.
 */
@Component
public class BatchStepExecutionResourceProcessor implements ResourceProcessor<Resource<BatchStepExecution>> {

	@Value("${spring.data.rest.base-path}")
	private String apiPath;

	@Override
	public Resource<BatchStepExecution> process(Resource<BatchStepExecution> resource) {
		String api = apiPath.replace("/", "");

		String stepExecutionUrl = linkTo(methodOn(BatchStepExecutionsRestController.class)
				.getBatchStepExecution(resource.getContent().getJobExecution().getJobInstance().getId(),
						resource.getContent().getId())).toString().replace("$", "");
		UriComponents stepExecutionUriComponents = UriComponentsBuilder.fromHttpUrl(stepExecutionUrl).buildAndExpand(api);

		String jobInstanceUrl = linkTo(methodOn(BatchJobInstancesRestController.class)
				.getBatchJobInstance(resource.getContent().getJobExecution().getJobInstance().getId())).toString().replace("$", "");
		UriComponents jobInstanceUriComponents = UriComponentsBuilder.fromHttpUrl(jobInstanceUrl).buildAndExpand(api);

		return new Resource<>(resource.getContent(), new Link(stepExecutionUriComponents.toString()).withSelfRel(),
				new Link(jobInstanceUriComponents.toString()).withRel("jobInstance"));
	}

}
