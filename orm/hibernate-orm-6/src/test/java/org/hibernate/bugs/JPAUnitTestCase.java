package org.hibernate.bugs;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;

import org.hibernate.bugs.entities.A;
import org.hibernate.bugs.entities.B;
import org.hibernate.bugs.entities.C;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * This template demonstrates how to develop a test case for Hibernate ORM, using the Java Persistence API.
 */
public class JPAUnitTestCase {

	private EntityManagerFactory entityManagerFactory;

	@Before
	public void init() {
		entityManagerFactory = Persistence.createEntityManagerFactory( "templatePU" );
	}

	@After
	public void destroy() {
		entityManagerFactory.close();
	}

	// Entities are auto-discovered, so just add them anywhere on class-path
	// Add your tests, using standard JUnit.
	@Test
	public void collectionMappedByLazyClear_NotEmpty() throws Exception {
		// Setup
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
		
		A a = new A();
		a.name = "name1";
		B b = new B();
		C c = new C();
		a.bs.add(b);
		b.a = a;
		c.a = a;
		
		entityManager.persist(a);
		entityManager.persist(b);
		entityManager.persist(c);
		entityManager.getTransaction().commit();
		entityManager.close();
		
		// Update A entity, Clear A.bs mappedBy collection.
		// Trigger flush on A entity with select C join A query.
		EntityManager entityManager2 = entityManagerFactory.createEntityManager();
		
		a = entityManager2.find(A.class, a.id);
		b = entityManager2.find(B.class, b.id);
		
		a.name = "name2";
		
		entityManager2.getTransaction().begin();
		a.bs.clear();
		a.bs.addAll(new ArrayList<>());
		
		entityManager2.createQuery("SELECT c FROM C c JOIN A a ON c.a = a WHERE a = ?1", C.class).setParameter(1, a).getResultList();
		
		// a.bs is unexpectedly reloaded from database and so not empty.
		assertThat(a.bs).isEmpty();
		
		entityManager2.getTransaction().commit();
		entityManager2.close();
	}
}
