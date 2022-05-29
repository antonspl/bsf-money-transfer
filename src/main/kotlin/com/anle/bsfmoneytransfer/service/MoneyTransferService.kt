package com.anle.bsfmoneytransfer.service

import com.anle.bsfmoneytransfer.dto.TransferMoneyDto
import com.anle.bsfmoneytransfer.entity.Account
import java.math.BigDecimal

interface MoneyTransferService {
    fun getAccount(id: Long): Account
    fun createAccount(): Account
    fun increaseBalance(id: Long, sum: BigDecimal): Account
    fun decreaseBalance(id: Long, sum: BigDecimal): Account
    fun transferMoney(dto: TransferMoneyDto): Any
}