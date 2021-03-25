package com.example.hardwaremall.bean;

import java.io.Serializable;

public class Reorder implements Serializable {
    private OrderItems orderItems;
    private int qtyInStock;
    private double price;
    private double discount;

    public Reorder() {

    }

    public Reorder(OrderItems orderItems, int qtyInStock, double price, double discount) {
        this.orderItems = orderItems;
        this.qtyInStock = qtyInStock;
        this.price = price;
        this.discount = discount;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public OrderItems getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(OrderItems orderItems) {
        this.orderItems = orderItems;
    }

    public int getQtyInStock() {
        return qtyInStock;
    }

    public void setQtyInStock(int qtyInStock) {
        this.qtyInStock = qtyInStock;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
