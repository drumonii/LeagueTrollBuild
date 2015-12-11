package com.drumonii.loltrollbuild.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.BasicErrorController;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

/**
 * Controller for rendering a separate error view for a specific {@link HttpStatus}. All error views have
 * {@link ErrorAttributes} defined in their {@link Model}.
 */
@Controller
@RequestMapping("${error.path:/error}")
public class ErrorPageController extends BasicErrorController {

	private ErrorAttributes errorAttributes;

	@Autowired
	public ErrorPageController(ErrorAttributes errorAttributes) {
		super(errorAttributes);
		this.errorAttributes = errorAttributes;
	}

	@RequestMapping(produces = "text/html")
	public String errorHtml(HttpServletRequest request, Model model) {
		model.addAllAttributes(errorAttributes.getErrorAttributes(new ServletRequestAttributes(request), false));
		String view;
		switch (getStatus(request)) {
			case FORBIDDEN: // 403
				view = String.valueOf(HttpStatus.FORBIDDEN.value());
				break;
			case NOT_FOUND: // 404
				view = String.valueOf(HttpStatus.NOT_FOUND.value());
				break;
			default: // 500 and other
				view = String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value());
				break;
		}
		return view;
	}

	private HttpStatus getStatus(HttpServletRequest request) {
		Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		if (statusCode != null) {
			try {
				return HttpStatus.valueOf(statusCode);
			} catch (Exception ex) {
				// Nothing here
			}
		}
		return HttpStatus.INTERNAL_SERVER_ERROR;
	}

}
