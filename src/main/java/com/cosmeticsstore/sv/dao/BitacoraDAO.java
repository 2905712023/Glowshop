package com.cosmeticsstore.sv.dao;

import com.cosmeticsstore.sv.model.Bitacora;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class BitacoraDAO {

	private EntityManagerFactory emf = Persistence.createEntityManagerFactory("GlowShopPU");

	public void create(Bitacora b) {
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			em.persist(b);
			tx.commit();
		} catch (Exception e) {
			if (tx.isActive()) tx.rollback();
			throw e;
		} finally {
			em.close();
		}
	}

}
