package com.pba.budgetservice.unit;

import com.PBA.budgetservice.gateway.UserGateway;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles({"default", "test"})
@SpringBootTest
@WireMockTest(httpPort = 8089)
public class UserGatewayUnitTest {
    @Autowired
    private UserGateway userGateway;

    @Test
    public void testGetUserUidFromHeader() throws IOException {
        // given
        UUID userUid = UUID.randomUUID();
        String header = "Bearer token";
        this.stubUserDtoResponse(userUid);

        // when
        UUID result = userGateway.getUserUidFromAuthHeader(header);

        // then
        assertEquals(userUid, result);
    }

    private void stubUserDtoResponse(UUID userUid) throws IOException {
        String mockUserResponseJSON = String.format(getFileContent("classpath:mock_user_response.json"), userUid);
        stubFor(get(urlEqualTo("/api/user"))
                .willReturn(ok()
                        .withHeader("Content-Type", "application/json")
                        .withBody(mockUserResponseJSON)));
    }

    private String getFileContent(String path) throws IOException {
        return FileUtils.readFileToString(ResourceUtils.getFile(path), StandardCharsets.UTF_8);
    }
}
