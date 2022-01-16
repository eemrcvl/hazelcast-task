package com.hazel.task.services.cluster;

import com.hazel.task.model.response.ClusterCreationResponse;
import com.hazel.task.model.response.ClusterContentResponse;
import org.springframework.http.ResponseEntity;

public interface RemoteClusterServices {
    ResponseEntity<ClusterCreationResponse> createCluster();
    ResponseEntity deleteCluster(int clusterId);
    ResponseEntity<ClusterContentResponse> getMyClusters();
}
