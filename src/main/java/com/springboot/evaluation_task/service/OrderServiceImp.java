package com.springboot.evaluation_task.service;

import com.springboot.evaluation_task.advice.OrderServiceException;
import com.springboot.evaluation_task.dto.BaseResponse;
import com.springboot.evaluation_task.dto.OrderRequest;
import com.springboot.evaluation_task.entity.Customer;
import com.springboot.evaluation_task.entity.OrderDetails;
import com.springboot.evaluation_task.entity.Orders;
import com.springboot.evaluation_task.entity.Product;
import com.springboot.evaluation_task.repository.CustomerRepository;
import com.springboot.evaluation_task.repository.OrderDetailsRepository;
import com.springboot.evaluation_task.repository.OrdersRepository;
import com.springboot.evaluation_task.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class OrderServiceImp implements OrderService {
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private OrderDetailsRepository orderDetailsRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public BaseResponse createOrder(OrderRequest orderRequest) {
        Customer customer = customerRepository.findById(orderRequest.getCustomerId())
                .orElseThrow(() -> new OrderServiceException("Customer not found"));
        List<Product> productList = validatingProducts(orderRequest.getProductsIdAndItsQuantities());
        Orders orders = ordersRepository.save(Orders.builder()
                .customer(customer)
                .build());
        for (Product product1 : productList) {
            OrderDetails orderDetails = OrderDetails.builder()
                    .orderId(orders.getId())
                    .productId(product1.getId())
                    .quantity(orderRequest.getProductsIdAndItsQuantities().get(product1.getId()))
                    .build();
            orderDetailsRepository.save(orderDetails);
        }
        return BaseResponse.builder()
                .status("0")
                .message("Created Order")
                .data(orders)
                .build();
    }

    @Override
    @Transactional
    public BaseResponse updateOrder(Long orderId, OrderRequest orderRequest) {
        ordersRepository.findById(orderId)
                .orElseThrow(() -> new OrderServiceException("Order not found"));
        List<Product> products = validatingProducts(orderRequest.getProductsIdAndItsQuantities());
        if (orderRequest.getAddProduct()) {
            for (Product product : products) {
                orderDetailsRepository.save(OrderDetails.builder()
                        .orderId(orderId)
                        .productId(product.getId())
                        .quantity(orderRequest.getProductsIdAndItsQuantities().get(product.getId()))
                        .build());
            }
            return BaseResponse.builder()
                    .status("0")
                    .message("Successfully added")
                    .data(products)
                    .build();
        }
        if (orderRequest.getRemoveProduct()) {
            for (Product product : products) {
                if (orderDetailsRepository.deleteByOrderIdAndProductId(orderId,product.getId()) != 1L)
                    throw new OrderServiceException("Product is not present " + product.getId());
            }
            return BaseResponse.builder()
                    .status("0")
                    .message("Successfully removed")
                    .data(products)
                    .build();
        }

        if (orderRequest.getChangeQuantity()) {
            List<OrderDetails> orderDetails = orderDetailsRepository.findByOrderId(orderId);
            for (Product product : products) {
                orderDetails.forEach(orderDetails1 -> {
                    if (orderDetails1.getProductId().equals(product.getId())) {
                        orderDetails1.setQuantity(orderRequest.getProductsIdAndItsQuantities().get(product.getId()));
                        orderDetailsRepository.save(orderDetails1);
                    }
                });
            }
            return BaseResponse.builder()
                    .status("0")
                    .message("Changed quantities ")
                    .build();
        }
        return BaseResponse.builder()
                .status("0")
                .message("Nothing to Update")
                .build();
    }


    @Override
    @Transactional
    public BaseResponse deleteOrder(Long orderId) {
        Orders orders = ordersRepository.findById(orderId)
                .orElseThrow(() -> new OrderServiceException("Order not found"));
        ordersRepository.deleteById(orderId);
        orderDetailsRepository.deleteByOrderId(orderId);
        return BaseResponse.builder()
                .status("0")
                .message("Successfully deleted")
                .data(orders)
                .build();
    }

    @Override
    public BaseResponse getOrders(Long orderId) {
        OrderDetails orderDetails = orderDetailsRepository.findById(orderId)
                .orElseThrow(() -> new OrderServiceException("Order not found"));
        return BaseResponse.builder()
                .status("0")
                .message("Successfully")
                .data(orderDetails)
                .build();
    }

    private List<Product> validatingProducts(Map<Long, Integer> productsIdAndItsQuantities) {
        List<Product> product = productRepository.findAllById(productsIdAndItsQuantities.keySet());
        if (product.size() == productsIdAndItsQuantities.size()) {
            return product;
        }
        throw new OrderServiceException("Product is Not Present");
    }
}
