package com.booking.apartments.service;

import com.booking.apartments.entity.ProfileEntity;
import com.booking.apartments.entity.UserEntity;
import com.booking.apartments.model.UserDetailsModel;
import com.booking.apartments.repository.CityRepository;
import com.booking.apartments.repository.ProfileRepository;
import com.booking.apartments.repository.UserRepository;
import com.booking.apartments.utility.Session;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationService implements UserDetailsService {

    Session session;

    UserRepository userRepository;

    ProfileRepository profileRepository;

    CityRepository cityRepository;

    public UserEntity addNewUser(UserEntity user) {
        userRepository.save(user);
        return user;
    }

    public int getUserId(String email) {

        UserEntity user = userRepository.getUserByEmail(email).get(0);

        return user.getIdUser();
    }

    public String getUserProfile(String email) {

        UserEntity user = userRepository.getUserByEmail(email).get(0);

        return profileRepository.getProfileById(user.getIdProfile()).get(0).getProfileName();
    }

    public int getProfileId(String profileName) {
        return profileRepository.getIdByProfileName(profileName).get(0).getIdProfile();
    }

    public int getIdByCityName(String cityName) {

        return cityRepository.getIdByCityName(cityName).get(0).getIdCity();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserEntity user = userRepository.getUserByEmail(email).get(0);

        ProfileEntity profile = profileRepository.getProfileById(user.getIdProfile()).get(0);

        session.addParam("email", email);

        return new UserDetailsModel(user, profile);
    }
}
