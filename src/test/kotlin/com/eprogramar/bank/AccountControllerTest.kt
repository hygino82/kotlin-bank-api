package com.eprogramar.bank

import com.eprogramar.bank.model.Account
import com.eprogramar.bank.repository.AccountRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var accountRepository: AccountRepository

    @Test
    fun testFindAll() {
        //cria uma conta
        accountRepository.save(Account(name = "Test1", document = "123", phone = "468751753"))
        accountRepository.save(Account(name = "Test2", document = "456", phone = "469887410"))
        mockMvc.perform(MockMvcRequestBuilders.get("/account"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("\$").isArray)
            .andExpect(MockMvcResultMatchers.jsonPath("\$[0].id").isNumber)
            .andExpect(MockMvcResultMatchers.jsonPath("\$[0].name").isString)
            .andExpect(MockMvcResultMatchers.jsonPath("\$[0].document").isString)
            .andExpect(MockMvcResultMatchers.jsonPath("\$[0].phone").isString)
            .andDo(MockMvcResultHandlers.print())

    }

    @Test
    fun testFindById() {
        //cria uma conta
        val account = accountRepository.save(Account(name = "Test1", document = "123", phone = "468751753"))
        mockMvc.perform(MockMvcRequestBuilders.get("/account/${account.id}"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.id").value(account.id))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.name").value(account.name))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.document").value(account.document))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.phone").value(account.phone))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun testCreate() {
        //limpa a lista
        accountRepository.deleteAll()
        //cria uma conta sem id
        val account = Account(name = "Conta Teste", document = "01234567", phone = "12345678")
        val json = ObjectMapper().writeValueAsString(account)

        mockMvc.perform(
            MockMvcRequestBuilders.post("/account")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.name").value(account.name))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.document").value(account.document))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.phone").value(account.phone))
            .andDo(MockMvcResultHandlers.print())

        Assertions.assertFalse(accountRepository.findAll().isEmpty())
    }

    @Test
    fun testUpdate() {
        //limpa a lista
        accountRepository.deleteAll()
        //cria uma conta sem id
        val account = accountRepository.save(Account(name = "Conta Teste", document = "01234567", phone = "12345678"))
            .copy(name = "Updated")
        val json = ObjectMapper().writeValueAsString(account)

        mockMvc.perform(
            MockMvcRequestBuilders.put("/account/${account.id}")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("\$.name").value(account.name))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.document").value(account.document))
            .andExpect(MockMvcResultMatchers.jsonPath("\$.phone").value(account.phone))
            .andDo(MockMvcResultHandlers.print())

        val buscaPorId = accountRepository.findById(account.id!!)
        Assertions.assertTrue(buscaPorId.isPresent)
        Assertions.assertEquals(account.name, buscaPorId.get().name)
    }

    @Test
    fun testDelete() {
        //limpa a lista
        accountRepository.deleteAll()
        //cria uma conta sem id
        val account = accountRepository.save(Account(name = "Conta Teste", document = "01234567", phone = "12345678"))

        mockMvc.perform(MockMvcRequestBuilders.delete("/account/${account.id}"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(MockMvcResultHandlers.print())

        val buscaPorId = accountRepository.findById(account.id!!)
        Assertions.assertFalse(buscaPorId.isPresent)
    }
}