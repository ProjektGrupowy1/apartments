package com.booking.apartments.config;

import com.booking.apartments.entity.CityEntity;
import com.booking.apartments.entity.ProfileEntity;
import com.booking.apartments.mapper.Mapper;
import com.booking.apartments.repository.*;
import com.booking.apartments.service.*;
import com.booking.apartments.utility.Session;
import com.booking.apartments.utility.SessionBeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    public AuthenticationService authenticationService(PasswordEncoder passwordEncoder, UserRepository userRepository, ProfileRepository profileRepository, CityRepository cityRepository) {
        return new AuthenticationService(passwordEncoder, session(), userRepository, profileRepository, cityRepository);
    }

    @Bean
    public ManageTheHotelService manageTheHotelService(HotelRepository hotelRepository, ApartmentRepository apartmentRepository, CityRepository cityRepository, UserRepository userRepository, AuthenticationService authenticationService) {
        return new ManageTheHotelService(session(), hotelRepository, apartmentRepository, cityRepository, userRepository, authenticationService );
    }

    @Bean
    public SearchEngineService searchEngineService(HotelRepository hotelRepository, ApartmentRepository apartmentRepository,
                                                   CityRepository cityRepository, ReservationRepository reservationRepository){
        return new SearchEngineService(mapper(), hotelRepository,apartmentRepository,cityRepository, reservationRepository);
    }

    @Bean
    public ReserveService reserveService(AuthenticationService authenticationService, ManageTheHotelService manageTheHotelService, ReservationRepository reservationRepository){
        return new ReserveService(mapper(), session(),authenticationService,manageTheHotelService,reservationRepository);
    }

    @Bean
    public Mapper mapper() {
        return new Mapper();
    }

    @Bean
    public ViewResolver viewResolver() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setTemplateMode("XHTML");
        templateResolver.setCacheable(false);
        templateResolver.setPrefix("classpath:/templates/");
        templateResolver.setSuffix(".html");

        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(templateResolver);

        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(engine);
        return viewResolver;
    }

    @Bean
    CommandLineRunner insertToDatabase(UserRepository userRepository, ProfileRepository profileRepository, CityRepository cityRepository, HotelRepository hotelRepository) {
        return args -> {

            profileRepository.save(new ProfileEntity("Client"));
            profileRepository.save(new ProfileEntity("Owner"));
            profileRepository.save(new ProfileEntity("Admin"));

            cityRepository.save(new CityEntity("Warszawa","PL","Mazowieckie","00-300"));
//
//            userRepository.save(new UserEntity("Agnieszka", "Sz", "aga@gmail.com", "$2a$10$nQtRtmS45kHNjfeycb44vux3P0eBLWqh5c7wGWR/NKRFzQer9HHBC", "555555555", "Waryńskiego", 2, 1, 1));
//            userRepository.save(new UserEntity("Agnieszka", "Sz", "aga@gmail.com", "$2a$04$qRz8rKuG9IjxHcIcLUAJzurefZj2Vy7.7k3cnJT74PGEyF2OIckNK", "555555555", "Waryńskiego", 2, 1, 1));

//            hotelRepository.save(new HotelEntity("Porażka", 4,"Porażka to piękny hotel !", 1, 1, "ul. Nijaka"));

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
