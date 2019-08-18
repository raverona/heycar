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
                        csvFileLines.shouldMatchGenerated(listingEntities)
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

private fun List<String>.shouldMatchGenerated(listingEntities: List<ListingEntity>) {
    val csvLines = this.toMutableList()
    val csvHeader = csvLines.removeAt(0).split(",", "/")
    val csvLinesWithoutHeader = csvLines.map { line -> line.split(",", "/") }

    csvLinesWithoutHeader.forEach { line ->
        val matchingListing = listingEntities.first { listing -> listing.id.code.equals(line[csvHeader.indexOf("code")]) }

        line[csvHeader.indexOf("make")] shouldBe matchingListing.make
        line[csvHeader.indexOf("model")] shouldBe matchingListing.model
        line[csvHeader.indexOf("power-in-ps")].toInt() shouldBe matchingListing.power
        line[csvHeader.indexOf("year")].toInt() shouldBe matchingListing.year
        line[csvHeader.indexOf("color")] shouldBe matchingListing.color
        line[csvHeader.indexOf("price")].toInt() shouldBe matchingListing.price
    }
}
