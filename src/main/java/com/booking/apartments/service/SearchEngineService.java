package com.booking.apartments.service;

import com.booking.apartments.entity.ApartmentEntity;
import com.booking.apartments.entity.HotelEntity;
import com.booking.apartments.mapper.Mapper;
import com.booking.apartments.repository.ApartmentRepository;
import com.booking.apartments.repository.CityRepository;
import com.booking.apartments.repository.HotelRepository;
import com.booking.apartments.repository.ReservationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SearchEngineService {

    private Mapper mapper;

    private HotelRepository hotelRepository;

    private ApartmentRepository apartmentRepository;

    private CityRepository cityRepository;

    private ReservationRepository reservationRepository;

    public List<ApartmentEntity> findApartments() {
        return (List<ApartmentEntity>) apartmentRepository.findAll();
    }

    public List<ApartmentEntity> findApartmentsThatMeetTheCriteria(String city, String hotelName, LocalDate startDate, LocalDate endDate) {

        Integer idCity = null;
        List<HotelEntity> listOfHotels = (List<HotelEntity>) hotelRepository.findAll();
        List<Integer> listOfHotelsId = listOfHotels.stream().map(mapper.mapHotelsId).collect(Collectors.toList());
        List<Integer> reservedIdApartments = null;
        List<ApartmentEntity> apartments = (List<ApartmentEntity>) apartmentRepository.findAll();

//        List<ReservationEntity> ra = reservationRepository.findAllIdApartmentFromAGivenDateRange();


        if (city != null && !city.isEmpty()) {
            idCity = cityRepository.findCityListByCityName(city).get(0).getIdCity();
            listOfHotelsId = hotelRepository.findListOfHotelsByCityId(idCity);
        }

        if (hotelName != null && !hotelName.isEmpty()) {
            listOfHotelsId.removeIf(h -> h != hotelRepository.getHotelByHotelName(hotelName).get(0).getIdHotel());
        }

        if (hotelName != null && !hotelName.isEmpty() || city != null && !city.isEmpty()) {

            List<ApartmentEntity> apartmentTemp = new ArrayList<>();

            for (ApartmentEntity apartment : apartments) {
                if (listOfHotelsId.stream().anyMatch(i -> i == apartment.getIdHotel())) {
                    apartmentTemp.add(apartment);
                }
            }
            apartments = apartmentTemp;
        }

        if (startDate != null && endDate != null) {
            List<ApartmentEntity> apartmentTemp = new ArrayList<>();

            reservedIdApartments = reservationRepository.findAllIdApartmentFromAGivenDateRange(startDate, endDate);

            for (ApartmentEntity apartment : apartments) {
                if (reservedIdApartments.stream().allMatch(r -> r != apartment.getIdApartment())) {
                    apartmentTemp.add(apartment);
                }
            }
            apartments = apartmentTemp;
        }
        return apartments;
    }
}
