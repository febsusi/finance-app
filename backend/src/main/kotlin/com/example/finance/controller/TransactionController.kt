package com.example.finance.controller

import com.example.finance.model.Transaction
import com.example.finance.repository.TransactionRepository
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = ["*"])
class TransactionController(private val repo: TransactionRepository) {
    @GetMapping fun all() = repo.findAll()
    @GetMapping("/{id}") fun one(@PathVariable id: Long) = repo.findById(id)
    @PostMapping fun create(@RequestBody data: Transaction) = repo.save(data)
    @PutMapping("/{id}") fun update(@PathVariable id: Long, @RequestBody data: Transaction): Transaction {
        return repo.findById(id).map {
            repo.save(data.copy(id = it.id))
        }.orElseThrow { NoSuchElementException("Transaction not found") }
    }
    @DeleteMapping("/{id}") fun delete(@PathVariable id: Long) = repo.deleteById(id)
}
