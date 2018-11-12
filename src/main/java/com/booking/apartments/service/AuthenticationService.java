package com.booking.apartments.service;

import com.booking.apartments.entity.ProfileEntity;
import com.booking.apartments.entity.UserEntity;
import com.booking.apartments.mapper.Mapper;
import com.booking.apartments.entity.UserDetailsModel;
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

@Service
@AllArgsConstructor
public class AuthenticationService implements UserDetailsService {

    private PasswordEncoder passwordEncoder;

    private Session session;

    private UserRepository userRepository;

    private ProfileRepository profileRepository;

    private CityRepository cityRepository;

    public UserEntity addNewUser(Mapper.NewUserMapper newUserMapper) {

        UserEntity user = new UserEntity();
        user.setName(newUserMapper.getName());
        user.setLastname(newUserMapper.getLastname());
        user.setStreet(newUserMapper.getStreet());

        user.setIdCity(getIdByCityName(newUserMapper.getCity()));
        user.setPhone(newUserMapper.getPhone());
        user.setEmail(newUserMapper.getEmail());
        user.setPassword(passwordEncoder.encode(newUserMapper.getPassword()));
        user.setIdProfile(getProfileId(newUserMapper.getProfile()));
        user.setEnabled(1);

        userRepository.save(user);
        return user;
    }

    public UserEntity getUserById(Integer idUser){
        return userRepository.getUserById(idUser).get(0);
    }

    public Mapper.UserMapper getUserByEmail(String email){

        UserEntity user = userRepository.getUserByEmail(email).get(0);
        String cityName = cityRepository.getCityNameById(user.getIdCity()).get(0).getCityName();
        String profileName = profileRepository.getProfileById(user.getIdProfile()).get(0).getProfileName();

        return new Mapper.UserMapper(user.getIdUser(),user.getName(), user.getLastname(), user.getStreet(),cityName,user.getPhone(),user.getEmail(),
                user.getPassword(),profileName,(user.getEnabled()==1));
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

        return cityRepository.findCityListByCityName(cityName).get(0).getIdCity();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserEntity user = userRepository.getUserByEmail(email).get(0);

        ProfileEntity profile = profileRepository.getProfileById(user.getIdProfile()).get(0);

        session.addParam("email", email);
        session.addParam("profile", profile.getProfileName());

        return new UserDetailsModel(user, profile);
    }

    public void modifyUser(Mapper.UserMapper userMapper) {

        UserEntity user = userRepository.getUserById(userMapper.getIdUser()).get(0);
        String password = user.getPassword();

        if(!password.equals(userMapper.getPassword())){
            password = passwordEncoder.encode(userMapper.getPassword());
        }

        user.setIdUser(userMapper.getIdUser());
        user.setName(userMapper.getName());
        user.setLastname(userMapper.getLastname());
        user.setStreet(userMapper.getStreet());

        user.setIdCity(getIdByCityName(userMapper.getCity()));
        user.setPhone(userMapper.getPhone());
        user.setEmail(userMapper.getEmail());
        user.setPassword(password);
        user.setIdProfile(getProfileId(userMapper.getProfile()));
        user.setEnabled(1);

        userRepository.save(user);
    }
}
