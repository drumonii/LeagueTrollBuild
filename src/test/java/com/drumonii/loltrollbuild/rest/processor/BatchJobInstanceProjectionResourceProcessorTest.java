package com.drumonii.loltrollbuild.rest.processor;

import com.drumonii.loltrollbuild.rest.BatchJobInstancesRestController;
import com.drumonii.loltrollbuild.rest.projection.BatchJobExecutionProjection;
import com.drumonii.loltrollbuild.rest.projection.BatchJobInstanceProjection;
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
public class BatchJobInstanceProjectionResourceProcessorTest {

	@InjectMocks
	private BatchJobInstanceProjectionResourceProcessor resourceProcessor;

	@Mock
	private LinkBuilderFactory<LinkBuilder> linkBuilderFactory;

	@Before
	public void before() {
		ReflectionTestUtils.setField(resourceProcessor, "apiPath", "/api");

		MockHttpServletRequest request = new MockHttpServletRequest();
		ServletRequestAttributes requestAttributes = new ServletRequestAttributes(request);
		RequestContextHolder.setRequestAttributes(requestAttributes);

		given(linkBuilderFactory.linkTo(BatchJobInstancesRestController.class))
				.willReturn(linkTo(BatchJobInstancesRestController.class, "api", 1));
	}

	@Test
	public void processesBatchJobInstanceProjectionResource() {
		Resource<BatchJobInstanceProjection> resourceToProcess = new Resource<>(new DummyBatchJobInstanceProjection());

		Resource<BatchJobInstanceProjection> processedResource = resourceProcessor.process(resourceToProcess);
		assertThat(processedResource.getLinks()).hasSize(2);
		assertThat(processedResource.getLinks()).extracting(Link::getRel)
				.containsOnly("stepExecutions", "self");
		assertThat(processedResource.getLinks()).extracting(Link::getHref)
				.containsOnly("http://localhost/api/job-instances/1", "http://localhost/api/job-instances/1/step-executions");
	}

	private class DummyBatchJobInstanceProjection implements BatchJobInstanceProjection {

		@Override
		public long getId() {
			return 1;
		}

		@Override
		public String getName() {
			return null;
		}

		@Override
		public BatchJobExecutionProjection getJobExecution() {
			return null;
		}

	}

}