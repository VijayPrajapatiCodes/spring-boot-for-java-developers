package com.vijay.springbootlearning.dto;

import java.security.PrivateKey;
import java.util.List;

public class OrderRequest {

    private int customerId;
    private List<OrderItemRequest> items;

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setItems(List<OrderItemRequest> items) {
        this.items = items;
    }

    public OrderRequest(int customerId, List<OrderItemRequest> items) {
        this.customerId = customerId;
        this.items = items;
    }

    public int getCustomerId() {
        return customerId;
    }

    public List<OrderItemRequest> getItems() {
        return items;
    }
}
