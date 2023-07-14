package com.neodohae_spring_boot.neodohae_spring_boot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:env.properties")
public class PropertyConfig {
}
