package com.hiddless.dao;


import java.util.List;
import java.util.Optional;

public interface IDaoGenerics<T> {
    Optional<T> create(T entity);
    List<T> list();
    Optional<T> findByName(String name);
    Optional<T> findById(int id);
    Optional<T> update(int id, T entity);
    Optional<T> delete(int id);
    void choose();

}
