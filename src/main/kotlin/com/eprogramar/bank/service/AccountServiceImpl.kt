package com.eprogramar.bank.service

import com.eprogramar.bank.model.Account
import com.eprogramar.bank.repository.AccountRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.util.Assert
import java.util.*

@Service
class AccountServiceImpl(private val repository: AccountRepository) : AccountService {

    override fun create(account: Account): Account {
        Assert.hasLength(account.name, "[nome] não pode estar em branco!")
        Assert.isTrue(account.name.length >= 5, "[nome] deve ter no mínimo 5 caracteres!")

        Assert.hasLength(account.document, "[documento] não pode estar em branco!")
        Assert.isTrue(account.document.length >= 11, "[documento] deve ter 11 caracteres!")

        Assert.hasLength(account.phone, "[telefone] não pode estar em branco!")
        Assert.isTrue(account.phone.length >= 11, "[telefone] deve ter 11 caracteres!")

        return repository.save(account)
    }

    override fun getAll(): List<Account> {
        return repository.findAll()
    }

    override fun getById(id: Long): Optional<Account> {
        return repository.findById(id)
    }

    override fun update(id: Long, account: Account): Optional<Account> {
        val optional = getById(id)
        if (optional.isEmpty) Optional.empty<Account>()

        return optional.map {
            val accountToUpdate = it.copy(
                name = account.name,
                phone = account.phone,
                document = account.document
            )
            repository.save(accountToUpdate)
        }
    }

    override fun deleteById(id: Long) {
        repository.findById(id).map {
            repository.delete(it)

        }.orElseThrow { throw RuntimeException("Id $id not found!") }
    }
}