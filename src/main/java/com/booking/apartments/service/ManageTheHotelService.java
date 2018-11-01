package com.booking.apartments.service;

import com.booking.apartments.entity.HotelEntity;
import com.booking.apartments.repository.CityRepository;
import com.booking.apartments.repository.HotelRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ManageTheHotelService {

    HotelRepository hotelRepository;

    CityRepository cityRepository;

    public void addNewHotel(HotelEntity hotel) {
        hotelRepository.save(hotel);
    }

    public List<HotelEntity> getHotels(int idOwner) {
        return hotelRepository.getListOfHotelsByOwnerId(idOwner);
    }

    public String getCityName(int idCity){
        return cityRepository.getCityNameById(idCity).get(0).getCityName();
    }
}
