package com.grocery.bhadoriashop.Models;

import java.util.List;

public class Order {

    public Order(){}

    private List<CartProduct> Products;
    private String OrderId;
    private long CreatedDateEPoch;
    private OrderStatus Status;
    private long ExpectedDeliveryTime;
    private User CreatedBy;

    public List<CartProduct> getProducts() {
        return Products;
    }

    public void setProducts(List<CartProduct> products) {
        Products = products;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public long getCreatedDateEPoch() {
        return CreatedDateEPoch;
    }

    public void setCreatedDateEPoch(long createdDateEPoch) {
        CreatedDateEPoch = createdDateEPoch;
    }

    public OrderStatus getStatus() {
        return Status;
    }

    public void setStatus(OrderStatus status) {
        Status = status;
    }

    public long getExpectedDeliveryTime() {
        return ExpectedDeliveryTime;
    }

    public void setExpectedDeliveryTime(long expectedDeliveryTime) {
        ExpectedDeliveryTime = expectedDeliveryTime;
    }

    public User getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(User createdBy) {
        CreatedBy = createdBy;
    }

    public Order(List<CartProduct> products, String orderId, long createdDateEPoch, OrderStatus status, long expectedDeliveryTime, User createdBy) {
        Products = products;
        OrderId = orderId;
        CreatedDateEPoch = createdDateEPoch;
        Status = status;
        ExpectedDeliveryTime = expectedDeliveryTime;
        CreatedBy = createdBy;
    }
}

