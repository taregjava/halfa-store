package com.halfacode.specifiaction;

import com.halfacode.dto.ProductDTO;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

public class WrappedSpecification<T>
{}
        //implements Specification<T> {

   /* private Specification<T> wrappedSpec;
    private List<String> handledCriteria;
    private ProductDTO searchCriteria;

    public WrappedSpecification(Specification<T> wrappedSpec, List<String> handledCriteria, ProductDTO searchCriteria) {
        Assert.notNull(wrappedSpec, "Wrapped specification must not be null");
        Assert.notNull(handledCriteria, "Handled criteria list must not be null");
        this.wrappedSpec = wrappedSpec;
        this.handledCriteria = handledCriteria;
        this.searchCriteria = searchCriteria;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Predicate predicate = wrappedSpec.toPredicate(root, query, criteriaBuilder);
        return predicate != null ? predicate : criteriaBuilder.isTrue(criteriaBuilder.literal(true));
    }

    public boolean isCriteriaHandled(String criteriaName) {
        // Check if the criteria name is handled in the specification
        // Add more criteria checks here if needed
        if ("name".equals(criteriaName) && searchCriteria.getName() != null) {
            return true;
        }

        // Check for other criteria
        // ...

        return false;
    }

    public List<String> getNotHandledCriteria() {
        List<String> allCriteria = getAllCriteria();
        List<String> notHandledCriteria = new ArrayList<>();
        for (String criteria : allCriteria) {
            if (!handledCriteria.contains(criteria)) {
                notHandledCriteria.add(criteria);
            }
        }
        return notHandledCriteria;
    }

    private List<String> getAllCriteria() {
        String expressionString = toPredicate(null, null, null).toString();
        String[] expressions = expressionString.split(" and | or ");
        List<String> allCriteria = new ArrayList<>();
        for (String expression : expressions) {
            String[] parts = expression.split(" ");
            if (parts.length >= 3) {
                allCriteria.add(parts[1]);
            }
        }
        return allCriteria;
    }
}
*/