package com.heycar.codingchallenge.repository

import com.heycar.codingchallenge.entity.Listing
import com.heycar.codingchallenge.entity.Listing.ListingId
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ListingsRepository: CrudRepository<Listing, ListingId>, JpaSpecificationExecutor<Listing>