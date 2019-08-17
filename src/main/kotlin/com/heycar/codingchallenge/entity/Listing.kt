package com.heycar.codingchallenge.entity

import java.io.Serializable
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "listings")
class Listing(
        @Id
        private val code: String,
        private val make: String,
        private val model: String,
        private val power: Int,
        private val kw: Int,
        private val year: Int,
        private val color: String,
        private val price: Int,
        @Id
        private val dealerId: String
): Serializable