package com.drumonii.loltrollbuild.test.rest;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.drumonii.loltrollbuild.config.Profiles.DDRAGON;
import static com.drumonii.loltrollbuild.config.Profiles.TESTING;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles({ TESTING, DDRAGON })
public abstract class AbstractRestTests {

    @Autowired
    protected MockMvc mockMvc;

    @Value("${api.base-path}")
    protected String apiPath;

    @Autowired
    protected TestRestTemplate testRestTemplate;

}
