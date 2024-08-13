package com.nerdysoft.library.repository.entity;

import java.util.Set;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "books")
@Data
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;
	private String title;
	private String author;
	private Integer amount;

	@ManyToMany(mappedBy = "books")
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private Set<User> users;
}
