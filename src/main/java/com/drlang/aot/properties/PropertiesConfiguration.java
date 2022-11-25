package com.drlang.aot.properties;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(DemoProperties.class)
public class PropertiesConfiguration {
    @Bean
    public ApplicationListener<ApplicationReadyEvent> readyApplicationListener(DemoProperties demoProperties) {
        return event -> System.out.println("this name is " + demoProperties.name());
    }

}

@ConfigurationProperties(prefix = "bootiful")
record DemoProperties(String name) {
}
