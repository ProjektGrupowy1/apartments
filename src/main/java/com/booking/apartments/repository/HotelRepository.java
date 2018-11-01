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
    List<HotelEntity> getListOfHotelsByOwnerId(@Param("idOwner") int idOwner);
}
