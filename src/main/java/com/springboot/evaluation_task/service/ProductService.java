package com.springboot.evaluation_task.service;

import com.springboot.evaluation_task.dto.BaseResponse;
import com.springboot.evaluation_task.dto.ProductRequest;

public interface ProductService {
    public BaseResponse saveProduct(ProductRequest productRequest);
}
