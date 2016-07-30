package com.drumonii.loltrollbuild.controller;

import com.drumonii.loltrollbuild.BaseSpringTestRunner;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;
import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.RequestDispatcher;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class ErrorPageControllerTest extends BaseSpringTestRunner {

	@Test
	public void badRequest400() throws Exception {
		mockMvc.perform(get("/error").accept(MediaType.TEXT_HTML)
				.requestAttr(RequestDispatcher.ERROR_STATUS_CODE, 400)
				.requestAttr(DefaultErrorAttributes.class.getName() + ".ERROR",
						mock(BadController.AccessDeniedException.class))
				.requestAttr(RequestDispatcher.ERROR_REQUEST_URI, "/error"))
				.andExpect(model().attributeExists("timestamp", "status", "error", "exception", "message", "path"))
				.andExpect(model().attribute("error", is(HttpStatus.BAD_REQUEST.getReasonPhrase())))
				.andExpect(view().name("/error/400"));
	}

	@Test
	public void forbidden403() throws Exception {
		mockMvc.perform(get("/error").accept(MediaType.TEXT_HTML)
				.requestAttr(RequestDispatcher.ERROR_STATUS_CODE, 403)
				.requestAttr(DefaultErrorAttributes.class.getName() + ".ERROR",
						mock(BadController.AccessDeniedException.class))
				.requestAttr(RequestDispatcher.ERROR_REQUEST_URI, "/error"))
				.andExpect(model().attributeExists("timestamp", "status", "error", "exception", "message", "path"))
				.andExpect(model().attribute("error", is(HttpStatus.FORBIDDEN.getReasonPhrase())))
				.andExpect(view().name("/error/403"));
	}

	@Test
	public void notFound404() throws Exception {
		mockMvc.perform(get("/error").accept(MediaType.TEXT_HTML)
				.requestAttr(RequestDispatcher.ERROR_STATUS_CODE, 404)
				.requestAttr(DefaultErrorAttributes.class.getName() + ".ERROR",
						mock(BadController.NotFoundException.class))
				.requestAttr(RequestDispatcher.ERROR_REQUEST_URI, "/error"))
				.andExpect(model().attributeExists("timestamp", "status", "error", "exception", "message", "path"))
				.andExpect(model().attribute("error", is(HttpStatus.NOT_FOUND.getReasonPhrase())))
				.andExpect(view().name("/error/404"));
	}

	@Test
	public void internalServerError500() throws Exception {
		mockMvc.perform(get("/error").accept(MediaType.TEXT_HTML)
				.requestAttr(RequestDispatcher.ERROR_STATUS_CODE, 500)
				.requestAttr(DefaultErrorAttributes.class.getName() + ".ERROR",
						mock(BadController.InternalServerErrorException.class))
				.requestAttr(RequestDispatcher.ERROR_REQUEST_URI, "/error"))
				.andExpect(model().attributeExists("timestamp", "status", "error", "exception", "message", "path"))
				.andExpect(model().attribute("error", is(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())))
				.andExpect(view().name("/error/500"));
	}

	@Test
	public void otherError() throws Exception {
		List<HttpStatus> httpStatuses = Arrays.stream(HttpStatus.values())
				.filter(status -> status != HttpStatus.FORBIDDEN && status != HttpStatus.NOT_FOUND)
				.collect(Collectors.toList());
		HttpStatus httpStatus = httpStatuses.get(RandomUtils.nextInt(0, httpStatuses.size()));
		mockMvc.perform(get("/error").accept(MediaType.TEXT_HTML)
				.requestAttr(RequestDispatcher.ERROR_STATUS_CODE, httpStatus.value())
				.requestAttr(DefaultErrorAttributes.class.getName() + ".ERROR",
						mock(RuntimeException.class))
				.requestAttr(RequestDispatcher.ERROR_REQUEST_URI, "/error"))
				.andExpect(model().attributeExists("timestamp", "status", "error", "exception", "message", "path"))
				.andExpect(model().attribute("status", is(httpStatus.value())))
				.andExpect(view().name("/error/500"));
	}

}