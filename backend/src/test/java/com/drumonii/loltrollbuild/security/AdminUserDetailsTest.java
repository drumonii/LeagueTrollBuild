package com.drumonii.loltrollbuild.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;

@RunWith(SpringRunner.class)
@JsonTest
public class AdminUserDetailsTest {

    @Autowired
    private JacksonTester<AdminUserDetails> jacksonTester;

    @Test
    public void serializesIntoJson() {
        User user = new User("username", "password", AuthorityUtils.createAuthorityList("ROLE_ADMIN"));
        AdminUserDetails adminUserDetails = new AdminUserDetails(user);

        JsonContent<AdminUserDetails> jsonContent = null;
        try {
            jsonContent = jacksonTester.write(adminUserDetails);
        } catch (IOException e) {
            fail("Unable to serialize Admin User Details into JSON", e);
        }

        assertThat(jsonContent).hasJsonPathStringValue("$.username");
        assertThat(jsonContent).hasJsonPathArrayValue("$.authorities");
        assertThat(jsonContent).hasJsonPathStringValue("$.authorities[0].authority", "ROLE_ADMIN");
        assertThat(jsonContent).doesNotHaveJsonPathValue("$.password");
        assertThat(jsonContent).doesNotHaveJsonPathValue("$.enabled");
        assertThat(jsonContent).doesNotHaveJsonPathValue("$.accountNonExpired");
        assertThat(jsonContent).doesNotHaveJsonPathValue("$.accountNonLocked");
        assertThat(jsonContent).doesNotHaveJsonPathValue("$.credentialsNonExpired");
    }

}