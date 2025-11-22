package com.reservation.reservation_server.dto.product;

import com.reservation.reservation_server.entity.Product;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProductResponseDto {

    private Long productId;
    private String name;
    private String description;
    private CategoryDto2 category; // Category 엔티티 대신 DTO 사용
    private Integer price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long storeId;
    private String status;
//    private String imageUrl;


    public static ProductResponseDto fromEntity(Product product) {
        return ProductResponseDto.builder()
                .productId(product.getProductId())
                .name(product.getName())
                .description(product.getDescription())
                .category(product.getCategory() != null
                        ? new CategoryDto2(product.getCategory().getId(), product.getCategory().getName())
                        : null)
                .price(product.getPrice())
                .storeId(product.getStore().getStoreId())
                .status(product.getStatus().name())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
//                .imageUrl(product.getImageUrl())
                .build();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CategoryDto2 {
        private Long id;
        private String name;
    }
}
