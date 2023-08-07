package com.pba.budgetservice.integration;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {"classpath:/cleanup.sql"})
public class BaseDaoIntegrationTest {
    @ServiceConnection
    private static PostgreSQLContainer postgreSQLContainer = PostgreSqlContainerConfig.getInstance();

    static {
        postgreSQLContainer.start();
    }
}
