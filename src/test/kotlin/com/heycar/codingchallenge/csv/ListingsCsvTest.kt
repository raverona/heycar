package com.heycar.codingchallenge.csv

import com.heycar.codingchallenge.entity.ListingEntity
import io.kotlintest.matchers.types.shouldBeInstanceOf
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrowAny
import io.kotlintest.specs.FreeSpec

class ListingsCsvTest: FreeSpec() {
    init {
        "Given a csv file with code, make, model, power-in-ps, year, color and price" - {
            val csvFile = """
                code,make/model,power-in-ps,year,color,price
                1,mercedes/a 180,123,2014,black,15950
                2,audi/a3,111,2016,white,17210
                3,vw/golf,86,2018,green,14980
                4,skoda/octavia,86,2018,not-specified,16990
            """.trimIndent()

            "When a ListingsCsv is built" - {
                val listingsCsv = ListingsCsv(csvFile)

                "And the Listing entities based on it are requested" - {
                    val listingEntities = listingsCsv.asListOfLintingEntities()

                    "Then all lines in the csv should output one entity" {
                        val csvFileLines = csvFile.split("\n")
                        listingEntities.shouldMatch(csvFileLines)
                    }
                }
            }
        }

        "Given a csv file with some other property other than code, make, model, power-in-ps, year, color and price" - {
            val csvFile = """
                code,make/model,power-in-ps,year,color,pricing
                1,mercedes/a 180,123,2014,black,15950
            """.trimIndent()

            "When a ListingsCsv is built" - {
                val listingsCsv = ListingsCsv(csvFile)

                "And the Listing entities based on it are requested" - {
                    val listingEntities = shouldThrowAny { listingsCsv.asListOfLintingEntities() }

                    "Then an exception should be thrown" {
                        listingEntities.shouldBeInstanceOf<Exception>()
                    }
                }
            }
        }

        "Given a csv file missing either code, make, model, power-in-ps, year, color or price" - {
            val csvFile = """
                code,make/model,power-in-ps,year,color
                1,mercedes/a 180,123,2014,black
            """.trimIndent()

            "When a ListingsCsv is built" - {
                val listingsCsv = ListingsCsv(csvFile)

                "And the Listing entities based on it are requested" - {
                    val listingEntities = shouldThrowAny { listingsCsv.asListOfLintingEntities() }

                    "Then an exception should be thrown" {
                        listingEntities.shouldBeInstanceOf<Exception>()
                    }
                }
            }
        }
    }
}

private fun List<ListingEntity>.shouldMatch(csvFileLines: List<String>) {
    val csvLines = csvFileLines.toMutableList()
    val csvHeader = csvLines.removeAt(0).split(",", "/")
    val csvLinesWithoutHeader = csvLines.map { line -> line.split(",", "/") }

    csvLinesWithoutHeader.forEach { line ->
        val matchingListing = this.first { listing -> listing.id.code.equals(line[csvHeader.indexOf("code")]) }

        matchingListing.make shouldBe line[csvHeader.indexOf("make")]
        matchingListing.model shouldBe line[csvHeader.indexOf("model")]
        matchingListing.power shouldBe line[csvHeader.indexOf("power-in-ps")].toInt()
        matchingListing.year shouldBe line[csvHeader.indexOf("year")].toInt()
        matchingListing.color shouldBe line[csvHeader.indexOf("color")]
        matchingListing.price shouldBe line[csvHeader.indexOf("price")].toInt()
    }
}
