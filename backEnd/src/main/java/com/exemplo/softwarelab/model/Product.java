package com.exemplo.softwarelab.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Product {
	
	@Id
	private long id;
	private String name;
	private double price;
	private String description;
	private String priority;
	private String link;

	public Product() {}
	
	public Product(String name, double price, String description, String priority, String link, long id) {
		super();
		this.name = name;
		this.price = price;
		this.description = description;
		this.priority = priority;
		this.link = link;
		this.id = id;
	}
	
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public double getPrice() { return price; }
	public void setPrice(double price) { this.price = price; }
	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }
	public String getPriority() { return priority; }
	public void setPriority(String priority) { this.priority = priority; }
	public String getLink() { return link; }
	public void setLink(String link) { this.link = link; }
	public long getId() { return id; }
	public void setId(long id) { this.id = id; }
	
}
