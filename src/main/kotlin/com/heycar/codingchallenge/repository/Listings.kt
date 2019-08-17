package com.heycar.codingchallenge.repository

import com.heycar.codingchallenge.entity.Listing
import com.heycar.codingchallenge.entity.Listing.ListingId
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface Listings: CrudRepository<Listing, ListingId>