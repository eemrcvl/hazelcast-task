package com.hazel.task;

import com.hazel.task.model.response.ClusterContentResponse;
import com.hazel.task.model.response.ClusterCreationResponse;
import com.hazel.task.services.cluster.HazelcastClusterServicesImpl;
import com.hazel.task.model.ClusterState;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HazelcastClustersAutomatedTests {
    // Arbitrary max. try count for inquiring availability of the cluster
    private final int MAX_TRY_COUNT = 100;

    @Autowired
    private HazelcastClusterServicesImpl clusterServices;

    @Order(1)
    @Test
    void createCluster() {
        ResponseEntity<ClusterCreationResponse> response = clusterServices.createCluster();

        // Check some conditions
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode()); // response code from the server
        Assertions.assertEquals(ClusterState.RUNNING, response.getBody().getDesiredState()); // desired state of cluster
        Assertions.assertEquals(ClusterState.PENDING, response.getBody().getState()); // current state of cluster
        Assertions.assertTrue(response.getBody().getId() > 0); // cluster ID
        Assertions.assertTrue(response.getBody().getClusterPassword().length() > 0); // auto-generated cluster password

        System.out.println("[INFO] Cluster creation request sent to the server.");
    }

    @Order(2)
    @Test
    void getClusterAndTestAvailability() throws InterruptedException {
        ClusterState clusterState = ClusterState.PENDING;
        int tryCount = 0;

        // Wait until cluster state is running
        while(clusterState == ClusterState.PENDING && tryCount < MAX_TRY_COUNT) {
            ResponseEntity<ClusterContentResponse> clusters = clusterServices.getMyClusters();
            Assertions.assertTrue(clusters.getBody().getContent().size() > 0); // Stop the test if not a valid body is received

            if(clusterIsRunning(clusters)) {
                // update the state
                clusterState = ClusterState.RUNNING;
                // check conditions
                Assertions.assertEquals(HttpStatus.OK, clusters.getStatusCode());
                Assertions.assertTrue(getClusterId(clusters) > 0);

                tryCount++;
                System.out.println("[STATUS] Cluster state is " + ClusterState.RUNNING + " [" + tryCount + "/" + MAX_TRY_COUNT + "]");
            } else {
                // Wait for some time before making another request (prevent a case in which Hazelcast servers blacklist my IP)
                Thread.sleep(2000);
                tryCount++;
                System.out.println("[STATUS] Cluster state is " + ClusterState.PENDING + " [" + tryCount + "/" + MAX_TRY_COUNT + "]");
            }
        }
    }

    @Order(3)
    @Test
    void deleteCluster() {
        // Check the availability of the cluster first
        ResponseEntity<ClusterContentResponse> clusters = clusterServices.getMyClusters();
        Assertions.assertNotNull(clusters.getBody().getContent());

        int clusterId = getClusterId(clusters);
        // Then post the delete request
        ResponseEntity response = clusterServices.deleteCluster(clusterId);
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        System.out.println("[INFO] Cluster deletion request sent to the server.");
    }

    @Order(4)
    @Test
    void checkClusterIsActuallyDeleted() throws InterruptedException {
        ClusterState clusterState = ClusterState.DELETE_IN_PROGRESS;
        int tryCount = 0;

        // Wait until cluster is deleted
        while(clusterState == ClusterState.DELETE_IN_PROGRESS && tryCount < MAX_TRY_COUNT) {
            // Get current clusters
            ResponseEntity<ClusterContentResponse> clusters = clusterServices.getMyClusters();
            Assertions.assertNotNull(clusters.getBody().getContent());

            if(clusterResponseIsEmpty(clusters)) {
                // If content is empty, it means there are no clusters, hence we can say the cluster we created earlier is deleted
                clusterState = ClusterState.DELETED;
                Assertions.assertEquals(HttpStatus.OK, clusters.getStatusCode());

                tryCount++;
                System.out.println("[STATUS] Cluster state is " + ClusterState.DELETED + " [" + tryCount + "/" + MAX_TRY_COUNT + "]");
            } else {
                Thread.sleep(2000);
                tryCount++;
                System.out.println("[STATUS] Cluster state is " + ClusterState.DELETE_IN_PROGRESS + " [" + tryCount + "/" + MAX_TRY_COUNT + "]");
            }
        }
    }

    private boolean clusterResponseIsEmpty(ResponseEntity<ClusterContentResponse> clusters) {
        return clusters.getBody().getContent().size() == 0;
    }

    private boolean clusterIsRunning(ResponseEntity<ClusterContentResponse> clusters) {
        return ClusterState.RUNNING.toString().equals(clusters.getBody().getContent().get(0).getState());
    }

    private int getClusterId(ResponseEntity<ClusterContentResponse> clusters) {
        return clusters.getBody().getContent().get(0).getId();
    }
}
