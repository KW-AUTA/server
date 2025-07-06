package com.auta.server;

import com.auta.server.adapter.out.s3.S3Adapter;
import com.auta.server.application.port.out.auth.RefreshTokenStorePort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@ActiveProfiles("test")
public abstract class IntegrationTestSupport {

    @MockitoBean
    protected S3Adapter s3Port;

    @MockitoBean
    protected RefreshTokenStorePort refreshTokenStorePort;
}
