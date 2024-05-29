package com.springboot.evaluation_task.service;

import com.springboot.evaluation_task.dto.BaseResponse;
import com.springboot.evaluation_task.dto.ProductRequest;
import com.springboot.evaluation_task.entity.Product;
import com.springboot.evaluation_task.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public BaseResponse saveProduct(ProductRequest productRequest) {
        Product product = productRepository.save(Product.builder()
                .description(productRequest.getDescription())
                .name(productRequest.getName())
                .price(productRequest.getPrice())
                .build()
        );
        return BaseResponse.builder()
                .status("0")
                .message("Created Product")
                .data(product)
                .build();
    }
}
