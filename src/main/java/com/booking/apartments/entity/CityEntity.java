package com.booking.apartments.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "city")
public class CityEntity {
    @Id
    @Column(name = "id_city")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCity;

    @Column(name = "name")
    private String cityName;

    @Column(name = "country_code")
    private String countryCode;

    private String state;

    @Column(name = "postal_code")
    private String postalCode;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "city")
    private List<UserEntity> users ;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "city")
    public List<HotelEntity> hotels;

    public CityEntity(String cityName, String countryCode, String state, String postalCode) {
        this.cityName = cityName;
        this.countryCode = countryCode;
        this.state = state;
        this.postalCode = postalCode;
    }

    public int getIdCity() {
        return idCity;
    }

    public void setIdCity(int idCity) {
        this.idCity = idCity;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}
