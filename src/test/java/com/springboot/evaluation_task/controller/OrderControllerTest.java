package com.springboot.evaluation_task.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.evaluation_task.dto.BaseResponse;
import com.springboot.evaluation_task.dto.OrderRequest;
import com.springboot.evaluation_task.repository.OrderDetailsRepository;
import com.springboot.evaluation_task.repository.OrdersRepository;
import com.springboot.evaluation_task.service.OrderService;
import com.springboot.evaluation_task.utils.Helper;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc(addFilters = false)
class OrderControllerTest {
    @InjectMocks
    private OrderController orderController;
    @Autowired
    private MockMvc mockMvc;
    @Mock
    private OrderService orderService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    void createOrder() throws Exception {
        OrderRequest orderRequest = Helper.getOrderRequest();
        when(orderService.createOrder(any())).thenReturn(Helper.createOrderResponse());
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/orders/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(new ObjectMapper().writeValueAsString(orderRequest)))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void getAllOrdersById() throws Exception {
        BaseResponse response = Helper.createOrderResponse();
        response.setData(Helper.ordersDetailsList().get(0));
//        when(orderDetailsRepository.findById(1L)).thenReturn(Optional.ofNullable(Helper.ordersDetailsList().get(0)));
        when(orderService.getOrders(12L)).thenReturn(response);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/orders/getAll/{id}", 12L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(new ObjectMapper().writeValueAsString(response))))
                .andExpect(status().isOk());
    }

    @Test
    void updateOrder() throws Exception {
        OrderRequest orderRequest = Helper.getOrderRequest();
        when(orderService.updateOrder(any(), any())).thenReturn(Helper.createOrderResponse());
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/orders/update/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(new ObjectMapper().writeValueAsString(orderRequest)))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void deleteOrder() throws Exception {
        when(orderService.deleteOrder(any())).thenReturn(Helper.createOrderResponse());
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/orders/delete/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Objects.requireNonNull(new ObjectMapper().writeValueAsString(Helper.createOrderResponse())))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}