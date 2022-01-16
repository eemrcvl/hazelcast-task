package com.hazel.task.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class ConfigBinder {
    @Value("${hazelcast.baseUrl}")
    private String baseUrl;

    @Value("${hazelcast.clusterPrefix}")
    private String clusterPrefix;

    @Value("${hazelcast.bearerToken}")
    private String bearerToken;

    public String getBaseUrl() {
        return baseUrl;
    }
}
