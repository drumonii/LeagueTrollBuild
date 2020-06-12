package com.drumonii.loltrollbuild.routing;

import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

/**
 * An {@link AbstractErrorController} that forward requests to Angular so it can resolve the route.
 */
@Controller
@RequestMapping("${error.path:/error}")
public class ErrorController extends AbstractErrorController {

    private final ErrorAttributes errorAttributes;

    public ErrorController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
        this.errorAttributes = errorAttributes;
    }

    @RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
    public String errorHtml(WebRequest webRequest) {

        Map<String, Object> errorAttrs = this.errorAttributes.getErrorAttributes(webRequest, ErrorAttributeOptions.defaults());
        String requestPath = (String) errorAttrs.get("path");

        if (requestPath.startsWith("/admin")) {
            return "forward:/admin/index.html";
        }

        return "forward:/troll-build/index.html";
    }

    @Override
    public String getErrorPath() {
        return null;
    }

}
