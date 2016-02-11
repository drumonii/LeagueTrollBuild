package com.drumonii.loltrollbuild.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.BasicErrorController;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Controller for rendering a separate error view for a specific {@link HttpStatus}. All error views have
 * {@link ErrorAttributes} defined in their {@link Model}.
 */
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class ErrorPageController extends BasicErrorController {

	private ErrorAttributes errorAttributes;

	@Autowired
	public ErrorPageController(ErrorAttributes errorAttributes) {
		super(errorAttributes, new ErrorProperties());
		this.errorAttributes = errorAttributes;
	}

	@Override
	@RequestMapping(produces = "text/html")
	public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> model = errorAttributes.getErrorAttributes(new ServletRequestAttributes(request), false);
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
		return new ModelAndView(view, model);
	}

}
