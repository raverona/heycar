package com.heycar.codingchallenge.repository

import com.heycar.codingchallenge.entity.ListingEntity
import com.heycar.codingchallenge.entity.ListingEntity.ListingId
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ListingsRepository: CrudRepository<ListingEntity, ListingId>, JpaSpecificationExecutor<ListingEntity>