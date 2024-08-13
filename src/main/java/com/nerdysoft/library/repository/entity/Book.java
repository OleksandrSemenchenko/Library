package com.nerdysoft.library.repository.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.Set;
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
