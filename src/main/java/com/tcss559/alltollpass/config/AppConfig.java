package com.tcss559.alltollpass.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author sikha
 * This file defines the service URL
 */

@Configuration
@Getter
public class AppConfig {

    @Value("${service.url}")
    private String serviceUrl;
}
