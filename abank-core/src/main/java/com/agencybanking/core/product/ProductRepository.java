/**
 * 
 */
package com.agencybanking.core.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, String>, QuerydslPredicateExecutor<Product> {

    List<Product> findByModuleCode(String moduleCode);

    @Query("select p.events from Product p where p.code = ?1")
    Optional<String> findEventsByCode(String product);

    @Query("select p.workflowEvents from Product p where p.code = ?1")
    Optional<String> findWorkflowEventsByCode(String product);

    Page<Product> findByCodeNotIn(List<String> productCodes, Pageable pageable);
}