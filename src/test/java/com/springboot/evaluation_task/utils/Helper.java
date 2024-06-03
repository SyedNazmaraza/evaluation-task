package com.springboot.evaluation_task.utils;

import com.springboot.evaluation_task.dto.BaseResponse;
import com.springboot.evaluation_task.dto.OrderRequest;
import com.springboot.evaluation_task.entity.Customer;
import com.springboot.evaluation_task.entity.OrderDetails;
import com.springboot.evaluation_task.entity.Orders;
import com.springboot.evaluation_task.entity.Product;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Helper {
    public static Customer getCustomer() {
        return Customer.builder()
                .id(1L)
                .name("CustomerName")
                .email("CustomerEmail")
                .build();
    }

    public static OrderRequest getOrderRequest() {
        return OrderRequest.builder()
                .customerId(1L)
                .productsIdAndItsQuantities(Map.of(
                        1L, 2,
                        2L, 3
                ))
                .build();
    }

    public static OrderRequest getOrderRequestForUpdateAddProducts() {
        return OrderRequest.builder()
                .customerId(1L)
                .addProduct(true)
                .productsIdAndItsQuantities(Map.of(
                        1L, 2,
                        2L, 3
                ))
                .build();
    }

    public static OrderRequest getOrderRequestForUpdateRemoveProducts() {
        return OrderRequest.builder()
                .customerId(1L)
                .addProduct(false)
                .changeQuantity(false)
                .removeProduct(true)
                .productsIdAndItsQuantities(Map.of(
                        1L, 2,
                        2L, 3
                ))
                .build();
    }

    public static OrderRequest getOrderRequestForUpdateChangeQuantites() {
        return OrderRequest.builder()
                .customerId(1L)
                .changeQuantity(true)
                .productsIdAndItsQuantities(Map.of(
                        1L, 2,
                        2L, 3
                ))
                .build();
    }

    public static List<Product> productList() {
        return List.of(
                Product.builder()
                        .id(1L)
                        .name("Product1")
                        .price(10.0)
                        .build(),
                Product.builder()
                        .id(2L)
                        .name("Product2")
                        .price(20.0)
                        .build()
        );
    }

    public static List<Orders> ordersList() {
        return List.of(
                Orders.builder()
                        .id(1L)
                        .customer(getCustomer())
                        .build(),
                Orders.builder()
                        .id(2L)
                        .customer(getCustomer())
                        .build()
        );
    }

    public static List<OrderDetails> ordersDetailsList() {
        return List.of(
                OrderDetails.builder()
                        .id("1_1")
                        .quantity(2)
                        .build()
        );
    }

    public static BaseResponse createOrderResponse() {
        return BaseResponse.builder()
                .status("0")
                .message("success")
                .data(ordersList().get(0))
                .build();
    }
}
