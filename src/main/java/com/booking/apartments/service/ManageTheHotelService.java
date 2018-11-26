package com.booking.apartments.service;

import com.booking.apartments.entity.ApartmentEntity;
import com.booking.apartments.entity.CityEntity;
import com.booking.apartments.entity.HotelEntity;
import com.booking.apartments.mapper.Mapper;
import com.booking.apartments.repository.ApartmentRepository;
import com.booking.apartments.repository.CityRepository;
import com.booking.apartments.repository.HotelRepository;
import com.booking.apartments.repository.UserRepository;
import com.booking.apartments.utility.Session;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ManageTheHotelService {

    private Session session;

    private HotelRepository hotelRepository;

    private ApartmentRepository apartmentRepository;

    private CityRepository cityRepository;

    private UserRepository userRepository;

    private AuthenticationService authenticationService;

    public List<HotelEntity> getHotels(int idOwner) {
        return hotelRepository.findListOfHotelsByOwnerId(idOwner);
    }

    public List<ApartmentEntity> getApartments(int idHotel) {
        return apartmentRepository.findApartmentsByHotelId(idHotel);
    }

    public List<ApartmentEntity> getApartments(String hotelName) {
        return apartmentRepository.findApartmentsByHotelId(hotelRepository.findHotelByHotelName(hotelName).get(0).getIdHotel());
    }

    public List<ApartmentEntity> getAllApartments() {
        return (List<ApartmentEntity>) apartmentRepository.findAll();
    }

    public String getCityName(int idCity) {
        return cityRepository.findCityNameById(idCity).get(0).getCityName();
    }

    public int getHotelId(String hotelName) {
        return hotelRepository.findHotelByHotelName(hotelName).get(0).getIdHotel();
    }

    public String getHotelName(int idHotel) {
        return getHotel(idHotel).getName();
    }

    public Boolean addNewHotel(Mapper.NewHotelMapper newHotelMapper) {

        HotelEntity hotel = new HotelEntity();
        hotel.setIdCity(authenticationService.checkIfCityExist(newHotelMapper.getCity(), "PL", newHotelMapper.getPostalCode(), newHotelMapper.getState()).getIdCity());
        hotel.setIdOwner(userRepository.findUserByEmail(session.getParam("email").toString()).get(0).getIdUser());
        hotel.setDescription(newHotelMapper.getDescription());
        hotel.setRating(newHotelMapper.getRating());
        hotel.setStreet(newHotelMapper.getStreet());
        hotel.setName(newHotelMapper.getName());

        hotelRepository.save(hotel);
        return true;
    }

    public void addNewApartment(Mapper.NewApartmentMapper newApartmentMapper) {

        ApartmentEntity apartment = new ApartmentEntity();

        apartment.setIdHotel(getHotelId(newApartmentMapper.getHotelName()));
        apartment.setName(newApartmentMapper.getName());
        apartment.setPrice(newApartmentMapper.getPrice());
        apartment.setSize(newApartmentMapper.getSize());
        apartment.setStatus(newApartmentMapper.getStatus());

        apartmentRepository.save(apartment);
    }

    public HotelEntity getHotel(int idHotel) {
        return hotelRepository.findHotelById(idHotel).get(0);
    }

    public ApartmentEntity getApartment(int idApartment) {
        return apartmentRepository.findById(idApartment).get();
    }

    public void modyfyTheHotel(Mapper.HotelMapper hotelMapper) {

        HotelEntity hotel = getHotel(hotelMapper.getIdHotel());

        hotel.setIdCity(authenticationService.checkIfCityExist(hotelMapper.getCity(), "PL", hotelMapper.getPostalCode(), hotelMapper.getState()).getIdCity());
        hotel.setIdOwner(userRepository.findUserByEmail(session.getParam("email").toString()).get(0).getIdUser());

        hotel.setDescription(hotelMapper.getDescription());
        hotel.setRating(hotelMapper.getRating());
        hotel.setStreet(hotelMapper.getStreet());
        hotel.setName(hotelMapper.getName());

        hotelRepository.save(hotel);
    }

    public void modyfyTheApartment(Mapper.ApartmentMapper apartmentMapper) {

        ApartmentEntity apartment = getApartment(apartmentMapper.getIdApartment());
        apartment.setSize(apartmentMapper.getSize());
        apartment.setStatus(apartmentMapper.getStatus());
        apartment.setPrice(apartmentMapper.getPrice());
        apartment.setName(apartmentMapper.getName());
        apartment.setIdHotel(getHotelId(apartmentMapper.getHotelName()));

        apartmentRepository.save(apartment);
    }

    public void deleteHotel(int idHotel) {
        hotelRepository.deleteById(idHotel);
    }

    public void deleteApartment(int idApartment) {
        apartmentRepository.deleteById(idApartment);
    }

    public HotelEntity getHotelNameByApartmentId(int idApartment) {
        return hotelRepository.findHotelById(apartmentRepository.findApartmentsByApartmentId(idApartment).get(0).getIdHotel()).get(0);
    }

    public void changeHotelStatus(int idHotel) {
        HotelEntity hotel = getHotel(idHotel);
        hotel.setEnabled(!hotel.isEnabled());
        hotelRepository.save(hotel);

    }

    public CityEntity getCityById(int idCity) {
            return cityRepository.findCityNameById(idCity).get(0);
    }
}
