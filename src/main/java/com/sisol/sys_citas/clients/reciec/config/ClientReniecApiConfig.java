package com.sisol.sys_citas.clients.reciec.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class ClientReniecApiConfig {

    @Value("${api.reniec.base.url}")
    private String baseUrl;

    @Bean(name = "reniecRestTemplate")
    public RestTemplate reniecRestTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(baseUrl));
        return restTemplate;
    }
    



    
}
