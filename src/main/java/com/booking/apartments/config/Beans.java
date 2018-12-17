package com.booking.apartments.config;

import com.booking.apartments.entity.*;
import com.booking.apartments.mapper.Mapper;
import com.booking.apartments.repository.*;
import com.booking.apartments.service.AuthenticationService;
import com.booking.apartments.service.ManageTheHotelService;
import com.booking.apartments.service.ReserveService;
import com.booking.apartments.service.SearchEngineService;
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

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

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
        return new ManageTheHotelService(session(), hotelRepository, apartmentRepository, cityRepository, userRepository, authenticationService);
    }

    @Bean
    public SearchEngineService searchEngineService(HotelRepository hotelRepository, ApartmentRepository apartmentRepository,
                                                   CityRepository cityRepository, ReservationRepository reservationRepository) {
        return new SearchEngineService(mapper(), hotelRepository, apartmentRepository, cityRepository, reservationRepository);
    }

    @Bean
    public ReserveService reserveService(AuthenticationService authenticationService, ManageTheHotelService manageTheHotelService, ReservationRepository reservationRepository) {
        return new ReserveService(mapper(), session(), authenticationService, manageTheHotelService, reservationRepository);
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
    CommandLineRunner insertToDatabase(UserRepository userRepository, ProfileRepository profileRepository, CityRepository cityRepository,
                                       HotelRepository hotelRepository, ApartmentRepository apartmentRepository, ReservationRepository reservationRepository) {
        return args -> {

            List<UserEntity> owners = userRepository.findUserByEmail("owner@wp.pl");
            List<UserEntity> clients = userRepository.findUserByEmail("user@wp.pl");
            List<HotelEntity> hotels = hotelRepository.findHotelByHotelName("hotel");

            if (profileRepository.findProfileByProfileName("Client").isEmpty()) {
                profileRepository.save(new ProfileEntity("Client"));
            }
            if (profileRepository.findProfileByProfileName("Owner").isEmpty()) {
                profileRepository.save(new ProfileEntity("Owner"));
            }
            if (profileRepository.findProfileByProfileName("Admin").isEmpty()) {
                profileRepository.save(new ProfileEntity("Admin"));
            }
            if (cityRepository.findCityListByCityName("Warszawa").isEmpty()) {
                cityRepository.save(new CityEntity("Warszawa", "PL", "Polska", "00-300"));
            }
            if (userRepository.findUserByEmail("admin@wp.pl").isEmpty()) {
                userRepository.save(new UserEntity("admin", "admin", "admin@wp.pl", "$2a$10$fj1O5fqp8f/W7O9AWr4/aOeAEv0DN.VB.mtm76yOw7DPVcCEgJnwu", "admin", "admin", 3, 1, 1));
            }
            if (owners.isEmpty()) {
                UserEntity owner = new UserEntity("owner", "owner", "owner@wp.pl", "$2a$10$fj1O5fqp8f/W7O9AWr4/aOeAEv0DN.VB.mtm76yOw7DPVcCEgJnwu", "owner", "owner", 2, 1, 1);
                owners.add(owner);
                userRepository.save(owner);
            }
            if (clients.isEmpty()) {
                UserEntity client = new UserEntity("user", "user", "user@wp.pl", "$2a$10$50IcwAXaRKuVAnSbvlqPtecWTAfHaPLVVNXJrz0G.BrYPTFTtt5ru", "user", "user", 1, 1, 1);
                clients.add(client);
                userRepository.save(client);
            }
            if (hotels.isEmpty()) {
                HotelEntity hotel = new HotelEntity("hotel", 4, "hotel", owners.get(0).getIdUser(), 1, "hotel");
                hotels.add(hotel);
                hotelRepository.save(hotel);
                ApartmentEntity apartment = new ApartmentEntity(hotel.getIdHotel(), "apartment", 60, 200.0f, "Available");
                ApartmentEntity apartment2 = new ApartmentEntity(hotel.getIdHotel(), "apartment2", 60, 200.0f, "Available");
                apartmentRepository.save(apartment);
                apartmentRepository.save(apartment2);
                reservationRepository.save(new ReservationEntity(LocalDate.of(2018, Month.SEPTEMBER, 2),
                        LocalDate.of(2018, Month.SEPTEMBER, 10), 200.0f,
                        apartment.getIdApartment(), clients.get(0).getIdUser(), "Waiting"));
                reservationRepository.save(new ReservationEntity(LocalDate.of(2018, Month.SEPTEMBER, 20),
                        LocalDate.of(2018, Month.SEPTEMBER, 30), 200.0f,
                        apartment2.getIdApartment(), clients.get(0).getIdUser(), "Waiting"));
            }

        };
    }

    @Bean
    public ServletContextInitializer initializer() {
        return servletContext -> servletContext.getSessionCookieConfig();
    }

}
