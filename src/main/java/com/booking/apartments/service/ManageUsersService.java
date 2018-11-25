package com.booking.apartments.service;

import com.booking.apartments.entity.UserEntity;
import com.booking.apartments.mapper.Mapper;
import com.booking.apartments.repository.CityRepository;
import com.booking.apartments.repository.ProfileRepository;
import com.booking.apartments.repository.UserRepository;
import com.booking.apartments.utility.Session;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ManageUsersService {
    private Session session;
    private UserRepository userRepository;
    private CityRepository cityRepository;
    private ProfileRepository profileRepository;
    private AuthenticationService authenticationService;
    private PasswordEncoder passwordEncoder;

    public List<UserEntity> getAllUsers() {
        return userRepository.findAllUsers();

    }

    public UserEntity getUserById(Integer idUser) {
        return userRepository.findUserById(idUser).get(0);
    }


    public void deleteUser(Integer idUser) {
        userRepository.deleteById(idUser);
    }

    public void modifyUser(Mapper.UserMapper userMapper) {
        UserEntity user=  getUserById(userMapper.getIdUser());

        user.setIdCity( authenticationService.checkIfCityExist(userMapper.getCity(),"PL",userMapper.getPostalCode(), userMapper.getState()).getIdCity());
        user.setEmail(userMapper.getEmail());
        user.setLastname(userMapper.getLastname());
        user.setPhone( userMapper.getPhone());
        user.setName(userMapper.getName());
        user.setStreet(userMapper.getStreet());
        user.setIdProfile(profileRepository.findProfileByProfileName(userMapper.getProfile()).get(0).getIdProfile());
        user.setPassword(passwordEncoder.encode(userMapper.getPassword()));
        user.setEnabled((userMapper.isEnabled()? 1 : 0));

        userRepository.save(user);
    }

    public void addNewUser(Mapper.NewUserMapper newUserMapper) {
        UserEntity user = new UserEntity();
        user.setIdCity( authenticationService.checkIfCityExist(newUserMapper.getCity(),"PL",newUserMapper.getPostalCode(), newUserMapper.getState()).getIdCity() );
        user.setEmail(newUserMapper.getEmail());
        user.setLastname(newUserMapper.getLastname());
        user.setName(newUserMapper.getName());
        user.setPhone( newUserMapper.getPhone());
        user.setStreet(newUserMapper.getStreet());
        user.setIdProfile(profileRepository.findProfileByProfileName(newUserMapper.getProfile()).get(0).getIdProfile());
        user.setPassword(passwordEncoder.encode(newUserMapper.getPassword()));
        user.setEnabled((newUserMapper.isEnabled()? 1 : 0));
        userRepository.save(user);

    }
}
