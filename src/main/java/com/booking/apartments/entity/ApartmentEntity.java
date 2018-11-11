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
@Table(name = "apartment")
public class ApartmentEntity implements Serializable {

    @Id
    @Column(name = "id_apartment")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idApartment;

    @Column(name = "id_hotel")
    private int idHotel;

    private String name;

    private int size;

    private float price;

    private String status;

    @JoinColumn(name = "id_hotel", referencedColumnName = "id_hotel", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    public HotelEntity hotel;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "idApartment")
    public List<ReservationEntity> reservations;

    public ApartmentEntity(int idApartment, int idHotel, String name, int size, float price, String status) {
        this.idApartment = idApartment;
        this.idHotel = idHotel;
        this.name = name;
        this.size = size;
        this.price = price;
        this.status = status;
    }
}
