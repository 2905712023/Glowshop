package com.cosmeticsstore.sv.model;

public class CartItem {

    private Products producto; 
    private int cantidad;
    
    public CartItem(Products producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public CartItem() {}

    public double getSubtotal() {
        return this.producto.getPrice().doubleValue() * this.cantidad;
    }
    
    public Products getProducto() {
        return producto;
    }

    public void setProducto(Products producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
