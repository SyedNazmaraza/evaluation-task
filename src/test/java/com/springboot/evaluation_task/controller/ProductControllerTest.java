package com.springboot.evaluation_task.controller;

import com.springboot.evaluation_task.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
class ProductControllerTest {

    @InjectMocks
    private ProductController productController;
    @Mock
    private ProductRepository productRepository;

    @Test
    void createProduct() {

    }
}