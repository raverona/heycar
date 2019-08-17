package com.heycar.codingchallenge

import org.flywaydb.core.Flyway
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CodingChallengeApplication: CommandLineRunner {
	@Value("\${spring.datasource.url}")
	lateinit var databaseUrl: String

	@Value("\${spring.datasource.username}")
	private lateinit var databaseUser: String

	@Value("\${spring.datasource.password}")
	private lateinit var databasePassword: String

	override fun run(vararg args: String?) {
		val flyway = Flyway.configure().dataSource(databaseUrl, databaseUser, databasePassword).load()
		flyway.migrate()
	}

}

fun main(args: Array<String>) {
	runApplication<CodingChallengeApplication>(*args)
}