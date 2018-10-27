package com.booking.apartments.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "apartment")
public class ApartmentEntity {

    @Id
    @Column(name = "id_apartment")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idApartment;

    @Column(name = "id_hotel")
    private int idHotel;

    private String name;

    private int size;

    private float price;

    private int status;

    @JoinColumn(name = "id_hotel", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    public HotelEntity hotel;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "apartment")
    public List<ReservationEntity> reservations;


    public ApartmentEntity(int idApartment, int idHotel, String name, int size, float price, int status) {
        this.idApartment = idApartment;
        this.idHotel = idHotel;
        this.name = name;
        this.size = size;
        this.price = price;
        this.status = status;
    }

    public int getIdApartment() {
        return idApartment;
    }

    public void setIdApartment(int idApartment) {
        this.idApartment = idApartment;
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

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
