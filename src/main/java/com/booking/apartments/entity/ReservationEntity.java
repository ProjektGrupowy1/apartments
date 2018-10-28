package com.booking.apartments.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "reservation")
public class ReservationEntity implements Serializable {

    @Id
    @Column(name = "id_reservation")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idReservation;

    @Column(name = "date_start")
    private Date startDate;

    @Column(name = "date_end")
    private Date endDate;

    private float price;

    @Column(name = "id_apartment")
    private int idApartment;

    @Column(name = "id_user")
    private int idUser;

    @JoinColumn(name = "id_user", referencedColumnName = "id_user", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    public UserEntity user;

    @JoinColumn(name = "id_apartment", referencedColumnName = "id_apartment", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    public ApartmentEntity apartment;

    public ReservationEntity(Date startDate, Date endDate, float price, int idApartment, int idUser) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.idApartment = idApartment;
        this.idUser = idUser;
    }
}
