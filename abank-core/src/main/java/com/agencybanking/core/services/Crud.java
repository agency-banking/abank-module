/**
 *
 */
package com.agencybanking.core.services;

import com.agencybanking.core.data.SearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author dubic
 */
public interface Crud<T, ID extends Serializable> {

    public T create(T t);

    public T update(T t);

    public void delete(T t);

    public Optional<T> findById(ID id);

    public Page<T> query(T t, PageRequest p);

    public boolean exists(T t, ID id);

    default Long count(T t) {
        return null;
    }

    default Collection<T> list() {
        return Collections.emptyList();
    }

    default List<T> findAll(ID... ids) {
        return Collections.emptyList();
    }

    default void deleteAll(Collection<T> t) {
    }

    default Page<T> findAll(T t, PageRequest p) {
        return null;
    }

    default Page<T> advanceSearch(T t, PageRequest p) {
        return null;
    }

    default Page<T> singleSearch(SearchRequest request, PageRequest p) {
        return null;
    }

    default Page<T> bankSingleSearch(SearchRequest request, PageRequest p) {
        return null;
    }

    default Page<T> bankAdvanceSearch(T t, PageRequest p) {
        return null;
    }

    default Page<T> single(String... args) {
        return null;
    }
}
