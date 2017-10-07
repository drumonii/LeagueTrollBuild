package com.drumonii.loltrollbuild.rest.processor;

import com.drumonii.loltrollbuild.model.BatchJobExecution;
import com.drumonii.loltrollbuild.model.BatchJobInstance;
import com.drumonii.loltrollbuild.model.BatchStepExecution;
import com.drumonii.loltrollbuild.rest.BatchStepExecutionsRestController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkBuilder;
import org.springframework.hateoas.LinkBuilderFactory;
import org.springframework.hateoas.Resource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RunWith(MockitoJUnitRunner.class)
public class BatchStepExecutionResourceProcessorTest {

	@InjectMocks
	private BatchStepExecutionResourceProcessor resourceProcessor;

	@Mock
	private LinkBuilderFactory<LinkBuilder> linkBuilderFactory;

	@Before
	public void before() {
		ReflectionTestUtils.setField(resourceProcessor, "apiPath", "/api");

		MockHttpServletRequest request = new MockHttpServletRequest();
		ServletRequestAttributes requestAttributes = new ServletRequestAttributes(request);
		RequestContextHolder.setRequestAttributes(requestAttributes);

		given(linkBuilderFactory.linkTo(BatchStepExecutionsRestController.class))
				.willReturn(linkTo(BatchStepExecutionsRestController.class, "api", 1, 2));
	}

	@Test
	public void processesBatchStepExecutionResource() {
		BatchJobInstance jobInstance = new BatchJobInstance();
		jobInstance.setId(1);

		BatchJobExecution jobExecution = new BatchJobExecution();
		jobExecution.setJobInstance(jobInstance);

		BatchStepExecution stepExecution = new BatchStepExecution();
		stepExecution.setId(2);
		stepExecution.setJobExecution(jobExecution);

		Resource<BatchStepExecution> resourceToProcess = new Resource<>(stepExecution);

		Resource<BatchStepExecution> processedResource = resourceProcessor.process(resourceToProcess);
		assertThat(processedResource.getLinks()).hasSize(2);
		assertThat(processedResource.getLinks()).extracting(Link::getRel)
				.containsOnly("jobInstance", "self");
		assertThat(processedResource.getLinks()).extracting(Link::getHref)
				.containsOnly("http://localhost/api/job-instances/1", "http://localhost/api/job-instances/1/step-executions/2");
	}

}