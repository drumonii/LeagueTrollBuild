package com.drumonii.loltrollbuild.riot.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class RiotClientHttpRequestInterceptorTest {

    private RiotClientHttpRequestInterceptor interceptor = new RiotClientHttpRequestInterceptor();

    @Mock
    private ClientHttpRequestExecution execution;

    @Test
    public void interceptorShouldAddAcceptHeaders() throws Exception {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        ClientHttpRequest request = requestFactory.createRequest(new URI("https://ddragon.leagueoflegends.com/api/"), HttpMethod.GET);
        byte[] body = new byte[] {};

        interceptor.intercept(request, body, execution);

        assertThat(request.getHeaders().getAccept()).contains(MediaType.APPLICATION_JSON);
    }

}
