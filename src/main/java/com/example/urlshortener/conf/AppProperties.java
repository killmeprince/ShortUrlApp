package com.example.urlshortener.conf;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private int defaultMaxClicks;
    private String defaultExpirationTime;

}
