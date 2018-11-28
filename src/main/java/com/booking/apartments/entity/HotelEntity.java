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
@Table(name = "hotel")
public class HotelEntity implements Serializable {

    @Id
    @Column(name = "id_hotel")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idHotel;

    @Column(name = "name", unique = true)
    private String name;

    private int rating;

    private String description;

    private boolean enabled;

    @Column(name = "id_owner")
    private int idOwner;

    @Column(name = "id_city")
    private int idCity;

    @JoinColumn(name = "id_city", referencedColumnName = "id_city" , insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    public CityEntity city;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "idHotel")
    public List<ApartmentEntity> apartments;

    private String street;

    @JoinColumn(name = "id_owner", referencedColumnName = "id_user", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    public UserEntity owner;

    public HotelEntity(String name, int rating, String description, int idOwner, int idCity, String street) {
        this.name = name;
        this.rating = rating;
        this.description = description;
        this.idOwner = idOwner;
        this.idCity = idCity;
        this.street = street;
    }

}
