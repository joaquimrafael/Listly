package com.exemplo.softwarelab.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Item {
    
    @Id
    private long id;
    private long ListId;
    private long ProductId;
    private int quantity;

    public Item() {}
    public Item(long listId, long productId, int quantity) {
        this.ListId = listId;
        this.ProductId = productId;
        this.quantity = quantity;
        this.id = listId * 1000 + productId;
    }
    public long getListId() {
        return ListId;
    }
    public void setListId(long listId) {
        this.ListId = listId;
    }
    public long getProductId() {
        return ProductId;
    }
    public void setProductId(long productId) {
        this.ProductId = productId;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
