package com.booking.apartments.service;

import com.booking.apartments.entity.UserEntity;
import com.booking.apartments.mapper.Mapper;
import com.booking.apartments.repository.CityRepository;
import com.booking.apartments.repository.UserRepository;
import com.booking.apartments.utility.Session;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ManageUsersService {
    Session session;
    UserRepository userRepository;
    CityRepository cityRepository;

    public List<UserEntity> getAllUsers() {
        return userRepository.getAllUsers();

    }

    public UserEntity getUserById(int idUser) {
        return userRepository.getUserById(idUser).get(0);
    }


    public void deleteUser(int idUser) {
        userRepository.deleteById(idUser);
    }

    public void modifyUser(Mapper.UserMapper userMapper) {
        UserEntity user=  getUserById(userMapper.getIdUser());

        user.setIdCity( cityRepository.findCityListByCityName(userMapper.getCity()).get(0).getIdCity());
        user.setEmail(userMapper.getEmail());
        user.setLastname(userMapper.getLastname());
        user.setPhone( userMapper.getPhone());
        user.setStreet(userMapper.getStreet());

        userRepository.save(user);
    }
}
