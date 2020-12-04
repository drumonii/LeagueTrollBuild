package com.drumonii.loltrollbuild.security.logout;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class JsonLogoutSuccessHandlerTest {

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private JsonLogoutSuccessHandler logoutSuccessHandler;

    @Test
    void onLogoutSuccessWithAuthentication() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        User user = new User("username", "password", AuthorityUtils.createAuthorityList("ROLE_ADMIN"));
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        String json =
                "{" +
                "  \"status\": \"SUCCESS\"," +
                "  \"message\": \"Logout success\"," +
                "  \"userDetails\": { " +
                "    \"name\": \"username\"," +
                "    \"authorities\": [" +
                "      {" +
                "        \"authority\": \"ROLE_ADMIN\"" +
                "      }" +
                "    ]" +
                "  }" +
                "}";

        given(objectMapper.writeValueAsString(any(LogoutResponse.class))).willReturn(json);

        logoutSuccessHandler.onLogoutSuccess(request, response, authenticationToken);

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo("application/json");
        assertThat(response.getContentAsString()).isEqualTo(json);
    }

    @Test
    void onLogoutSuccessWithoutAuthentication() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();


        String json =
                "{" +
                "  \"status\": \"FAILED\"," +
                "  \"message\": \"Logout failed\"" +
                "}";

        given(objectMapper.writeValueAsString(any(LogoutResponse.class))).willReturn(json);

        logoutSuccessHandler.onLogoutSuccess(request, response, null);

        assertThat(response.getStatus()).isEqualTo(500);
        assertThat(response.getContentType()).isEqualTo("application/json");
        assertThat(response.getContentAsString()).isEqualTo(json);
    }

}
