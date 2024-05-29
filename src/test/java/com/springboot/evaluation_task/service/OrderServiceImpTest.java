package com.springboot.evaluation_task.service;

import com.springboot.evaluation_task.entity.Orders;
import com.springboot.evaluation_task.repository.CustomerRepository;
import com.springboot.evaluation_task.repository.OrderDetailsRepository;
import com.springboot.evaluation_task.repository.OrdersRepository;
import com.springboot.evaluation_task.repository.ProductRepository;
import com.springboot.evaluation_task.utils.Helper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
class OrderServiceImpTest {
    @Mock
    private OrdersRepository ordersRepository;
    @Mock
    private OrderDetailsRepository orderDetailsRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private CustomerRepository customerRepository;
    @InjectMocks
    private OrderServiceImp orderService;

    @Test
    void createOrder() {
        when(customerRepository.findById(1L)).thenReturn(Optional.ofNullable(Helper.getCustomer()));
        when(productRepository.findAllById(any())).thenReturn(Helper.productList());
        when(ordersRepository.save(any())).thenReturn(Helper.ordersList().get(0));
        when(orderDetailsRepository.save(any())).thenReturn(Helper.ordersDetailsList().get(0));
        Orders order = (Orders) orderService.createOrder(Helper.getOrderRequest()).getData();
        assertEquals(1L,order.getId());
    }

    @Test
    void updateOrder() {
        when(ordersRepository.findById(1L)).thenReturn(Optional.ofNullable(Helper.ordersList().get(0)));
        when(ordersRepository.save(any())).thenReturn(Helper.ordersList().get(0));
        when(orderDetailsRepository.save(any())).thenReturn(Helper.ordersDetailsList().get(0));
        Orders order = (Orders) orderService.updateOrder(1L,Helper.getOrderRequest()).getData();
        assertEquals(1L,order.getId());
    }

    @Test
    void deleteOrder() {
        when(ordersRepository.findById(1L)).thenReturn(Optional.ofNullable(Helper.ordersList().get(0)));
        when(orderDetailsRepository.deleteByOrderId(1L)).thenReturn(1L);
        Orders order = (Orders) orderService.deleteOrder(1L).getData();
        assertEquals(1L,order.getId());
    }
}