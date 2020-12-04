package com.drumonii.loltrollbuild.security.login;

import com.drumonii.loltrollbuild.security.login.LoginResponse.FailedLoginResponseBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * An {@link AuthenticationFailureHandler} that sends a JSON response of the {@link AuthenticationException} as a 200 OK.
 */
public class JsonAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException {
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        LoginResponse failedLoginResponse = new FailedLoginResponseBuilder()
                .fromAuthenticationException(exception)
                .build();

        PrintWriter out = response.getWriter();
        out.print(objectMapper.writeValueAsString(failedLoginResponse));
        out.flush();
    }

}
