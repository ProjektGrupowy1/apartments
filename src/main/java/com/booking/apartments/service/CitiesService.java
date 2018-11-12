package com.booking.apartments.service;

import com.booking.apartments.entity.CityEntity;
import com.booking.apartments.repository.CityRepository;
import com.booking.apartments.utility.Session;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

public class CitiesService {

    Session session;
    @Autowired
    CityRepository cityRepository;

    public CityEntity getCityById(int idCity) {
        return cityRepository.getCityById(idCity);
    }
}
