package com.hamza.microservices.order_service.service;

import com.hamza.microservices.order_service.Repository.OrderRepository;
import com.hamza.microservices.order_service.client.InventoryClient;
import com.hamza.microservices.order_service.dto.OrderRequest;
import com.hamza.microservices.order_service.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final InventoryClient inventoryClient;
    private final OrderRepository orderRepository;

    public void placeOrder(OrderRequest orderRequest){
        boolean isInStock = inventoryClient.isInStock(orderRequest.skuCode(), orderRequest.quantity());

        if(!isInStock){
            throw new RuntimeException("Product " + orderRequest.skuCode() +" Not enough inventory");
        }else {
            Order order = new Order();
            order.setOrderNumber(UUID.randomUUID().toString());
            order.setPrice(orderRequest.price());
            order.setQuantity(orderRequest.quantity());
            order.setSkuCode(orderRequest.skuCode());
            orderRepository.save(order);
        }
    }


}
