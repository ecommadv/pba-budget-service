package com.pba.budgetservice.integration;

import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

public class MongoDBContainerConfig {
    public static MongoDBContainer getInstance() {
        return new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));
    }
}
