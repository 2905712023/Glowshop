
package com.cosmeticsstore.sv.model;


public class Product {
    private int productId;
    private String name;
    private String description;
    private Double price;
    private int stock;
    private int categoryId;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int idProducto) {
        this.productId = idProducto;
    }

    public String getName() {
        return name;
    }

    public void setName(String nombre) {
        this.name = nombre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String descripcion) {
        this.description = descripcion;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double precio) {
        this.price = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int idCategoria) {
        this.categoryId = idCategoria;
    }
}
