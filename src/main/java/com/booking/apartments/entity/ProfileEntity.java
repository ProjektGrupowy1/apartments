package com.booking.apartments.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "profiles")
public class ProfileEntity {

    @Id
    @Column(name="id_profile")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProfile;

    private String profileName;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "idProfile")
    private Set<UserEntity> user = new HashSet<>();

    public ProfileEntity() {}

    public ProfileEntity(String profileName) {
        this.profileName = profileName;
    }

    public Long getIdProfile() {
        return idProfile;
    }

    public void setIdProfile(Long idProfile) {
        this.idProfile = idProfile;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }
}
