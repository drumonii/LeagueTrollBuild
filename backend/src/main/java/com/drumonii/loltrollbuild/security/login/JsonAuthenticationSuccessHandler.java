package com.drumonii.loltrollbuild.security.login;

import com.drumonii.loltrollbuild.security.login.LoginResponse.SuccessfulLoginResponseBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * An {@link AuthenticationSuccessHandler} that sends a JSON response of the authenticated principal as a 200 OK.
 */
public class JsonAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException {
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        LoginResponse successfulLoginResponse = new SuccessfulLoginResponseBuilder()
                .fromAuthentication(authentication)
                .build();

        PrintWriter out = response.getWriter();
        out.print(objectMapper.writeValueAsString(successfulLoginResponse));
        out.flush();
    }

}
