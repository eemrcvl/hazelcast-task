package com.hazel.task.model;

public enum ClusterState {
    RUNNING("RUNNING"),
    PENDING("PENDING"),
    DELETE_IN_PROGRESS("DELETE_IN_PROGRESS"),
    DELETED("DELETED");

    private String state;

    ClusterState(String state) {
        this.state = state;
    }

    public String getState() {
        return this.state;
    }
}
