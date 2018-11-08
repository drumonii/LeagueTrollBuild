package com.drumonii.loltrollbuild.security.authentication;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.InsufficientAuthenticationException;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnit4.class)
public class Http401AuthenticationEntryPointTest {

    private Http401AuthenticationEntryPoint authenticationEntryPoint = new Http401AuthenticationEntryPoint();

    @Test
    public void sendsErrorAsHttp401() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        InsufficientAuthenticationException authenticationException =
                new InsufficientAuthenticationException("Full authentication is required to access this resource");

        authenticationEntryPoint.commence(request, response, authenticationException);

        assertThat(response.getStatus()).isEqualTo(401);
    }

}