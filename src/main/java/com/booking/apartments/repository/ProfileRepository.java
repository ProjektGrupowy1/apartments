package com.booking.apartments.repository;

import com.booking.apartments.entity.ProfileEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileRepository extends CrudRepository<ProfileEntity,Integer> {
    @Query("select p from ProfileEntity p where p.idProfile = :idProfile")
    List<ProfileEntity> getProfileById(@Param("idProfile") int idProfile);

    @Query("select p from ProfileEntity p where p.profileName = :profileName")
    List<ProfileEntity> getIdByProfileName(@Param("profileName") String profileName);
}
