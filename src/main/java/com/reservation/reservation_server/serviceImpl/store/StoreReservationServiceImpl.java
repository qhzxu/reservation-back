package com.reservation.reservation_server.serviceImpl.store;
import com.reservation.reservation_server.common.ReservationStatus;
import com.reservation.reservation_server.dto.ReservationFormDto;
import com.reservation.reservation_server.dto.ReservationResponseDto;
import com.reservation.reservation_server.dto.ReservationSaveResult;
import com.reservation.reservation_server.dto.ReservationStatusUpdateRequest;
import com.reservation.reservation_server.entity.ReservationHdr;
import com.reservation.reservation_server.repository.ReservationRepository;
import com.reservation.reservation_server.service.store.StoreReservationService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StoreReservationServiceImpl implements StoreReservationService {

    private final ReservationRepository reservationRepository;

    @Autowired
    public StoreReservationServiceImpl(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }


    @Override
    public List<ReservationResponseDto> getReservations(Long storeId) {
        List<ReservationHdr> reservations = reservationRepository.findAllByStore_StoreId(storeId);
        // N+1 발생 가능
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
    public ReservationResponseDto getReservationDetail(Long storeId, Long reservationId) {
        Optional<ReservationHdr> reservationOpt = reservationRepository.findByReservationIdAndStore_StoreId(reservationId, storeId);

        ReservationHdr reservation = reservationOpt.orElseThrow(() ->
                new EntityNotFoundException("해당 예약이 없습니다. reservationId=" + reservationId + ", storeId=" + storeId));

        ReservationResponseDto dto = new ReservationResponseDto();
        dto.setReservationId(reservation.getReservationId());
        dto.setDate(reservation.getDate());
        dto.setTime(reservation.getTime());
        dto.setStatus(reservation.getStatus() != null ? reservation.getStatus().name() : null);
        dto.setIsActive(reservation.getIsActive());

        if (reservation.getUser() != null) {
            dto.setUserId(reservation.getUser().getUserId());
            dto.setUserName(reservation.getUser().getName());
        }
        if (reservation.getProduct() != null) {
            dto.setProductId(reservation.getProduct().getProductId());
            dto.setProductName(reservation.getProduct().getName());
        }
        if (reservation.getStore() != null) {
            dto.setStoreId(reservation.getStore().getStoreId());
        }
        dto.setCreatedAt(reservation.getCreatedAt());
        dto.setUpdatedAt(reservation.getUpdatedAt());

        return dto;
    }

    @Override
    public ReservationSaveResult createReservation(ReservationFormDto dto) {
        return null;
    }


    @Transactional
    public void updateReservation(ReservationStatusUpdateRequest request) {
        ReservationHdr reservation = reservationRepository.findById(request.getReservationId())
                .orElseThrow(() -> new RuntimeException("예약을 찾을 수 없습니다."));

        reservation.setStatus(request.getStatus()); // ReservationStatus 적용

        reservationRepository.save(reservation);
    }


    @Override
    public void cancelReservation(Long reservationId, Long storeId) {
        Optional<ReservationHdr> reservationOpt = reservationRepository.findByReservationIdAndStore_StoreId(reservationId, storeId);

        ReservationHdr reservation = reservationOpt.orElseThrow(() ->
                new EntityNotFoundException("해당 예약이 없습니다. reservationId=" + reservationId + ", storeId=" + storeId));

        // 예약 상태 변경 (Enum, boolean 등 실제 필드에 맞게 변경)
        reservation.setStatus(ReservationStatus.CANCELLED);
        reservation.setIsActive(false);  // 필요 시 활성 상태도 변경

        // 변경 내역 저장
        reservationRepository.save(reservation);
    }
}
