package com.booking.apartments.config;

import com.booking.apartments.entity.UserEntity;
import com.booking.apartments.repository.UserRepository;
import com.booking.apartments.service.AuthenticationService;
import com.booking.apartments.utility.Session;
import com.booking.apartments.utility.SessionBeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.web.servlet.ViewResolver;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Import(DatabaseConfig.class)
@Configuration
public class Beans {

    @Scope(scopeName = "session")
    @Bean
    public Session session() {
        return new Session();
    }

    @Bean
    public static BeanFactoryPostProcessor beanFactoryPostProcessor() {
        return new SessionBeanFactoryPostProcessor();
    }

    @Bean
    public AuthenticationService authenticationService(UserRepository userRepository){
        return new AuthenticationService(userRepository);
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
    CommandLineRunner insertToDatabase(UserRepository userRepository) {
        return args -> {
            userRepository.save(new UserEntity("Tomasz", "Nowak", "user", "password", "Client", 1));
            userRepository.save(new UserEntity("Jan", "Kowalski", "user2", "password", "Admin", 1));
            userRepository.save(new UserEntity("Jan", "Kowalski", "user3", "$2a$04$qRz8rKuG9IjxHcIcLUAJzurefZj2Vy7.7k3cnJT74PGEyF2OIckNK", "Client", 1));
        };
    }
}
