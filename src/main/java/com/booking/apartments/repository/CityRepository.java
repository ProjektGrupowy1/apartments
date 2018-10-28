package com.booking.apartments.repository;

import com.booking.apartments.entity.CityEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository  extends CrudRepository<CityEntity,Long> {

    @Query("select c from CityEntity c where c.cityName = :name")
    List<CityEntity> getIdByCityName(@Param("name") String name);
}
