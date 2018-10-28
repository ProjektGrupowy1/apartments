package com.booking.apartments.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "profile")
public class ProfileEntity implements Serializable {

    @Id
    @Column(name="id_profile")
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int idProfile;

    @Column(name = "name")
    private String profileName;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "idProfile")
    private List<UserEntity> user ;

    public ProfileEntity(String profileName) {
        this.profileName = profileName;
    }

}
