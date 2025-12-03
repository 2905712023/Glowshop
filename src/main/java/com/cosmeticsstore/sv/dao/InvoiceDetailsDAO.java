package com.cosmeticsstore.sv.dao;

import com.cosmeticsstore.sv.model.InvoiceDetails;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class InvoiceDetailsDAO {
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("GlowShopPU");
    
    public InvoiceDetails createInvoiceDetail(InvoiceDetails detail) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            
            em.persist(detail);
            tx.commit();
            return detail;
        } catch (Exception e) {
            System.out.println("Error en el DAO al guardar detalle: " + e.getMessage());
            e.printStackTrace();
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            if (em.isOpen()) em.close();
        }
    }
}