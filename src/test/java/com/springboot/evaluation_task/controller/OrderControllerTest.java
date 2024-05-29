package com.springboot.evaluation_task.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.evaluation_task.dto.BaseResponse;
import com.springboot.evaluation_task.dto.OrderRequest;
import com.springboot.evaluation_task.repository.OrdersRepository;
import com.springboot.evaluation_task.service.OrderService;
import com.springboot.evaluation_task.utils.Helper;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
class OrderControllerTest {
    @InjectMocks
    private OrderController orderController;
    @Mock
    private OrderService orderService;
    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.orderController).build();
    }

    @Test
    void createOrder() throws Exception {
        OrderRequest orderRequest = Helper.getOrderRequest();
        when(orderService.createOrder(any())).thenReturn(Helper.createOrderResponse());
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/order/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(new ObjectMapper().writeValueAsString(orderRequest)))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void getAllOrdersById() {
    }

    @Test
    void updateOrder() {
    }

    @Test
    void deleteOrder() {
    }
}