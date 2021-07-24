package com.ikub.intern.forum.Quora;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
//
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(
                "static/**",
                "/groups/styles/**",
                "/users/styles/**",
                "/styles/**")
                .addResourceLocations(
                        "classpath:/static/",
                        "classpath:/static/",
                        "classpath:/static/",
                        "classpath:/static/");
    }

}