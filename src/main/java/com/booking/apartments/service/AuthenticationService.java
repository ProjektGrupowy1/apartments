package com.booking.apartments.service;

import com.booking.apartments.entity.UserEntity;
import com.booking.apartments.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationService {

    UserRepository userRepository;

    public UserEntity addNewUser(UserEntity user) {
        userRepository.save(user);
        return user;
    }

    public String getUserRole(String username) {
        return userRepository.getUserByLogin(username).get(0).getRole();
    }
}
