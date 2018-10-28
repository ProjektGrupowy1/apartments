package com.booking.apartments.config;

import com.booking.apartments.entity.ProfileEntity;
import com.booking.apartments.repository.CityRepository;
import com.booking.apartments.repository.ProfileRepository;
import com.booking.apartments.repository.UserRepository;
import com.booking.apartments.service.AuthenticationService;
import com.booking.apartments.utility.Session;
import com.booking.apartments.utility.SessionBeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ViewResolver;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Configuration
public class Beans {

    @Scope(value = WebApplicationContext.SCOPE_SESSION, scopeName = "session")
    @Bean
    public Session session() {
        return new Session();
    }

    @Bean
    public static BeanFactoryPostProcessor beanFactoryPostProcessor() {
        return new SessionBeanFactoryPostProcessor();
    }

    @Bean
    public AuthenticationService authenticationService(UserRepository userRepository, ProfileRepository profileRepository, CityRepository cityRepository) {
        return new AuthenticationService(session(), userRepository, profileRepository, cityRepository);
    }

    @Bean
    public ViewResolver viewResolver() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setTemplateMode("XHTML");
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");

        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(templateResolver);

        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(engine);
        return viewResolver;
    }

    @Bean
    CommandLineRunner insertToDatabase(UserRepository userRepository, ProfileRepository profileRepository, CityRepository cityRepository) {
        return args -> {

            profileRepository.save(new ProfileEntity("Client"));
            profileRepository.save(new ProfileEntity("Owner"));
            profileRepository.save(new ProfileEntity("Admin"));

//            cityRepository.save(new CityEntity("Warszawa","PL","Mazowieckie","00-300"));

//            userRepository.save(new UserEntity("Tomasz", "Nowak", "user", "password", "Client", 1));
//            userRepository.save(new UserEntity("Jan", "Kowalski", "user2", "password", "Admin", 1));
//            userRepository.save(new UserEntity("Jan", "Kowalski", "user3", "$2a$04$qRz8rKuG9IjxHcIcLUAJzurefZj2Vy7.7k3cnJT74PGEyF2OIckNK", "Client", 1));
        };
    }

    @Bean
    public ServletContextInitializer initializer() {
        return servletContext -> servletContext.getSessionCookieConfig();
    }

}
