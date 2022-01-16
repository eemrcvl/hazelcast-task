package com.hazel.task.services.cluster;

import com.hazel.task.config.ConfigBinder;
import com.hazel.task.model.response.ClusterContentResponse;
import com.hazel.task.model.request.ClusterCreationRequest;
import com.hazel.task.model.response.ClusterCreationResponse;
import com.hazel.task.util.HazelcastServiceHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class HazelcastClusterServicesImpl implements RemoteClusterServices {

    @Autowired
    private ConfigBinder configBinder;

    @Autowired
    private HazelcastServiceHeader headers;

    @Override
    public ResponseEntity<ClusterCreationResponse> createCluster() {
        String url = configBinder.getBaseUrl() + configBinder.getClusterPrefix();

        // Create request payload
        ClusterCreationRequest creationRequest = createClusterCreationRequest();
        HttpEntity<ClusterCreationRequest> entity = new HttpEntity(creationRequest.toJSONString(), headers.getHttpHeaders());

        // Send request
        return new RestTemplate().exchange(url, HttpMethod.POST, entity, ClusterCreationResponse.class);
    }

    @Override
    public ResponseEntity deleteCluster(int clusterId) {
        String url = configBinder.getBaseUrl() + configBinder.getClusterPrefix() + clusterId;
        HttpEntity entity = new HttpEntity<>(headers.getHttpHeaders());
        return new RestTemplate().exchange(url, HttpMethod.DELETE, entity, Object.class);
    }

    @Override
    public ResponseEntity<ClusterContentResponse> getMyClusters() {
        String url = configBinder.getBaseUrl() + configBinder.getClusterPrefix();
        HttpEntity<ClusterContentResponse> entity = new HttpEntity<>(headers.getHttpHeaders());
        // Send request
        return new RestTemplate().exchange(url, HttpMethod.GET, entity, ClusterContentResponse.class);
    }

    private ClusterCreationRequest createClusterCreationRequest() {
        // Create request
        return ClusterCreationRequest.builder()
                .name("look-ma-I-can-create-clusters")
                .clusterTypeId(1)
                .hazelcastVersion("5.0")
                .autoScalingEnabled(false)
                .hotBackupEnabled(false)
                .hotRestartEnabled(false)
                .ipWhitelistEnabled(false)
                .tlsEnabled(false)
                .regions(List.of(1))
                .cloudProviders(List.of(1))
                .build();
    }
}
