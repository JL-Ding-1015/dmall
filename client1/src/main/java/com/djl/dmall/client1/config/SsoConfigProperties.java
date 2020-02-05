package com.djl.dmall.client1.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "sso.server")
public class SsoConfigProperties {
    private String url;
    private String loginPath;
}
