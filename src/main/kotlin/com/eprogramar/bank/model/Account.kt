package com.eprogramar.bank.model

import jakarta.persistence.*

@Entity(name = "account")
@Table(name = "tb_account")
data class Account(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    val name: String,
    val document: String,
    val phone: String,
)