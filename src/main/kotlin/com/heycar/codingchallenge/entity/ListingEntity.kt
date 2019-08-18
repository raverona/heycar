package com.heycar.codingchallenge.entity

import com.fasterxml.jackson.annotation.JsonCreator
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "listings")
data class ListingEntity(
        @EmbeddedId
        val id: ListingId,
        val make: String,
        val model: String,
        val power: Int?,
        val kw: Int?,
        val year: Int,
        val color: String,
        val price: Int
): Serializable {
        @JsonCreator
        constructor(code: String,
                    dealerId: String?,
                    make: String,
                    model: String,
                    power: Int?,
                    kw: Int?,
                    year: Int,
                    color: String,
                    price: Int): this(ListingId(code, dealerId.orEmpty()), make, model, power, kw, year, color, price
        )

        @Embeddable
        data class ListingId (
                val code: String,
                val dealerId: String
        ): Serializable
}