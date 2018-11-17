package com.booking.apartments.service;

import com.booking.apartments.repository.ProfileRepository;
import com.booking.apartments.utility.Session;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProfileService {
    Session session;
    ProfileRepository profileRepository;

    public String getProfileNameById(int idProfile) {
        return profileRepository.findProfileById(idProfile).get(0).getProfileName();
    }
}
