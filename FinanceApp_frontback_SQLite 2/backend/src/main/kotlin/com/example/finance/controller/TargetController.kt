package com.example.finance.controller

import com.example.finance.model.FinancialTarget
import com.example.finance.repository.FinancialTargetRepository
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/targets")
@CrossOrigin(origins = ["*"])
class TargetController(private val repo: FinancialTargetRepository) {
    @GetMapping fun all() = repo.findAll()
    @GetMapping("/{id}") fun one(@PathVariable id: Long) = repo.findById(id)
    @PostMapping fun create(@RequestBody data: FinancialTarget) = repo.save(data)
    @PutMapping("/{id}") fun update(@PathVariable id: Long, @RequestBody data: FinancialTarget): FinancialTarget {
        return repo.findById(id).map {
            repo.save(data.copy(id = it.id))
        }.orElseThrow { NoSuchElementException("Target not found") }
    }
    @DeleteMapping("/{id}") fun delete(@PathVariable id: Long) = repo.deleteById(id)
}
