package com.asiainfo.data.config;

import com.asiainfo.data.service.SolrQueryService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.time.Duration;

@Configuration
public class RestTemplateConfig {

    private Log logger = LogFactory.getLog(SolrQueryService.class);

    @Resource
    private RestTemplateBuilder restTemplateBuilder;

    @Value("${spring.data.solr.host}")
    private String rootUri;

    @Bean
    public RestTemplate buildRestTemplate() {
        return restTemplateBuilder
                .rootUri(rootUri)
                .setConnectTimeout(5)
                .build();
    }
}
