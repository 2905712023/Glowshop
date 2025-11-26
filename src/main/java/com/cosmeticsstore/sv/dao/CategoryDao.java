package com.cosmeticsstore.sv.dao;

import java.util.List;

import com.cosmeticsstore.sv.model.Categories;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class CategoryDao {
    
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("GlowShopPU");
    
    public List<Categories> findAllToShowSelect() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createNamedQuery("Categories.findAllToShowSelect", Categories.class).getResultList();
        } finally {
            em.close();
        }
    }

    public List<Categories> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createNamedQuery("Categories.findAll", Categories.class).getResultList();
        } finally {
            em.close();
        }
    }

    public Categories findById(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createNamedQuery("Categories.findByCategoryId", Categories.class)
                     .setParameter("categoryId", id)
                     .getSingleResult();
        } finally {
            em.close();
        }
    }

    public void create(Categories category) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(category);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void update(Categories category) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(category);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public int delete(int categoryId) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            //Verificar si hay productos asociados a esta categoría
            Long count = em.createQuery(
                    "SELECT COUNT(p) FROM Products p WHERE p.category.categoryId = :id",
                    Long.class)
                    .setParameter("id", categoryId)
                    .getSingleResult();

            if (count > 0) {
                // Si hay productos, no eliminamos
                return -1; // Código especial: no se puede eliminar
            }

            //Buscar la categoría
            Categories category = em.find(Categories.class, categoryId);
            if (category != null) {
                em.remove(category);
                tx.commit();
                return 1; // Eliminación exitosa
            } else {
                return 0; // No existe
            }

        } catch (IllegalStateException e) {
            if (tx.isActive()) tx.rollback();
            System.err.println(e.getMessage());
            return -1; // Código especial: no se puede eliminar
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return 0; // Otro error
        } finally {
            em.close();
        }
    }

}
