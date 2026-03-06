package com.reservation.reservation_server.serviceImpl.user;


import com.reservation.reservation_server.common.ReservationStatus;
import com.reservation.reservation_server.dto.ReservationFormDto;
import com.reservation.reservation_server.dto.ReservationResponseDto;
import com.reservation.reservation_server.dto.ReservationSaveResult;
import com.reservation.reservation_server.entity.ReservationHdr;
import com.reservation.reservation_server.repository.ReservationRepository;
import com.reservation.reservation_server.repository.StoreRepository;
import com.reservation.reservation_server.service.store.StoreReservationService;
import com.reservation.reservation_server.service.user.UserReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserReservationServiceImpl implements UserReservationService {

    private final ReservationRepository reservationRepository;

    @Autowired
    public UserReservationServiceImpl(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }


    @Override
    public List<ReservationResponseDto> getReservations(Long userId) {

        List<ReservationHdr> reservations = reservationRepository.findAllByUser_UserId(userId);

        System.out.println(reservations.size() + "예약 건수");


        return reservations.stream()
                .map(reservation -> {
                    ReservationResponseDto dto = new ReservationResponseDto();
                    dto.setReservationId(reservation.getReservationId());
                    dto.setDate(reservation.getDate());
                    dto.setTime(reservation.getTime());
                    dto.setStatus(reservation.getStatus() != null ? reservation.getStatus().name() : null);
                    dto.setIsActive(reservation.getIsActive());

                    // User 정보
                    if (reservation.getUser() != null) {
                        dto.setUserId(reservation.getUser().getUserId());
                        dto.setUserName(reservation.getUser().getName());
                    }

                    // Product 정보
                    if (reservation.getProduct() != null) {
                        dto.setProductId(reservation.getProduct().getProductId());
                        dto.setProductName(reservation.getProduct().getName());
                    }

                    // Store 정보
                    if (reservation.getStore() != null) {
                        dto.setStoreId(reservation.getStore().getStoreId());
                    }

                    dto.setCreatedAt(reservation.getCreatedAt());
                    dto.setUpdatedAt(reservation.getUpdatedAt());

                    return dto;
                })
                .toList();
    }



    @Override
    public ReservationSaveResult createReservation(ReservationFormDto dto) {

        ReservationHdr reservationHdr = new ReservationHdr();
        ReservationSaveResult result = new ReservationSaveResult();

        reservationHdr.setDate(dto.getDate());
        reservationHdr.setTime(dto.getTime());
        reservationHdr.setUserId(dto.getUserId());
        reservationHdr.setProductId(dto.getProductId());
        reservationHdr.setStoreId(dto.getStoreId());
        reservationHdr.setStatus(ReservationStatus.PENDING);
        reservationHdr.setIsActive(true);


        int reservationCount = reservationRepository.findAllByDateAndTime(dto.getDate(),dto.getTime()).size();
        System.out.println(reservationCount + " = 시간,날짜 겹치는 날짜");

        // 저장
        if (reservationCount >= 2) { // 일단은 기본값 2로 잡고 그것보다 크면 반환
            result.setMessage("이미 예약이 전부 찼습니다.");
            result.setSuccess(Boolean.FALSE);
        }else{
            reservationHdr = reservationRepository.save(reservationHdr);
            result.setSuccess(Boolean.TRUE);
        }



//        responseDto.setReservationId(reservationHdr.getReservationId());
//        responseDto.setDate(reservationHdr.getDate());
//        responseDto.setTime(reservationHdr.getTime());
//        responseDto.setUserId(reservationHdr.getUserId());
//        responseDto.setStoreId(reservationHdr.getStore() != null ? reservationHdr.getStore().getStoreId() : null);
//        responseDto.setStatus(reservationHdr.getStatus().name());
//        responseDto.setCreatedAt(reservationHdr.getCreatedAt());
//        responseDto.setIsActive(reservationHdr.getIsActive());

        return result;
    }



}
