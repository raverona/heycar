package com.heycar.codingchallenge.csv

import com.heycar.codingchallenge.entity.ListingEntity

class ListingsCsv(csvFile: String) {
    private var header: List<String>
    private var values: List<List<String>>

    init {
        val csvLines = csvFile.split("\n").toMutableList()
        header = csvLines.removeAt(0).split(",", "/")
        values = csvLines.map { line -> line.split(",", "/") }
    }

    fun asListOfLintingEntities(): List<ListingEntity> {
        return values.map {
            listing -> ListingEntity(
                code = listing[header.indexOf("code")],
                make = listing[header.indexOf("make")],
                model = listing[header.indexOf("model")],
                power = listing[header.indexOf("power-in-ps")].toInt(),
                year = listing[header.indexOf("year")].toInt(),
                color = listing[header.indexOf("color")],
                price = listing[header.indexOf("price")].toInt(),
                dealerId = null, 
                kw = null
        )
        }
    }
}