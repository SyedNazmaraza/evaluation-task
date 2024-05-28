package com.springboot.evaluation_task.service;

import com.springboot.evaluation_task.dto.BaseResponse;
import com.springboot.evaluation_task.dto.OrderRequest;

public interface OrderService {
    public BaseResponse createOrder(OrderRequest orderRequest);
    public BaseResponse updateOrder(Long orderId ,OrderRequest orderRequest);
    public BaseResponse deleteOrder(Long orderId);
    public BaseResponse getOrders();
}
