package com.springboot.evaluation_task.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequest {
    private Long customerId;
    private Boolean addProduct = false;
    private Boolean removeProduct = false;
    private Boolean changeQuantity = false;
    private Map<Long, Integer> productsIdAndItsQuantities;

}

