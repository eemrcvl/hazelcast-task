package com.hazel.task.util;

import com.hazel.task.config.ConfigBinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

/**
 * Returns valid headers for Hazelcast services.
 */
@Service
public class HazelcastServiceHeader {

    @Autowired
    private ConfigBinder configBinder;

    private HttpHeaders headers;

    public HttpHeaders getHttpHeaders() {
        if(this.headers == null) {
            // Prevent re-instantiating headers every time
            this.headers = new HttpHeaders();
            this.headers.set("Authorization", "Bearer " + configBinder.getBearerToken());
            this.headers.setContentType(MediaType.APPLICATION_JSON);
        }
        return this.headers;
    }
}
