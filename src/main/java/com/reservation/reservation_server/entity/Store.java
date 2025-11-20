package com.reservation.reservation_server.entity;

import com.reservation.reservation_server.common.RoleType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "stores")
public class Store {

    @Id
    @Column(name = "store_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeId;

    private String email;

    private String password;

    private String name;

    @Column(name = "owner_name")
    private String ownerName;

    @Column(name = "phone_number")
    private String phoneNumber;

    private String address;

    private String description;

    private String category;

    @Column(name = "biz_registration_num")
    private String bizRegistrationNum;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @CreationTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @OneToMany(mappedBy = "store")
    private List<StoreOperationHour> operationHours;

    @OneToMany(mappedBy = "store")
    private List<Product> product;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private RoleType role;

    // getters and setters
}
