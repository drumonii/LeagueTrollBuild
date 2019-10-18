package com.drumonii.loltrollbuild.security.login;

import com.drumonii.loltrollbuild.security.login.LoginResponse.FailedLoginResponseBuilder;
import com.drumonii.loltrollbuild.security.login.LoginResponse.SuccessfulLoginResponseBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;

@JsonTest
class LoginResponseTest {

    @Autowired
    private JacksonTester<LoginResponse> jacksonTester;

    @Test
    void serializesIntoJson() {
        User user = new User("username", "password", AuthorityUtils.createAuthorityList("ROLE_ADMIN"));
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        LoginResponse response = new SuccessfulLoginResponseBuilder()
                .fromAuthentication(authenticationToken)
                .build();

        JsonContent<LoginResponse> jsonContent = null;
        try {
            jsonContent = jacksonTester.write(response);
        } catch (IOException e) {
            fail("Unable to serialize Login Response into JSON", e);
        }

        assertThat(jsonContent).hasJsonPathStringValue("$.status");
        assertThat(jsonContent).hasJsonPathStringValue("$.message");
        assertThat(jsonContent).hasJsonPathMapValue("$.userDetails");

        BadCredentialsException authenticationException = new BadCredentialsException("Bad Credentials");

        response = new FailedLoginResponseBuilder()
                .fromAuthenticationException(authenticationException)
                .build();

        jsonContent = null;
        try {
            jsonContent = jacksonTester.write(response);
        } catch (IOException e) {
            fail("Unable to serialize Login Response into JSON", e);
        }

        assertThat(jsonContent).hasJsonPathStringValue("$.status");
        assertThat(jsonContent).hasJsonPathStringValue("$.message");
        assertThat(jsonContent).doesNotHaveJsonPathValue("$.userDetails");
    }

}
