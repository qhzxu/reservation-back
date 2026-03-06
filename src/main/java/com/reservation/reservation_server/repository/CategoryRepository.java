package com.reservation.reservation_server.repository;

import com.reservation.reservation_server.entity.Category;
import com.reservation.reservation_server.entity.Product;
import com.reservation.reservation_server.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findAllBy();


    // storeId가 아닌 Store 엔티티로 조회
    List<Category> findAllByStoreAndIsActiveTrue(Store store);

    // 반환 타입을 Optional<Category>로 지정
    Optional<Category> findByIdAndStoreAndIsActiveTrue(Long categoryId, Store store);
    Optional<Category> findByIdAndStore_StoreId(Long categoryId, Long storeId);
}

