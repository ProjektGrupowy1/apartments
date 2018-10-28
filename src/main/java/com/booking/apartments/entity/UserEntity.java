package com.booking.apartments.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "user")
public class UserEntity implements Serializable {

    @Id
    @Column(name="id_user")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idUser;

    private String name;

    private String lastname;

    @Column(unique = true)
    private String email;

    private String password;

    private String phone;

    private String street;

    @Column(name="id_profile")
    private int idProfile;

    @Column(name="id_city")
    private int idCity;

    private int enabled;

    @JoinColumn(name = "id_profile", referencedColumnName = "id_profile", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    public ProfileEntity profile;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "idUser")
    public List<ReservationEntity> userReservations;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "idOwner")
    public List<HotelEntity> hotels;

    @JoinColumn(name = "id_city", referencedColumnName = "id_city", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    public CityEntity city;

    public UserEntity(String name, String lastname, String email, String password, String phone, String street, int idProfile, int idCity, int enabled) {
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.street = street;
        this.idProfile = idProfile;
        this.idCity = idCity;
        this.enabled = enabled;
    }

    public UserEntity(UserEntity userEntity) {
        this.idUser = userEntity.getIdUser();
        this.idProfile = userEntity.getIdProfile();
        this.idCity = userEntity.getIdCity();
        this.name = userEntity.getName();
        this.lastname = userEntity.getLastname();
        this.email = userEntity.getEmail();
        this.password = userEntity.getPassword();
        this.phone = userEntity.getPhone();
        this.street = userEntity.getStreet();
        this.enabled = userEntity.getEnabled();
        this.profile = userEntity.getProfile();
        this.city = userEntity.getCity();
        this.userReservations = userEntity.getUserReservations();
        this.hotels = userEntity.getHotels();
    }
}
