package com.halfacode.repoistory;

import com.halfacode.entity.Category;
import com.halfacode.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Add custom queries or methods if needed
    List<Product> findByCategory(Category category);
}
