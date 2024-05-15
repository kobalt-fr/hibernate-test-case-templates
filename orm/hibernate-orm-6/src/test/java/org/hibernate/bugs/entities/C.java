package org.hibernate.bugs.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class C {

	@Id
	@GeneratedValue
	public Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	public A a;
}
