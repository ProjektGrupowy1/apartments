package com.booking.apartments.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;

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

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date_start")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date_end")
    private LocalDate endDate;

    private float price;

    @Column(name = "id_apartment")
    private int idApartment;

    @Column(name = "id_user")
    private int idUser;

    private String status;

    @JoinColumn(name = "id_user", referencedColumnName = "id_user", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    public UserEntity user;

    @JoinColumn(name = "id_apartment", referencedColumnName = "id_apartment", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    public ApartmentEntity apartment;

    public ReservationEntity(LocalDate startDate, LocalDate endDate, float price, int idApartment, int idUser, String status) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.idApartment = idApartment;
        this.idUser = idUser;
        this.status = status;
    }
}
