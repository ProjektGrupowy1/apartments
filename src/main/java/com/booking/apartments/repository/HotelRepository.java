package com.booking.apartments.repository;

import com.booking.apartments.entity.HotelEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends CrudRepository<HotelEntity,Integer> {

    @Query("select h from HotelEntity h where h.idOwner = :idOwner")
    List<HotelEntity> findListOfHotelsByOwnerId(@Param("idOwner") int idOwner);

    @Query("select h from HotelEntity h")
    List<HotelEntity> findAllHotels();

    @Query("select h from HotelEntity h where h.idHotel = :idHotel")
    List<HotelEntity> findHotelById(@Param("idHotel") int idHotel);

    @Query("select h from HotelEntity h where h.name = :hotelName")
    List<HotelEntity> findHotelByHotelName(@Param("hotelName") String hotelName);

    @Query("select h.idHotel from HotelEntity h where h.idCity = :idCity")
    List<Integer> findListOfHotelsByCityId(@Param("idCity") int idCity);

}
