package com.booking.apartments.service;

import com.booking.apartments.entity.CityEntity;
import com.booking.apartments.entity.ProfileEntity;
import com.booking.apartments.entity.UserDetailsModel;
import com.booking.apartments.entity.UserEntity;
import com.booking.apartments.mapper.Mapper;
import com.booking.apartments.repository.CityRepository;
import com.booking.apartments.repository.ProfileRepository;
import com.booking.apartments.repository.UserRepository;
import com.booking.apartments.utility.Session;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AuthenticationService implements UserDetailsService {

    private PasswordEncoder passwordEncoder;

    private Session session;

    private UserRepository userRepository;

    private ProfileRepository profileRepository;

    private CityRepository cityRepository;

    public UserEntity addNewUser(Mapper.NewUserMapper newUserMapper) {

        CityEntity city = checkIfCityExist(newUserMapper.getCity(), "PL" ,newUserMapper.getPostalCode(), newUserMapper.getState());

        UserEntity user = new UserEntity();
        user.setName(newUserMapper.getName());
        user.setLastname(newUserMapper.getLastname());
        user.setStreet(newUserMapper.getStreet());
        user.setIdCity(city.getIdCity());
        user.setPhone(newUserMapper.getPhone());
        user.setEmail(newUserMapper.getEmail());
        user.setPassword(passwordEncoder.encode(newUserMapper.getPassword()));
        user.setIdProfile(getProfileId(newUserMapper.getProfile()));
        user.setEnabled(1);

        userRepository.save(user);
        return user;
    }

    public UserEntity getUserById(Integer idUser) {
        return userRepository.findUserById(idUser).get(0);
    }

    public Mapper.UserMapper getUserByEmail(String email) {

        UserEntity user = userRepository.findUserByEmail(email).get(0);
        CityEntity city = cityRepository.findCityNameById(user.getIdCity()).get(0);
        String profileName = profileRepository.findProfileById(user.getIdProfile()).get(0).getProfileName();

        return new Mapper.UserMapper(user.getIdUser(), user.getName(), user.getLastname(), user.getStreet(), city.getCityName(), city.getState(),
                city.getPostalCode(), user.getPhone(), user.getEmail(), user.getPassword(), profileName, (user.getEnabled() == 1));
    }

    public Integer getUserId(String email) {

        UserEntity user = userRepository.findUserByEmail(email).get(0);

        return user.getIdUser();
    }

    public String getUserProfile(String email) {

        UserEntity user = userRepository.findUserByEmail(email).get(0);

        return profileRepository.findProfileById(user.getIdProfile()).get(0).getProfileName();
    }

    public Integer getProfileId(String profileName) {
        return profileRepository.findProfileByProfileName(profileName).get(0).getIdProfile();
    }

    public Integer getIdByCityName(String cityName) {

        return cityRepository.findCityListByCityName(cityName).get(0).getIdCity();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserEntity user = userRepository.findUserByEmail(email).get(0);

        ProfileEntity profile = profileRepository.findProfileById(user.getIdProfile()).get(0);

        session.addParam("email", email);
        session.addParam("profile", profile.getProfileName());

        return new UserDetailsModel(user, profile);
    }

    CityEntity checkIfCityExist(String cityName, String countryCode ,String postalCode, String state){
        List<CityEntity> cities = cityRepository.findCityListByCityName(cityName);
        CityEntity city = null;

        if(!cities.isEmpty() && cities.stream().anyMatch(c -> c.getCityName().contains(cityName) &&
                c.getPostalCode().contains(postalCode) && c.getState().contains(state))){

            city = cityRepository.findCityListByCityName(cityName).stream()
                    .filter(c -> c.getCityName().contains(cityName) && c.getPostalCode().contains(postalCode) &&
                            c.getState().contains(state)).collect(Collectors.toList()).get(0);
        }
        else {
            city = addNewCity(cityName, countryCode, postalCode, state);
            cityRepository.save(city);
        }
        return city;
    }

    private CityEntity addNewCity(String cityName, String countryCode, String postalCode, String state) {
        CityEntity city = new CityEntity();
        city.setCityName(cityName);
        city.setCountryCode(countryCode);  // dla Polski
        city.setState(state);
        city.setPostalCode(postalCode);
        return city;
    }

    public void modifyUser(Mapper.UserMapper userMapper) {

        UserEntity user = userRepository.findUserById(userMapper.getIdUser()).get(0);
        String password = user.getPassword();
        CityEntity city = checkIfCityExist(userMapper.getCity(), "PL" , userMapper.getPostalCode(), userMapper.getState());

        user.setIdUser(userMapper.getIdUser());
        user.setName(userMapper.getName());
        user.setLastname(userMapper.getLastname());
        user.setStreet(userMapper.getStreet());

        user.setIdCity(city.getIdCity());
        user.setPhone(userMapper.getPhone());
        user.setEmail(userMapper.getEmail());
        user.setPassword(password);
        user.setIdProfile(getProfileId(userMapper.getProfile()));
        user.setEnabled(1);

        userRepository.save(user);
    }
}
