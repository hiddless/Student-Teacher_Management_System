package com.hiddless.dao;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface IDaoGenerics<T> {

    Optional<T> create(T t);
    List<T> list();
    Optional<T> findByName(String name);
    Optional<T> findById(int id);
    Optional<T> update(int id, T t);
    Optional<T> delete(int id);
    void chooise();

    default Connection getInterfaceConnection() {
        return null;
    }
}
