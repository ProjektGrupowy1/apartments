package com.booking.apartments.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "hotel")
public class HotelEntity {

    @Id
    @Column(name = "id_hotel")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idHotel;

    @Column(name = "name")
    private String name;

    private int rating;

    private String description;

    @Column(name = "id_owner")
    private int idOwner;

    @Column(name = "id_city")
    private int idCity;

    @JoinColumn(name = "id_city", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    public CityEntity city;


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "hotel")
    public List<ApartmentEntity> apartments;

    private String street;

    public HotelEntity() {

    }

    public HotelEntity(String name, int rating, String description, int idOwner, int idCity, String street) {
        this.name = name;
        this.rating = rating;
        this.description = description;
        this.idOwner = idOwner;
        this.idCity = idCity;
        this.street = street;
    }

    public int getIdHotel() {
        return idHotel;
    }

    public void setIdHotel(int idHotel) {
        this.idHotel = idHotel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIdOwner() {
        return idOwner;
    }

    public void setIdOwner(int idOwner) {
        this.idOwner = idOwner;
    }

    public int getIdCity() {
        return idCity;
    }

    public void setIdCity(int idCity) {
        this.idCity = idCity;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }
}
