package com.library.task.shared.config;

import com.library.task.shared.dtos.auth.UserDto;
import com.library.task.models.User;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Define a custom mapping for User to UserDto
        modelMapper.addMappings(new PropertyMap<User, UserDto>() {
            @Override
            protected void configure() {
                map().setRole(source.getRole().getName());
            }
        });
        return modelMapper;
    }
}