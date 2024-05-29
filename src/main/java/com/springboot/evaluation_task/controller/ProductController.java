package com.springboot.evaluation_task.controller;

import com.springboot.evaluation_task.dto.BaseResponse;
import com.springboot.evaluation_task.dto.ProductRequest;
import com.springboot.evaluation_task.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;
    @PostMapping("/create")
    public ResponseEntity<BaseResponse> createProduct(@RequestBody ProductRequest productRequest){
        return new ResponseEntity<BaseResponse>(
                productService.saveProduct(productRequest),
                HttpStatus.CREATED
        );
    }

}
