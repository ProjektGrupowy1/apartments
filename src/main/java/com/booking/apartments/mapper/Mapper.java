package com.booking.apartments.mapper;

import com.booking.apartments.entity.*;
import com.booking.apartments.service.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.function.Function;

@Component
public class Mapper {

    @Autowired
    ManageTheHotelService manageTheHotelService;

    @Autowired
    ReserveService reserveService;

    @Autowired
    ManageUsersService manageUsersService;

    @Autowired
    AuthenticationService authenticationService;



    @Getter
    @AllArgsConstructor
    public class NewUserMapper {
        private String name;
        private String lastname;
        private String street;
        private String city;
        private String state;
        private String postalCode;
        private String phone;
        private String email;
        private String password;
        private String profile;
        private Boolean enabled;
    }

    @Getter
    @AllArgsConstructor
    public static class UserMapper {
        private Integer idUser;
        private String name;
        private String lastname;
        private String street;
        private String city;
        private String state;
        private String postalCode;
        private String phone;
        private String email;
        private String password;
        private String profile;
        private boolean enabled;
    }

    @Getter
    @AllArgsConstructor
    public class NewHotelMapper {
        private String name;
        private String description;
        private String city;
        private String state;
        private String postalCode;
        private String street;
        private Integer rating;
        private boolean enabled;
    }

    @Getter
    @AllArgsConstructor
    public class HotelMapper {
        private Integer idHotel;
        private String name;
        private String description;
        private String city;
        private String state;
        private String postalCode;
        private String street;
        private Integer rating;
        private boolean enabled;
    }

    @Getter
    @AllArgsConstructor
    public class NewApartmentMapper {
        private String hotelName;
        private String name;
        private Integer size;
        private Float price;
        private String status;
    }

    @Getter
    @AllArgsConstructor
    public class ApartmentMapper {
        private Integer idApartment;
        private String hotelName;
        private String name;
        private Integer size;
        private Float price;
        private String status;
    }

    @Getter
    @AllArgsConstructor
    public class CustomerInformationAboutTheApartmentMapper {
        private Integer idApartment;
        private String hotelName;
        private String description;
        private String city;
        private String street;
        private Integer rating;
        private String name;
        private Integer size;
        private Float price;
        private String status;
    }

    @Getter
    @AllArgsConstructor
    public class BookingInformation {
        private Integer idReservation;
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate startDate;
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate endDate;
        private Float price;
        private Integer idApartment;
        private String apartamentName;
        private String hotelName;
        private String street;
        private String city;
        private Integer idUser;
        private String status;
    }

    @Getter
    @AllArgsConstructor
    public class InformationForTheOwner {
        private Integer idReservation;
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate startDate;
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate endDate;
        private Float price;
        private Integer idApartment;
        private String apartamentName;
        private String hotelName;
        private String street;
        private String city;
        private Integer idUser;
        private String status;
        private String name;
        private String lastname;
        private String login;
    }

    public Function<HotelEntity, HotelMapper> mapTheHotel = new Function<HotelEntity, HotelMapper>() {
        public HotelMapper apply(HotelEntity hotelEntity) {
            CityEntity city = manageTheHotelService.getCityById(hotelEntity.getIdCity());
            return new HotelMapper(hotelEntity.getIdHotel(), hotelEntity.getName(), hotelEntity.getDescription(), city.getCityName(),
                    city.getState(), city.getPostalCode(), hotelEntity.getStreet(), hotelEntity.getRating(),hotelEntity.isEnabled());
        }
    };

    public Function<UserEntity, UserMapper> mapUser = new Function<UserEntity, UserMapper>() {
        public UserMapper apply(UserEntity entity) {
            CityEntity city = manageTheHotelService.getCityById(entity.getIdCity());
            ProfileEntity profileEntity = manageUsersService.getProfileById(entity.getIdProfile());
            return new UserMapper(entity.getIdUser(), entity.getName(), entity.getLastname(), entity.getStreet()
                    ,city.getCityName(), city.getState(), city.getPostalCode() ,entity.getPhone(), entity.getEmail()
                    ,entity.getPassword(),  profileEntity.getProfileName(),(entity.getEnabled() == 1 ? true : false)
                   );
        }
    };


    public Function<ApartmentEntity, ApartmentMapper> mapTheApartment = new Function<ApartmentEntity, ApartmentMapper>() {
        public ApartmentMapper apply(ApartmentEntity apartmentEntity) {
            return new ApartmentMapper(apartmentEntity.getIdApartment(), manageTheHotelService.getHotelName(apartmentEntity.getIdHotel()),
                    apartmentEntity.getName(), apartmentEntity.getSize(), apartmentEntity.getPrice(), apartmentEntity.getStatus());
        }
    };

    public Function<ApartmentEntity, CustomerInformationAboutTheApartmentMapper> customerInformationAboutTheApartment = new Function<ApartmentEntity, CustomerInformationAboutTheApartmentMapper>() {
        public CustomerInformationAboutTheApartmentMapper apply(ApartmentEntity apartment) {

            HotelEntity hotel = manageTheHotelService.getHotel(apartment.getIdHotel());
            String city = manageTheHotelService.getCityName(hotel.getIdCity());

            return new CustomerInformationAboutTheApartmentMapper(apartment.getIdApartment(), hotel.getName(), hotel.getDescription(),
                    city, hotel.getStreet(), hotel.getRating(), apartment.getName(), apartment.getSize(), apartment.getPrice(),
                    apartment.getStatus());
        }
    };

    public Function<HotelEntity, Integer> mapHotelsId = new Function<HotelEntity, Integer>() {
        public Integer apply(HotelEntity hotel) {
            return hotel.getIdHotel();
        }
    };

    public Function<ReservationEntity, BookingInformation> bookingInformation = new Function<ReservationEntity, BookingInformation>() {
        public BookingInformation apply(ReservationEntity reservation) {
            String apartmentName = manageTheHotelService.getApartment(reservation.getIdApartment()).getName();
            HotelEntity hotel = manageTheHotelService.getHotelNameByApartmentId(reservation.getIdApartment());
            String cityName = manageTheHotelService.getCityName(hotel.getIdCity());

            return new BookingInformation(reservation.getIdReservation(), reservation.getStartDate(), reservation.getEndDate(), reservation.getPrice(),
                    reservation.getIdApartment(), apartmentName, hotel.getName(), hotel.getStreet(), cityName, reservation.getIdUser(),reservation.getStatus());
        }
    };

    public Function<ReservationEntity, InformationForTheOwner> informationForTheOwner = new Function<ReservationEntity, InformationForTheOwner>() {
        public InformationForTheOwner apply(ReservationEntity reservation) {
            String apartmentName = manageTheHotelService.getApartment(reservation.getIdApartment()).getName();
            HotelEntity hotel = manageTheHotelService.getHotelNameByApartmentId(reservation.getIdApartment());
            String cityName = manageTheHotelService.getCityName(hotel.getIdCity());
            UserEntity user = authenticationService.getUserById(reservation.getIdUser());

            return new InformationForTheOwner(reservation.getIdReservation(), reservation.getStartDate(), reservation.getEndDate(), reservation.getPrice(),
                    reservation.getIdApartment(), apartmentName, hotel.getName(), hotel.getStreet(), cityName, reservation.getIdUser(),reservation.getStatus(),
                    user.getName(),user.getLastname(),user.getEmail());
        }
    };
}
