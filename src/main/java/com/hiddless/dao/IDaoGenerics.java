package com.hiddless.dao;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface IDaoGenerics<T> {


    /// Create
    Optional<T> create(T t);

    /// List
    List<T> list();

    /// find by name
    Optional<T> findByName(String name);

    /// find by id
    Optional<T> findById(int id);

    /// update
    Optional<T> update(int id, T t);

    /// delete
    Optional<T> delete(int id);

    /// Chooise
    void chooise();

    default Connection getInterfaceConnection() {
        return null;
    }
}
