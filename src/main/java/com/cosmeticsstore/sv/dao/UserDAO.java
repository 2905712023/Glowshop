package com.cosmeticsstore.sv.dao;

import com.cosmeticsstore.sv.model.Users;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;

public class UserDAO {

	private EntityManagerFactory emf = Persistence.createEntityManagerFactory("GlowShopPU");

	public Users findByNameAndPassword(String name, String password) {
		EntityManager em = emf.createEntityManager();
		try {
			List<Users> result = em.createQuery("SELECT u FROM Users u WHERE u.name = :name AND u.password = :password", Users.class)
					.setParameter("name", name)
					.setParameter("password", password)
					.getResultList();
			if (result.isEmpty()) return null;
			return result.get(0);
		} finally {
			em.close();
		}
	}

	public Users findByName(String name) {
		EntityManager em = emf.createEntityManager();
		try {
			List<Users> result = em.createQuery("SELECT u FROM Users u WHERE u.name = :name", Users.class)
					.setParameter("name", name)
					.getResultList();
			if (result.isEmpty()) return null;
			return result.get(0);
		} finally {
			em.close();
		}
	}

	public List<Users> findAll() {
		EntityManager em = emf.createEntityManager();
		try {
			return em.createQuery("SELECT u FROM Users u", Users.class).getResultList();
		} finally {
			em.close();
		}
	}

}
