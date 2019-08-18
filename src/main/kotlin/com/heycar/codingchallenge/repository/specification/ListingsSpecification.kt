package com.heycar.codingchallenge.repository.specification

import com.heycar.codingchallenge.entity.Listing
import org.springframework.data.jpa.domain.Specification
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

class ListingsSpecification(private val listingsSearchCriteria: ListingsSearchCriteria): Specification<Listing> {

    override fun toPredicate(root: Root<Listing>, query: CriteriaQuery<*>, criteriaBuilder: CriteriaBuilder): Predicate? {
        if (listingsSearchCriteria.value == null) return null
        return criteriaBuilder.equal(root.get<String>(listingsSearchCriteria.attributeName), listingsSearchCriteria.value)
    }
}

class ListingsSearchCriteria(val attributeName: String, val value: Any?)