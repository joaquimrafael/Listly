package com.exemplo.softwarelab.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Item {
    
    @Id
    private long id;
    private long listId;
    private long productId;
    private int quantity;

    public Item() {}

    public Item(long listId, long productId, int quantity) {
        this.listId = listId;
        this.productId = productId;
        this.quantity = quantity;
        this.id = listId * 1000 + productId;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public long getListId() {
        return listId;
    }
    public void setListId(long listId) {
        this.listId = listId;
    }

    public long getProductId() {
        return productId;
    }
    public void setProductId(long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
