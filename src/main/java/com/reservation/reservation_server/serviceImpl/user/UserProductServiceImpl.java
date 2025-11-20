package com.reservation.reservation_server.serviceImpl.user;

import com.reservation.reservation_server.common.ServiceStatus;
import com.reservation.reservation_server.config.Exception.ProductNotFoundException;
import com.reservation.reservation_server.dto.CategoryDto;
import com.reservation.reservation_server.dto.ProductDto;
import com.reservation.reservation_server.dto.product.ProductRequestDto;
import com.reservation.reservation_server.dto.product.ProductResponseDto;
import com.reservation.reservation_server.entity.Category;
import com.reservation.reservation_server.entity.Product;
import com.reservation.reservation_server.repository.CategoryRepository;
import com.reservation.reservation_server.repository.StoreProductRepository;
import com.reservation.reservation_server.repository.UserProductRepository;
import com.reservation.reservation_server.service.store.StoreProductService;
import com.reservation.reservation_server.service.user.UserProductService;
import jakarta.transaction.Transactional;
import org.hibernate.annotations.NotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.MissingResourceException;
import java.util.stream.Collectors;

@Service
public class UserProductServiceImpl implements UserProductService {

    private final UserProductRepository userProductRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public UserProductServiceImpl(UserProductRepository userProductRepository, CategoryRepository categoryRepository) {
        this.userProductRepository = userProductRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<ProductDto> getProduct() {
        return userProductRepository.findAll().stream()
                .map(ProductDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> getProductsByCategory(Long categoryId) {
        return userProductRepository.findByCategory_Id(categoryId).stream()
                .map(ProductDto::fromEntity)
                .collect(Collectors.toList());
    }


    @Transactional
    public ProductResponseDto getDetailProduct(Long productId) {
        Product product = userProductRepository.findById(productId)
                .orElseThrow(ProductNotFoundException :: new);
//         테스트용
//        Product product = userProductRepository.findById(productId)
//                .orElseThrow(() -> new RuntimeException("product not found"));


        return ProductResponseDto.fromEntity(product);
    }

    @Transactional
    public List<CategoryDto> getCategory() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryDto::fromEntity)
                .collect(Collectors.toList());
    }


}