package com.booking.apartments.mapper;

import com.booking.apartments.entity.ApartmentEntity;
import com.booking.apartments.entity.HotelEntity;
import com.booking.apartments.entity.ReservationEntity;
import com.booking.apartments.service.ManageTheHotelService;
import com.booking.apartments.service.ReserveService;
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

    @Getter
    @AllArgsConstructor
    public class NewUser {
        private String name;
        private String lastname;
        private String street;
        private String city;
        private String phone;
        private String email;
        private String password;
        private String profile;
    }

    @Getter
    @AllArgsConstructor
    public class NewHotelMapper {
        private String name;
        private String description;
        private String city;
        private String street;
        private int rating;
    }

    @Getter
    @AllArgsConstructor
    public class HotelMapper {
        private int idHotel;
        private String name;
        private String description;
        private String city;
        private String street;
        private int rating;
    }

    @Getter
    @AllArgsConstructor
    public class NewApartmentMapper {
        private String hotelName;
        private String name;
        private int size;
        private float price;
        private int status;
    }

    @Getter
    @AllArgsConstructor
    public class ApartmentMapper {
        private int idApartment;
        private String hotelName;
        private String name;
        private int size;
        private float price;
        private int status;
    }

    @Getter
    @AllArgsConstructor
    public class CustomerInformationAboutTheApartmentMapper {
        private int idApartment;
        private String hotelName;
        private String description;
        private String city;
        private String street;
        private int rating;
        private String name;
        private int size;
        private float price;
        private int status;
    }

    @Getter
    @AllArgsConstructor
    public class BookingInformation {
        private int idReservation;
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate startDate;
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate endDate;
        private float price;
        private int idApartment;
        private String apartamentName;
        private String hotelName;
        private String street;
        private String city;
        private int idUser;
        private String status;
    }

    public Function<HotelEntity, HotelMapper> mapTheHotel = new Function<HotelEntity, HotelMapper>() {
        public HotelMapper apply(HotelEntity hotelEntity) {
            return new HotelMapper(hotelEntity.getIdHotel(), hotelEntity.getName(), hotelEntity.getDescription(), manageTheHotelService.getCityName(hotelEntity.getIdCity()),
                    hotelEntity.getStreet(), hotelEntity.getRating());
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

}
