package com.eprogramar.bank.controller

import com.eprogramar.bank.model.Account
import com.eprogramar.bank.repository.AccountRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/account")
class AccountController(val repository: AccountRepository) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody account: Account): Account =
        repository.save(account)

    @GetMapping
    fun getAll(): List<Account> = repository.findAll()

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<Account> =
        repository.findById(id).map {
            ResponseEntity.ok(it)
        }.orElse(ResponseEntity.notFound().build())

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody account: Account): ResponseEntity<Account> =
        repository.findById(id).map {
            val accountToUpdate = it.copy(
                name = account.name,
                phone = account.phone,
                document = account.document
            )
            ResponseEntity.ok(repository.save(accountToUpdate))
        }.orElse(ResponseEntity.notFound().build())

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: Long): ResponseEntity<Void> =
        repository.findById(id).map {
            repository.delete(it)
            ResponseEntity<Void>(HttpStatus.OK)
        }.orElse(ResponseEntity.notFound().build())
}