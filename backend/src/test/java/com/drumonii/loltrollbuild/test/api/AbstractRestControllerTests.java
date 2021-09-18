package com.drumonii.loltrollbuild.test.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static com.drumonii.loltrollbuild.config.Profiles.TESTING;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles({ TESTING })
public abstract class AbstractRestControllerTests {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected TestRestTemplate testRestTemplate;

}
