package com.heycar.codingchallenge.repository.specification

import com.heycar.codingchallenge.entity.ListingEntity
import org.springframework.data.jpa.domain.Specification
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

class ListingsSpecification(private val listingsSearchCriteria: ListingsSearchCriteria): Specification<ListingEntity> {

    override fun toPredicate(root: Root<ListingEntity>, query: CriteriaQuery<*>, criteriaBuilder: CriteriaBuilder): Predicate? {
        if (listingsSearchCriteria.value == null) return null
        return criteriaBuilder.equal(root.get<String>(listingsSearchCriteria.attributeName), listingsSearchCriteria.value)
    }
}

class ListingsSearchCriteria(val attributeName: String, val value: Any?)