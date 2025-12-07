
package com.cosmeticsstore.sv.dao;

import com.cosmeticsstore.sv.model.Categories;
import com.cosmeticsstore.sv.model.Products;
import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class ProductDAO {
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("GlowShopPU");
    
    public List<Products> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createNamedQuery("Products.findAll", Products.class).getResultList();
        } finally {
            em.close();
        }
    }
    
    public int Create(Products product) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            // Obtenemos una referencia "gestionada" de la categoría
            if (product.getCategory() != null && product.getCategory().getCategoryId() != null) {
                Categories managedCategory = em.find(Categories.class, product.getCategory().getCategoryId());
                System.out.println("category :" + managedCategory);
                if (managedCategory == null) {
                    throw new IllegalArgumentException("Categoría no encontrada con ID: " + product.getCategory().getCategoryId());
                }
                product.setCategory(managedCategory);
            }
        System.out.println("producto antes de guardar: "+ product);
            em.persist(product); // producto es nuevo
            em.getTransaction().commit();
            return 1;
        } catch (Exception e) {
            System.out.println("ERROR EN EL DAO AL GUARDAR: " + e.getMessage());
            e.printStackTrace();
            em.getTransaction().rollback();
            return 0;
        } finally {
            em.close();
        }
    }
   
    public int Update(Products product) {
    EntityManager em = emf.createEntityManager();
    EntityTransaction tx = em.getTransaction();
    try {
        tx.begin();

        if (product.getCategory() != null && product.getCategory().getCategoryId() != null) {
            Categories managedCategory = em.find(Categories.class, product.getCategory().getCategoryId());
            product.setCategory(managedCategory);
        }

        em.merge(product); // Hibernate decide si actualiza o crea según el ID
        tx.commit();
        return 1;
    } catch (Exception e) {
        if (tx.isActive()) tx.rollback();
        e.printStackTrace();
        throw e;
    } finally {
        em.close();
    }
}

    public boolean Delete(int productId) {
        EntityManager em = emf.createEntityManager();
        try {
            Products product = em.createNamedQuery("Products.findByProductId", Products.class)
                     .setParameter("productId", productId)
                     .getSingleResult();
            if (product != null) {
                em.getTransaction().begin();
                em.remove(product);
                em.getTransaction().commit();
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }
             
    public Products FindById(int productId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createNamedQuery("Products.findByProductId", Products.class)
                     .setParameter("productId", productId)
                     .getSingleResult();
        } finally {
            em.close();
        }
    }

}
