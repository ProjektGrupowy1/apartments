package com.booking.apartments.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "profile")
public class ProfileEntity {

    @Id
    @Column(name="id_profile")
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int idProfile;

    @Column(name = "name")
    private String profileName;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "profile")
    private List<UserEntity> user ;

    public ProfileEntity() {}

    public ProfileEntity(String profileName) {
        this.profileName = profileName;
    }

    public int getIdProfile() {
        return idProfile;
    }

    public void setIdProfile(int idProfile) {
        this.idProfile = idProfile;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }
}
