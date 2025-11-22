package com.reservation.reservation_server.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.reservation.reservation_server.common.ServiceStatus;
import com.reservation.reservation_server.entity.Product;
import com.reservation.reservation_server.entity.ReservationHdr;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProductRequestDto {

    private Long productId;
    private String name;
    private String description;
    private Long categoryId;
    private Integer price;
    private String imageUrl;

}
