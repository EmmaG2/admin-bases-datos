package com.fernandogranados.repository;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T> {
  int create(T entity);
  Optional<T> findById(int id);
  List<T> findAll();
  boolean update(int id, T entity);
  boolean delete(int id);
}
