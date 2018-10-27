package com.booking.apartments.entity;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "reservation")
public class ReservationEntity {

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


    @JoinColumn(name = "id_user", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    public UserEntity user;

    @JoinColumn(name = "id_apartment", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    public ApartmentEntity apartment;

    public ReservationEntity() {

    }

    public ReservationEntity(Date startDate, Date endDate, float price, int idApartment, int idUser) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.idApartment = idApartment;
        this.idUser = idUser;
    }

    public int getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getIdApartment() {
        return idApartment;
    }

    public void setIdApartment(int idApartment) {
        this.idApartment = idApartment;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }
}
