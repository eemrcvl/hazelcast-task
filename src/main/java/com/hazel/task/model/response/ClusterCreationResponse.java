package com.hazel.task.model.response;

import com.hazel.task.model.ClusterState;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClusterCreationResponse {
    private int id;
    private ClusterState state;
    private ClusterState desiredState;
    private String name;
    private String clusterPassword;
}
