package com.reservation.reservation_server.serviceImpl.store;

import com.reservation.reservation_server.common.ServiceStatus;
import com.reservation.reservation_server.dto.CategoryDto;
import com.reservation.reservation_server.dto.product.ProductRequestDto;
import com.reservation.reservation_server.dto.product.ProductRequestDto;
import com.reservation.reservation_server.dto.product.ProductResponseDto;
import com.reservation.reservation_server.entity.Category;
import com.reservation.reservation_server.entity.Product;
import com.reservation.reservation_server.entity.QStore;
import com.reservation.reservation_server.entity.Store;
import com.reservation.reservation_server.repository.CategoryRepository;
import com.reservation.reservation_server.repository.StoreProductRepository;
import com.reservation.reservation_server.repository.StoreRepository;
import com.reservation.reservation_server.service.store.StoreProductService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.reservation.reservation_server.entity.QStore.store;

@Service
public class StoreProductServiceImpl implements StoreProductService {

    private final StoreProductRepository storeProductRepository;
    private final CategoryRepository categoryRepository;
    private final StoreRepository storeRepository;

    @Autowired
    public StoreProductServiceImpl(StoreProductRepository storeProductRepository, CategoryRepository categoryRepository, StoreRepository storeRepository) {
        this.storeProductRepository = storeProductRepository;
        this.categoryRepository = categoryRepository;
        this.storeRepository = storeRepository;
    }



    @Override
    public List<ProductResponseDto> getProduct(Long storeId) {
        List<Product> products = storeProductRepository.findAllByStore_StoreId(storeId);

        return products.stream()
                .map(ProductResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProductResponseDto getDetailProduct(Long productId, Long storeId) {
        Product product = storeProductRepository.findByProductIdAndStore_StoreId(productId, storeId);
        return ProductResponseDto.fromEntity(product);
    }

    @Transactional
    public ProductResponseDto createProduct(Long storeId, ProductRequestDto requestDto) {
        // store 엔티티 조회
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 가게입니다."));

        Product product = new Product();
        product.setStore(store);
        product.setName(requestDto.getName());
        product.setPrice(requestDto.getPrice());
        product.setDescription(requestDto.getDescription());
        product.setImageUrl(requestDto.getImageUrl());

        // 카테고리도 엔티티로 세팅
        if (requestDto.getCategoryId() != null) {
            Category category = categoryRepository.findById(requestDto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("존재하지 않는 카테고리입니다."));
            product.setCategory(category);
        }

        product.setStatus(ServiceStatus.CONFIRMED);

        Product result = storeProductRepository.save(product);

        return ProductResponseDto.fromEntity(result);
    }


    @Transactional
    public ProductResponseDto updateProduct(Long storeId, ProductRequestDto requsetDto) {
        Product product = storeProductRepository.findById(requsetDto.getProductId())
                .orElseThrow(() -> new RuntimeException("상품 없음"));

        // storeId 세팅
        product.getStore().setStoreId(storeId);

        if (requsetDto.getName() != null)
            product.setName(requsetDto.getName());

        if (requsetDto.getPrice() != null)
            product.setPrice(requsetDto.getPrice());

        if (requsetDto.getDescription() != null)
            product.setDescription(requsetDto.getDescription());

        System.out.println(requsetDto.getCategoryId() + "카테고리----------------------------------------");
        // Category 업데이트
        if (requsetDto.getCategoryId() != null) {
            Long categoryId = requsetDto.getCategoryId();
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("카테고리 없음"));

            product.setCategory(category);
        }

        product.setStatus(ServiceStatus.CONFIRMED);

        return ProductResponseDto.fromEntity(product);
    }

    @Transactional
    public ProductResponseDto deleteProduct(Long storeId, Long productId) {
        Product product = storeProductRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("상품 없음"));

        // 소유자 체크 필요하면 추가
//        if (!product.getStoreId().equals(storeId)) {
//            throw new RuntimeException("권한 없음");
//        }

        product.setStatus(ServiceStatus.DELETED); // 논리 삭제 처리

        return ProductResponseDto.fromEntity(product); // 트랜잭션 안에서 자동 반영
    }
    @Override
    public List<CategoryDto> getCategories(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 매장입니다."));
        return categoryRepository.findAllByStoreAndIsActiveTrue(store).stream()
                .map(CategoryDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategory(Long storeId, Long categoryId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 매장입니다."));
        return categoryRepository.findByIdAndStoreAndIsActiveTrue(categoryId, store)
                .map(CategoryDto::fromEntity)
                .orElse(null);
    }

    @Override
    public CategoryDto createCategory(CategoryDto requestDto) {
        Store store = storeRepository.findByStoreId(requestDto.getStoreId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 매장입니다."));

        Category category = Category.builder()
                .store(store)
                .name(requestDto.getCategoryName())
                .isActive(true)
                .build();

        Category saved = categoryRepository.save(category);

        // 기존 변환 메서드 사용
        return CategoryDto.fromEntity(saved);
    }


    @Override
    public CategoryDto updateCategory(Long storeId, CategoryDto requestDto) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 매장입니다."));

        Category category = categoryRepository.findByIdAndStoreAndIsActiveTrue(requestDto.getCategoryId(), store)
                .orElse(null);
        if (category == null) return null;

        if (requestDto.getCategoryName() != null) {
            category.setName(requestDto.getCategoryName());
        }

        Category saved = categoryRepository.save(category);

        return CategoryDto.fromEntity(saved);
    }

    @Transactional
    public void deleteCategory(Long storeId, Long categoryId) {
        // 1. 카테고리 존재 여부 확인
        Category category = categoryRepository.findByIdAndStore_StoreId(categoryId, storeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));

        // 2. 상품 존재 여부 확인
        boolean hasProducts = storeProductRepository.existsByCategoryIdAndStore_StoreId(categoryId, storeId);
        if (hasProducts) {
            throw new IllegalStateException("카테고리에 상품이 존재하여 삭제할 수 없습니다.");
        }

        // 3. 삭제 (soft delete)
        category.setIsActive(false); // isActive = false
        categoryRepository.save(category);
    }


}
