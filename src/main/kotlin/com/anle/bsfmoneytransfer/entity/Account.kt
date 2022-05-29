package com.anle.bsfmoneytransfer.entity

import java.math.BigDecimal
import javax.persistence.*

@Entity
@Table(name = "account")
data class Account(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

        @Column
        var balance: BigDecimal = BigDecimal.ZERO
)