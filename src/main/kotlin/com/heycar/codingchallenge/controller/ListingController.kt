package com.heycar.codingchallenge.controller

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class ListingController {
    @RequestMapping(path = ["/upload_csv/{dealer_id}"], method = [RequestMethod.POST])
    fun receiveJsonListings(@PathVariable("dealer_id") dealerId: String): String {
        return "Hello $dealerId"
    }

    @RequestMapping
    fun receiveCsvListings() {

    }
}