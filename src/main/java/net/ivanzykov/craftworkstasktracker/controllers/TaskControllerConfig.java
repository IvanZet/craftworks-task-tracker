package net.ivanzykov.craftworkstasktracker.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TaskControllerConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
