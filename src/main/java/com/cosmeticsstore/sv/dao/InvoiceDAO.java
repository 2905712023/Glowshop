package com.cosmeticsstore.sv.dao;

import java.util.List;

import com.cosmeticsstore.sv.model.Invoices;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class InvoiceDAO {
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("GlowShopPU");
    
    public Invoices createInvoice(Invoices invoice) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            
            em.persist(invoice);
            em.flush();
            tx.commit();
            return invoice; 
        } catch (Exception e) {
            System.out.println("Error en el DAO al guardar factura: " + e.getMessage());
            e.printStackTrace();
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public Invoices findById(int invoiceId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Invoices.class, invoiceId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public Invoices findByInvoiceIdWithDetails(int invoiceId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                "SELECT i FROM Invoices i LEFT JOIN FETCH i.invoiceDetailsCollection WHERE i.invoiceId = :id", Invoices.class)
                .setParameter("id", invoiceId)
                .getSingleResult();
        } catch (jakarta.persistence.NoResultException ex) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public List<Invoices> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createNamedQuery("Invoices.findAll", Invoices.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new java.util.ArrayList<>();
        } finally {
            if (em.isOpen()) em.close();
        }
    }

    public void delete(int invoiceId) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Invoices invoice = em.find(Invoices.class, invoiceId);
            if (invoice != null) {
                em.remove(invoice);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        } finally {
            if (em.isOpen()) em.close();
        }
    }
}