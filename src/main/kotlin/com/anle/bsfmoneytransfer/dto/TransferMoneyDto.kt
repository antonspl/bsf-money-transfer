package com.anle.bsfmoneytransfer.dto

import java.math.BigDecimal

data class TransferMoneyDto(
        val accountFromId: Long,
        val accountToId: Long,
        val sum: BigDecimal
)
