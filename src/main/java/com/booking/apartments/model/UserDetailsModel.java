package com.booking.apartments.model;

import com.booking.apartments.entity.ProfileEntity;
import com.booking.apartments.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class UserDetailsModel extends UserEntity implements UserDetails {

    String profile = null;

    public UserDetailsModel(final UserEntity userEntity, final ProfileEntity profileEntity){
        super(userEntity);
        profile = profileEntity.getProfileName();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(profile));
        grantedAuthorities.add(new SimpleGrantedAuthority("User"));

        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public String getUsername() {
        return super.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return super.getEnabled()==1;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return super.getEnabled()==1;
    }
}
