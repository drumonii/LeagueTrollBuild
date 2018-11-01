package com.drumonii.loltrollbuild.security.logout;

import com.drumonii.loltrollbuild.security.logout.LogoutResponse.FailedLogoutResponseBuilder;
import com.drumonii.loltrollbuild.security.logout.LogoutResponse.SuccessfulLogoutResponseBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class JsonLogoutSuccessHandler implements LogoutSuccessHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        LogoutResponse logoutResponse;

        if (authentication == null) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

            logoutResponse = new FailedLogoutResponseBuilder()
                    .build();
        } else {
            response.setStatus(HttpStatus.OK.value());

            logoutResponse = new SuccessfulLogoutResponseBuilder()
                    .fromAuthentication(authentication)
                    .build();
        }

        PrintWriter out = response.getWriter();
        out.print(objectMapper.writeValueAsString(logoutResponse));
        out.flush();
    }

}
