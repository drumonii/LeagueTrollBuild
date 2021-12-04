package com.drumonii.loltrollbuild.security.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class JsonAuthenticationFailureHandlerTest {

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private JsonAuthenticationFailureHandler failureHandler;

    @Test
    void onAuthenticationFailure() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        BadCredentialsException authenticationException = new BadCredentialsException("Bad Credentials");

        String json =
                """
                {
                  "status": "FAILED",
                  "message": "Bad Credentials"
                }
                """;

        given(objectMapper.writeValueAsString(any(LoginResponse.class))).willReturn(json);

        failureHandler.onAuthenticationFailure(request, response, authenticationException);

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo("application/json");
        assertThat(response.getContentAsString()).isEqualTo(json);
    }

}
