package com.booking.apartments.service;

import com.booking.apartments.entity.UserEntity;
import com.booking.apartments.mapper.Mapper;
import com.booking.apartments.repository.CityRepository;
import com.booking.apartments.repository.ProfileRepository;
import com.booking.apartments.repository.UserRepository;
import com.booking.apartments.utility.Session;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ManageUsersService {
    Session session;
    UserRepository userRepository;
    CityRepository cityRepository;
    ProfileRepository profileRepository;
    PasswordEncoder passwordEncoder;

    public List<UserEntity> getAllUsers() {
        return userRepository.getAllUsers();

    }

    public UserEntity getUserById(int idUser) {
        return userRepository.getUserById(idUser).get(0);
    }


    public void deleteUser(int idUser) {
        userRepository.deleteById(idUser);
    }

    public void modifyUser(Mapper.User userMapper) {
        UserEntity user=  getUserById(userMapper.getIdUser());

        user.setIdCity( cityRepository.findCityListByCityName(userMapper.getCity()).get(0).getIdCity());
        user.setEmail(userMapper.getEmail());
        user.setLastname(userMapper.getLastname());
        user.setPhone( userMapper.getPhone());
        user.setName(userMapper.getName());
        user.setStreet(userMapper.getStreet());
        user.setIdProfile(profileRepository.getIdByProfileName(userMapper.getProfile()).get(0).getIdProfile());
        user.setPassword(passwordEncoder.encode(userMapper.getPassword()));
        user.setEnabled((userMapper.isEnabled()? 1 : 0));

        userRepository.save(user);
    }

    public void addNewUser(Mapper.NewUser newUser) {
        UserEntity user = new UserEntity();
        user.setIdCity( cityRepository.findCityListByCityName(newUser.getCity()).get(0).getIdCity());
        user.setEmail(newUser.getEmail());
        user.setLastname(newUser.getLastname());
        user.setName(newUser.getName());
        user.setPhone( newUser.getPhone());
        user.setStreet(newUser.getStreet());
        user.setIdProfile(profileRepository.getIdByProfileName(newUser.getProfile()).get(0).getIdProfile());
        user.setPassword(passwordEncoder.encode(newUser.getPassword()));
        user.setEnabled((newUser.isEnabled()? 1 : 0));
        userRepository.save(user);

    }
}
