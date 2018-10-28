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
@Table(name = "city")
public class CityEntity implements Serializable {
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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "idCity")
    private List<UserEntity> users ;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "idCity")
    public List<HotelEntity> hotels;

    public CityEntity(String cityName, String countryCode, String state, String postalCode) {
        this.cityName = cityName;
        this.countryCode = countryCode;
        this.state = state;
        this.postalCode = postalCode;
    }
}
