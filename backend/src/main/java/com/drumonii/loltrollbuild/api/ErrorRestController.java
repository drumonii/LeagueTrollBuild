package com.drumonii.loltrollbuild.api;

import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.error.ErrorAttributeOptions.Include;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * An {@link ErrorController} that always returns the error as JSON.
 */
@RestController
@RequestMapping("${server.error.path:${error.path:/error}}")
public class ErrorRestController extends AbstractErrorController {

    public ErrorRestController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> errorJson(HttpServletRequest request) {
        Map<String, Object> body = getErrorAttributes(request, ErrorAttributeOptions.of(Include.MESSAGE));
        HttpStatus status = getStatus(request);
        return new ResponseEntity<>(body, status);
    }

    @Override
    public String getErrorPath() {
        return null;
    }

}
