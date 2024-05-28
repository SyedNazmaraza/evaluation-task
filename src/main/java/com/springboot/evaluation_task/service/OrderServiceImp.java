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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class OrderServiceImp implements OrderService {
    private OrdersRepository ordersRepository;
    private OrderDetailsRepository orderDetailsRepository;
    private CustomerRepository customerRepository;
    private ProductRepository productRepository;

    @Override
    public BaseResponse createOrder(OrderRequest orderRequest) {
        Customer customer = customerRepository.findById(orderRequest.getCustomerId())
                .orElseThrow(() -> new OrderServiceException("Customer not found"));
        List<Product> productList = validatingProducts(orderRequest.getProductsIdAndItsQuantities());
        Orders orders = Orders.builder()
                .customer(customer)
                .build();
        ordersRepository.save(orders);
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
                .message("success")
                .data(orders)
                .build();
    }

    @Override
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
                orderDetailsRepository.deleteByProductId(product.getId());
            }
            return BaseResponse.builder()
                    .status("0")
                    .message("Successfully removed")
                    .data(products)
                    .build();
        }
        if (orderRequest.getChangeQuantity()) {
            for (Product product : products) {
                orderDetailsRepository.deleteByProductId(product.getId());
                orderDetailsRepository.save(OrderDetails.builder()
                        .orderId(orderId)
                        .productId(product.getId())
                        .quantity(orderRequest.getProductsIdAndItsQuantities().get(product.getId()))
                        .build());
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
    public BaseResponse getOrders() {
        return null;
    }

    private List<Product> validatingProducts(Map<Long, Integer> productsIdAndItsQuantities) {
        List<Product> product = productRepository.findAllById(productsIdAndItsQuantities.keySet());
        if (product.size() == productsIdAndItsQuantities.size()) {
            return product;
        }
        throw new OrderServiceException("Product is Not Present");
    }
}
