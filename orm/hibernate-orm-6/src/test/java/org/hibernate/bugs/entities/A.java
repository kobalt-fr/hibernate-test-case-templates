package org.hibernate.bugs.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class A {

	@Id
	@GeneratedValue
	public Long id;

	@Basic
	public String name;

	@OneToMany(mappedBy = "a", fetch = FetchType.LAZY)
	public List<B> bs = new ArrayList<>();
}
