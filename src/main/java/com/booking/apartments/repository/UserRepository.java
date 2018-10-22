package com.booking.apartments.repository;

import com.booking.apartments.entity.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<UserEntity,Long> {

    @Query("select u from UserEntity u where u.email = :email")
    List<UserEntity> getUserByEmail(@Param("email") String email);

//    @Query("select u.username, u.password from UserEntity u where u.username = :username")
//    List<UserEntity> getUserByLogin(@Param("username") String username);
//
//    @Query("select u.username, u.role from UserEntity u where u.username = :username")
//    List<UserEntity> getRoleByLogin(@Param("username") String username);
}