package com.booking.apartments.repository;

import com.booking.apartments.entity.ApartmentEntity;
import com.booking.apartments.entity.HotelEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ApartmentRepository extends CrudRepository<ApartmentEntity, Integer> {

    @Query("select a from ApartmentEntity a where a.idHotel = :idHotel")
    List<ApartmentEntity> getApartmentsByHotelId(@Param("idHotel") int idHotel);

    @Query("select a from ApartmentEntity a where a.name = :name")
    List<ApartmentEntity> getApartmentsByHotelName(@Param("name") String name);

    @Query("select a from ApartmentEntity a where a.idApartment = :idApartment")
    List<ApartmentEntity> getApartmentsByApartmentId(@Param("idApartment") Integer idApartment);

}
