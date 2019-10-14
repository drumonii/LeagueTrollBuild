package com.drumonii.loltrollbuild.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * An {@link AbstractErrorController} that forward requests to Angular so it can resolve the route.
 */
@Controller
@RequestMapping("${error.path:/error}")
public class ErrorController extends AbstractErrorController {

    @Value("${error.path:/error}")
    private String path;

    private final ErrorAttributes errorAttributes;

    public ErrorController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
        this.errorAttributes = errorAttributes;
    }

    @RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
    public String errorHtml(HttpServletRequest request) {
        WebRequest webRequest = new ServletWebRequest(request);

        Map<String, Object> errorAttrs = this.errorAttributes.getErrorAttributes(webRequest, false);
        String requestPath = (String) errorAttrs.get("path");

        if (requestPath.startsWith("/admin")) {
            return "forward:/admin/index.html";
        }

        return "forward:/troll-build/index.html";
    }

    @Override
    public String getErrorPath() {
        return path;
    }

}
