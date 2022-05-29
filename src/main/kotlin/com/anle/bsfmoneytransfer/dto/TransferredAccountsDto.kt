package com.anle.bsfmoneytransfer.dto

import com.anle.bsfmoneytransfer.entity.Account

data class TransferredAccountsDto(
        val accountFrom: Account,
        val accountTo: Account
)
