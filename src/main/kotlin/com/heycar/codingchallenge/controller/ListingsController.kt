package com.heycar.codingchallenge.controller

import com.heycar.codingchallenge.entity.Listing
import com.heycar.codingchallenge.entity.Listing.ListingId
import com.heycar.codingchallenge.repository.ListingsRepository
import com.heycar.codingchallenge.repository.specification.ListingsSearchCriteria
import com.heycar.codingchallenge.repository.specification.ListingsSpecification
import org.springframework.http.MediaType.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RequestMethod.GET
import org.springframework.web.bind.annotation.RequestMethod.POST

@RestController
class ListingsController(private val listingsRepository: ListingsRepository) {

    @RequestMapping(path = ["/upload_csv/{dealer_id}"], method = [POST], consumes = [TEXT_PLAIN_VALUE], produces = [APPLICATION_JSON_VALUE])
    fun receiveCsvListings(@PathVariable("dealer_id") dealerId: String, @RequestBody csvFile: String): String {
        val lines = csvFile.split("\n")

        return "hello $csvFile"
    }

    @RequestMapping(path = ["/vehicle_listings/{dealer_id}"], method = [POST], consumes = [APPLICATION_JSON_VALUE], produces = [APPLICATION_JSON_VALUE])
    fun receiveJsonListings(@PathVariable("dealer_id") dealerId: String, @RequestBody jsonListings: List<Listing>): List<Listing> {
        return listingsRepository.saveAll(jsonListings.map { it.copy(id = ListingId(it.id.code, dealerId)) }).toList()
    }

    @RequestMapping(path = ["/search"], method = [GET], consumes = [ALL_VALUE], produces = [APPLICATION_JSON_VALUE])
    fun searchListings(@RequestParam(value = "make", required = false) make: String?,
                       @RequestParam(value = "model", required = false) model: String?,
                       @RequestParam(value = "year", required = false) year: Int?,
                       @RequestParam(value = "color", required = false) color: String?): List<Listing> {

        val makeSpecification = ListingsSpecification(ListingsSearchCriteria("make", make))
        val modelSpecification = ListingsSpecification(ListingsSearchCriteria("model", model))
        val yearSpecification = ListingsSpecification(ListingsSearchCriteria("year", year))
        val colorSpecification = ListingsSpecification(ListingsSearchCriteria("color", color))

        return listingsRepository.findAll(makeSpecification.and(modelSpecification).and(yearSpecification).and(colorSpecification))
    }
}