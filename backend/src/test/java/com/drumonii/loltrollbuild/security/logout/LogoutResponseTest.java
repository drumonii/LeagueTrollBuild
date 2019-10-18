package com.drumonii.loltrollbuild.security.logout;

import com.drumonii.loltrollbuild.security.logout.LogoutResponse.FailedLogoutResponseBuilder;
import com.drumonii.loltrollbuild.security.logout.LogoutResponse.SuccessfulLogoutResponseBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;

@JsonTest
class LogoutResponseTest {

    @Autowired
    private JacksonTester<LogoutResponse> jacksonTester;

    @Test
    void serializesIntoJson() {
        User user = new User("username", "password", AuthorityUtils.createAuthorityList("ROLE_ADMIN"));
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        LogoutResponse response = new SuccessfulLogoutResponseBuilder()
                .fromAuthentication(authenticationToken)
                .build();

        JsonContent<LogoutResponse> jsonContent = null;
        try {
            jsonContent = jacksonTester.write(response);
        } catch (IOException e) {
            fail("Unable to serialize Logout Response into JSON", e);
        }

        assertThat(jsonContent).hasJsonPathStringValue("$.status");
        assertThat(jsonContent).hasJsonPathStringValue("$.message");
        assertThat(jsonContent).hasJsonPathMapValue("$.userDetails");

        response = new FailedLogoutResponseBuilder()
                .build();

        jsonContent = null;
        try {
            jsonContent = jacksonTester.write(response);
        } catch (IOException e) {
            fail("Unable to serialize Logout Response into JSON", e);
        }

        assertThat(jsonContent).hasJsonPathStringValue("$.status");
        assertThat(jsonContent).hasJsonPathStringValue("$.message");
        assertThat(jsonContent).doesNotHaveJsonPathValue("$.userDetails");
    }

}
