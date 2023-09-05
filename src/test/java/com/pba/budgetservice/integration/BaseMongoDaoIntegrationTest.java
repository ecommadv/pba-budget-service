package com.pba.budgetservice.integration;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MongoDBContainer;

@SpringBootTest
@Transactional
public class BaseMongoDaoIntegrationTest {
    @ServiceConnection
    private static MongoDBContainer mongoDBContainer = MongoDBContainerConfig.getInstance();

    static {
        mongoDBContainer.start();
    }
}
