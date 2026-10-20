package com.example.finance

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.nio.file.Files
import java.nio.file.Paths

@SpringBootApplication
class FinanceApplication

fun main(args: Array<String>) {
    // Run the backend with the backend folder as the working directory.
    // This guarantees that ./data exists before SQLite opens the database.
    Files.createDirectories(Paths.get("data"))
    runApplication<FinanceApplication>(*args)
}
