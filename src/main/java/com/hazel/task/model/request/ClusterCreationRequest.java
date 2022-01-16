package com.hazel.task.model.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ClusterCreationRequest {
    private String hazelcastVersion;
    private String name;
    private List<Integer> regions;
    private List<Integer> cloudProviders;
    private int clusterTypeId;
    private boolean autoScalingEnabled;
    private boolean hotRestartEnabled;
    private boolean hotBackupEnabled;
    private boolean tlsEnabled;
    private boolean ipWhitelistEnabled;

    public String toJSONString() {
        return "{\"hazelcastVersion\":\"" + hazelcastVersion
                + "\",\"name\":\"" + name
                + "\",\"regions\":" + regions
                + ",\"clusterTypeId\":" + clusterTypeId
                + ",\"autoScalingEnabled\":" + autoScalingEnabled
                + ",\"hotRestartEnabled\":" + hotRestartEnabled
                + ",\"hotBackupEnabled\":" + hotBackupEnabled
                + ",\"tlsEnabled\":" + tlsEnabled
                + ",\"ipWhitelistEnabled\":" + ipWhitelistEnabled
                + ",\"cloudProviders\":" + cloudProviders
                +"}";
    }
}
