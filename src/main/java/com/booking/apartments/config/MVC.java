package com.booking.apartments.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class MVC extends WebMvcConfigurationSupport {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("home");
        registry.addViewController("/sign_in").setViewName("registration");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/search_engine").setViewName("search_engine");
        registry.addViewController("/manage_hotels").setViewName("manage_hotels");
        registry.addViewController("/manage_account").setViewName("manage_account");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(
                "/css/**", "/img/**")
                .addResourceLocations(
                        "classpath:/static/css/",
                        "classpath:/static/img/");
    }

}
