package com.springboot.evaluation_task.controller;

import com.springboot.evaluation_task.dto.BaseResponse;
import com.springboot.evaluation_task.dto.OrderRequest;
import com.springboot.evaluation_task.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<BaseResponse> createOrder(@RequestBody OrderRequest orderRequest) {
        return new ResponseEntity<>(
                orderService.createOrder(orderRequest),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/getAll")
    public ResponseEntity<BaseResponse> getAllOrders() {
        return new ResponseEntity<>(
                orderService.getOrders(),
                HttpStatus.OK
        );
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<BaseResponse> updateOrder(@PathVariable Long id ,@RequestBody OrderRequest orderRequest) {
        return new ResponseEntity<>(
                orderService.updateOrder(id,orderRequest),
                HttpStatus.OK
        );
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<BaseResponse> deleteOrder(@PathVariable Long id) {
        return new ResponseEntity<>(
                orderService.deleteOrder(id),
                HttpStatus.OK
        );
    }
}
