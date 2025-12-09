package com.cosmeticsstore.sv.dao;

import com.cosmeticsstore.sv.model.UserModulePermission;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

public class UserPermissionDAO {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("GlowShopPU");

    public List<UserModulePermission> findByUserId(int userId) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<UserModulePermission> q = em.createQuery("SELECT p FROM UserModulePermission p WHERE p.userId = :uid", UserModulePermission.class);
            q.setParameter("uid", userId);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public boolean exists(int userId, String moduleName) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Long> q = em.createQuery("SELECT COUNT(p) FROM UserModulePermission p WHERE p.userId = :uid AND p.moduleName = :m", Long.class);
            q.setParameter("uid", userId);
            q.setParameter("m", moduleName);
            Long count = q.getSingleResult();
            return count != null && count > 0;
        } finally {
            em.close();
        }
    }

    public boolean grant(int userId, String moduleName) {
        EntityManager em = emf.createEntityManager();
        try {
            if (exists(userId, moduleName)) return false;
            em.getTransaction().begin();
            UserModulePermission p = new UserModulePermission();
            p.setUserId(userId);
            p.setModuleName(moduleName);
            p.setGrantedAt(new Date());
            em.persist(p);
            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

    public boolean revoke(int userId, String moduleName) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<UserModulePermission> q = em.createQuery("SELECT p FROM UserModulePermission p WHERE p.userId = :uid AND p.moduleName = :m", UserModulePermission.class);
            q.setParameter("uid", userId);
            q.setParameter("m", moduleName);
            List<UserModulePermission> list = q.getResultList();
            if (list.isEmpty()) return false;
            em.getTransaction().begin();
            for (UserModulePermission p : list) {
                UserModulePermission managed = em.find(UserModulePermission.class, p.getId());
                if (managed != null) em.remove(managed);
            }
            em.getTransaction().commit();
            return true;
        } catch (NoResultException ex) {
            return false;
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw ex;
        } finally {
            em.close();
        }
    }
}
