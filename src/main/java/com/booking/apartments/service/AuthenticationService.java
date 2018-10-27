package com.booking.apartments.service;

import com.booking.apartments.entity.UserEntity;
import com.booking.apartments.repository.ProfileRepository;
import com.booking.apartments.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationService {

    UserRepository userRepository;

    ProfileRepository profileRepository;

    public UserEntity addNewUser(UserEntity user) {
        userRepository.save(user);
        return user;
    }

    public String getUserProfile(String email) {

        UserEntity user = userRepository.getUserByEmail(email).get(0);

        return profileRepository.getProfileById(user.getIdProfile()).get(0).getProfileName();
    }

    public int getProfileId(String profileName) {
        return profileRepository.getIdByProfileName(profileName).get(0).getIdProfile();
    }
}
