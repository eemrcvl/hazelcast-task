package com.hazel.task.model.response;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class ClusterContentResponse {
    private ArrayList<Content> content;

    @Getter
    @Setter
    public static class Content {
        private int id;
        private int customerId;
        private double memory;
        private int port;
        private String name;
        private String state;
    }

}
