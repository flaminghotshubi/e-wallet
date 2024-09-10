package com.ewallet.config;

import org.json.simple.parser.JSONParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public JSONParser getParser() {
        return new JSONParser();
    }
}
