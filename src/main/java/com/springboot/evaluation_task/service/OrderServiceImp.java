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
import java.util.Optional;


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
                    .id(String.valueOf(orders.getId() + "_") + String.valueOf(product1.getId()))
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
                Optional<OrderDetails> orderDetails =orderDetailsRepository.findById((orderId) + "_" + String.valueOf(product.getId()));
                if (orderDetails.isPresent()){
                    orderDetailsRepository.save(OrderDetails.builder()
                            .id(String.valueOf(orderId) + "_" + String.valueOf(product.getId()))
                            .quantity(orderDetails.get().getQuantity()+orderRequest.getProductsIdAndItsQuantities().get(product.getId()))
                            .build());
                }
                else {
                    orderDetailsRepository.save(OrderDetails.builder()
                            .id(String.valueOf(orderId) + "_" + String.valueOf(product.getId()))
                            .quantity(orderRequest.getProductsIdAndItsQuantities().get(product.getId()))
                            .build());
                }
            }
            return BaseResponse.builder()
                    .status("0")
                    .message("Successfully added")
                    .data(products)
                    .build();
        }
        if (orderRequest.getRemoveProduct()) {
            System.out.println(orderDetailsRepository.findByIdStartingWith(String.valueOf(orderId)));
            if (orderDetailsRepository.findByIdStartingWith(String.valueOf(orderId)).size() == 1) {
                ordersRepository.deleteById(orderId);
            }
            for (Product product : products) {
                orderDetailsRepository.deleteById(String.valueOf(orderId) + "_" + String.valueOf(product.getId()));
            }
            return BaseResponse.builder()
                    .status("0")
                    .message("Successfully removed")
                    .data(products)
                    .build();
        }

        if (orderRequest.getChangeQuantity()) {
            for (Product product : products) {
                OrderDetails orderDetails1 = orderDetailsRepository.findById(String.valueOf(orderId) + "_" + String.valueOf(product.getId()))
                        .orElseThrow(() -> new OrderServiceException("Order with the product " + product.getId() + " is not found"));
                orderDetails1.setQuantity(orderRequest.getProductsIdAndItsQuantities().get(product.getId()));
                orderDetailsRepository.save(orderDetails1);
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
        orderDetailsRepository.deleteByIdStartingWith(String.valueOf(orderId));
        return BaseResponse.builder()
                .status("0")
                .message("Successfully deleted")
                .data(orders)
                .build();
    }

    @Override
    public BaseResponse getOrders(Long orderId) {
        List<OrderDetails> orderDetails = orderDetailsRepository.findByIdStartingWith(String.valueOf(orderId));
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
