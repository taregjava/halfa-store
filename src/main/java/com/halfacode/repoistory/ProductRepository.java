package com.halfacode.repoistory;

import com.halfacode.entity.Category;
import com.halfacode.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    // Add custom queries or methods if needed
    List<Product> findByCategory(Category category);
    @Query("SELECT p FROM Product p")
    List<Product> findByCategory();

}
