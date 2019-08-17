package com.heycar.codingchallenge.controller

import com.heycar.codingchallenge.entity.Listing
import com.heycar.codingchallenge.entity.Listing.ListingId
import com.heycar.codingchallenge.repository.Listings
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RequestMethod.POST
import org.springframework.web.bind.annotation.RequestMethod.GET

@RestController
class ListingController(private val listings: Listings) {

    @RequestMapping(path = ["/upload_csv/{dealer_id}"], method = [POST])
    fun receiveJsonListings(@PathVariable("dealer_id") dealerId: String): String {
        return "Hello $dealerId"
    }

    @RequestMapping(path = ["/vehicle_listings/{dealer_id}"], method = [POST], consumes = [APPLICATION_JSON_VALUE], produces = [APPLICATION_JSON_VALUE])
    fun receiveCsvListings(@PathVariable("dealer_id") dealerId: String, @RequestBody jsonListings: List<Listing>): List<Listing> {
        return listings.saveAll(jsonListings.map { it.copy(id = ListingId(it.id.code, dealerId)) }).toList()
    }

    @RequestMapping(path = ["/search"], method = [GET])
    fun searchListings(@RequestParam(value = "make", required = false) make: String?,
                       @RequestParam(value = "model", required = false) model: String?,
                       @RequestParam(value = "year", required = false) year: Int?,
                       @RequestParam(value = "color", required = false) color: String?): List<Listing> {
        return listings.findAll().toList()
    }
}