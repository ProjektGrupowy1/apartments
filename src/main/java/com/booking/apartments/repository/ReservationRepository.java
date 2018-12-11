package com.booking.apartments.repository;

import com.booking.apartments.entity.ReservationEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends CrudRepository<ReservationEntity, Integer> {

    @Query("select r.idApartment from ReservationEntity r where (r.startDate >= :startDate or r.endDate <= :endDate) and r.status in ('Approved','Waiting') ")
    List<Integer> findAllIdApartmentFromAGivenDateRange(@Param("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                                        @Param("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate);

    @Query("select r from ReservationEntity r where r.idApartment = :idApartment and (:startDate between r.startDate and r.endDate or :endDate between r.startDate and r.endDate " +
            "or r.startDate between :startDate and :endDate or r.endDate between :startDate and :endDate) and r.status in ('Approved','Waiting')")
    List<ReservationEntity> findAllIdApartmentFromAGivenDateRangeAndApartmentId(@Param("startDate")LocalDate startDate,
                                                                                @Param("endDate")LocalDate endDate,
                                                                                @Param("idApartment")Integer idApartment);

    @Query("select r from ReservationEntity r where r.idUser = :idUser and r.status = :status")
    List<ReservationEntity> findAllReservationByUserId(@Param("idUser") Integer idUser, @Param("status") String status);

    @Query("select r from ReservationEntity r where r.idApartment = :idApartment and r.status = :status")
    List<ReservationEntity> findAllReservationByApartmentId(@Param("idApartment") Integer idApartment, @Param("status") String status);
}
