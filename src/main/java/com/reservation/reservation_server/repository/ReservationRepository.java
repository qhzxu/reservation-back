package com.reservation.reservation_server.repository;

import com.reservation.reservation_server.dto.ReservationResponseDto;
import com.reservation.reservation_server.entity.ReservationDtl;
import com.reservation.reservation_server.entity.ReservationHdr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface ReservationRepository extends JpaRepository<ReservationHdr, Long> {

//    List<ReservationHdr> findAllByStoreId(Long storeId);
    List<ReservationHdr> findAllByDateAndTime(LocalDate date, LocalTime time);

    @Query("SELECT r FROM ReservationHdr r " +
            "JOIN FETCH r.user " +
            "JOIN FETCH r.product p " +
            "JOIN FETCH p.category " +
            "JOIN FETCH r.store " +
            "WHERE r.store.storeId = :storeId")
    List<ReservationHdr> findAllByStore_StoreId(Long storeId);
    List<ReservationHdr> findAllByUser_UserId(Long userId);
    Optional<ReservationHdr> findByReservationIdAndStore_StoreId(Long reservationId, Long storeId);
}
