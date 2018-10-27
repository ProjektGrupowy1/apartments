package com.booking.apartments.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

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
    private int cityId;

    private int enabled;

    @JoinColumn(name = "id_Profile", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    public ProfileEntity profile;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    public List<ReservationEntity> userReservations;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "owner")
    public List<HotelEntity> hotels;

    @JoinColumn(name = "id_city", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    public CityEntity city;

    public UserEntity() { }

    public UserEntity(String name, String lastname, String email, String password, String phone, String street, int idProfile, int cityId, int enabled) {
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.street = street;
        this.idProfile = idProfile;
        this.cityId = cityId;
        this.enabled = enabled;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getIdProfile() {
        return idProfile;
    }

    public void setIdProfile(int idProfile) {
        this.idProfile = idProfile;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }
}
