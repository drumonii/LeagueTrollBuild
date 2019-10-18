package com.drumonii.loltrollbuild.security.authentication;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.InsufficientAuthenticationException;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class Http401AuthenticationEntryPointTest {

    private Http401AuthenticationEntryPoint authenticationEntryPoint = new Http401AuthenticationEntryPoint();

    @Test
    void sendsErrorAsHttp401() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        InsufficientAuthenticationException authenticationException =
                new InsufficientAuthenticationException("Full authentication is required to access this resource");

        authenticationEntryPoint.commence(request, response, authenticationException);

        assertThat(response.getStatus()).isEqualTo(401);
    }

}
