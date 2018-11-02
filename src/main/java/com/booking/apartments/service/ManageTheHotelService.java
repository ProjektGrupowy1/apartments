package com.booking.apartments.service;

import com.booking.apartments.entity.ApartmentEntity;
import com.booking.apartments.entity.HotelEntity;
import com.booking.apartments.repository.ApartmentRepository;
import com.booking.apartments.repository.CityRepository;
import com.booking.apartments.repository.HotelRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ManageTheHotelService {

    HotelRepository hotelRepository;

    ApartmentRepository apartmentRepository;

    CityRepository cityRepository;

    public void addNewHotel(HotelEntity hotel) {
        hotelRepository.save(hotel);
    }

    public List<HotelEntity> getHotels(int idOwner) {
        return hotelRepository.getListOfHotelsByOwnerId(idOwner);
    }

    public List<ApartmentEntity> getApartments(int idHotel) {
        return apartmentRepository.getApartmentsByHotelId(idHotel);
    }
    public List<ApartmentEntity> getApartments(String hotelName) {
        return apartmentRepository.getApartmentsByHotelId(hotelRepository.getHotelByHotelName(hotelName).get(0).getIdHotel());
    }

    public String getCityName(int idCity){
        return cityRepository.getCityNameById(idCity).get(0).getCityName();
    }

    public HotelEntity getHotel(int idHotel) {
        return hotelRepository.getHotelById(idHotel).get(0);
    }

    public int getHotelId(String hotelName) {
        return hotelRepository.getHotelByHotelName(hotelName).get(0).getIdHotel();
    }

    public String getHotelName(int idHotel){
        return getHotel(idHotel).getName();
    }

    public void modyfyTheHotel(HotelEntity hotel) {
        hotelRepository.save(hotel);
    }

    public void deleteHotel(int idHotel) {
        hotelRepository.deleteById(idHotel);
    }

    public void addApartment(ApartmentEntity apartment) {
        apartmentRepository.save(apartment);
    }

    public ApartmentEntity getApartment(int idApartment) {
        return apartmentRepository.findById(idApartment).get();
    }

    public void modyfyTheApartment(ApartmentEntity apartment) {
        apartmentRepository.save(apartment);
    }

    public void deleteApartment(int idApartment) {
        apartmentRepository.deleteById(idApartment);
    }
}
