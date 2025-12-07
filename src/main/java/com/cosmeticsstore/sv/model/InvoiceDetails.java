/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cosmeticsstore.sv.model;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

/**
 *
 * @author ga571
 */
@Entity
@Table(name = "invoice_details")
@NamedQueries({
    @NamedQuery(name = "InvoiceDetails.findAll", query = "SELECT i FROM InvoiceDetails i"),
    @NamedQuery(name = "InvoiceDetails.findByDetailId", query = "SELECT i FROM InvoiceDetails i WHERE i.detailId = :detailId"),
    @NamedQuery(name = "InvoiceDetails.findByQuantity", query = "SELECT i FROM InvoiceDetails i WHERE i.quantity = :quantity"),
    @NamedQuery(name = "InvoiceDetails.findByUnitPrice", query = "SELECT i FROM InvoiceDetails i WHERE i.unitPrice = :unitPrice"),
    @NamedQuery(name = "InvoiceDetails.findBySubtotal", query = "SELECT i FROM InvoiceDetails i WHERE i.subtotal = :subtotal")})
public class InvoiceDetails implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "detail_id")
    private Integer detailId;
    @Basic(optional = false)
    @Column(name = "quantity")
    private int quantity;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "unit_price")
    private BigDecimal unitPrice;
    @Column(name = "subtotal", insertable = false, updatable = false)
    private BigDecimal subtotal;
    @JoinColumn(name = "invoice_id", referencedColumnName = "invoice_id")
    @ManyToOne(optional = false)
    private Invoices invoiceId;
    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    @ManyToOne(optional = false)
    private Products productId;

    public InvoiceDetails() {
    }

    public InvoiceDetails(Integer detailId) {
        this.detailId = detailId;
    }

    public InvoiceDetails(Integer detailId, int quantity, BigDecimal unitPrice) {
        this.detailId = detailId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public Integer getDetailId() {
        return detailId;
    }

    public void setDetailId(Integer detailId) {
        this.detailId = detailId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public Invoices getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Invoices invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Products getProductId() {
        return productId;
    }

    public void setProductId(Products productId) {
        this.productId = productId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (detailId != null ? detailId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InvoiceDetails)) {
            return false;
        }
        InvoiceDetails other = (InvoiceDetails) object;
        if ((this.detailId == null && other.detailId != null) || (this.detailId != null && !this.detailId.equals(other.detailId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.cosmeticsstore.sv.config.InvoiceDetails[ detailId=" + detailId + " ]";
    }
    
}
